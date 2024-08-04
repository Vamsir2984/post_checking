Feature: Verify response body for GET /objects/{id}

  Scenario: Verify response body fields
    Given I make a GET request to "https://api.restful-api.dev/objects/7"
    And the response status code should be 200
    Then the response should contain the following fields:

      | id              | string  | not null |
      | name            | string  | not null |
      | data.year       | integer | not null |

      | data.CPU model  | string  | not null |
      | data.Hard disk size | string | not null |
