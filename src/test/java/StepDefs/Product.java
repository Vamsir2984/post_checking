package StepDefs;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.json.*;

public class Product {
    private Response response;

    private String requestBody;

    @Given("I make a GET request to {string}")
    public void i_make_a_get_request_to(String url) {

        response = RestAssured.get(url);
    }

    @Given("I have the following payload to POST:")
    public void i_have_the_following_payload_to_post(String payload) {

        requestBody = payload;
    }
    @When("I make a POST request to {string}")
    public void i_make_a_post_request_to(String url) {
        response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .post(url);
    }
    @And("the response status code should be {int}")
    public void the_response_status_code_should_be(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
        System.out.println("status code is "+response.getStatusCode());
    }

    @Then("the response should contain the following fields:")
    public void the_response_should_contain_the_following_fields(io.cucumber.datatable.DataTable dataTable) {
        String responseBodyAsString = response.getBody().asString();
        System.out.println("Response Body: " + responseBodyAsString);
        JSONObject responseBody = new JSONObject(responseBodyAsString);

        for (int i = 0; i < dataTable.height(); i++) {
            String field = dataTable.cell(i, 0);
            String type = dataTable.cell(i, 1);
            String presence = dataTable.cell(i, 2);

            Object fieldValue = null;

            try {
                fieldValue = getFieldValue(responseBody, field);
            } catch (JSONException e) {
                Assert.fail("Field " + field + " not found in the response.");
            }

            // Check presence
            if (presence.equalsIgnoreCase("not null")) {
                Assert.assertNotNull(field + " is null", fieldValue);
            }

            // Check type
            switch (type.toLowerCase()) {
                case "integer":
                    Assert.assertTrue(field + " is not an integer", fieldValue instanceof Integer);
                    break;
                case "double":
                    Assert.assertTrue(field + " is not a float", fieldValue instanceof Double);
                    break;
                case "string":
                    Assert.assertTrue(field + " is not a string", fieldValue instanceof String);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }
    }

    public Object getFieldValue(JSONObject jsonObject, String fieldPath) {
        String[] pathElements = fieldPath.split("\\.");

        JSONObject currentObject = jsonObject;
        for (String pathElement : pathElements) {
            if (!currentObject.has(pathElement)) {
                throw new JSONException("Field " + pathElement + " not found in the response.");
            }
            Object fieldValue = currentObject.get(pathElement);
            if (fieldValue instanceof JSONObject) {
                currentObject = (JSONObject) fieldValue;
            } else {
                return fieldValue;
            }
        }

        return currentObject;
    }
}



