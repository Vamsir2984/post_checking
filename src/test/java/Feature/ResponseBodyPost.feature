Feature: Verify POST request body and response data types

  Scenario: Send POST request and check response
    Given I have the following payload to POST:
      """
      {
         "name": "Apple MacBook Pro 16",
         "data": {
            "year": 2019,

            "CPU model": "Intel Core i9",
            "Hard disk size": "1 TB"
         }
      }
      """
    When I make a POST request to "https://api.restful-api.dev/objects"
    And the response status code should be 200
    Then the response should contain the following fields:
      | id        | string  | not null |
      | name      | string  | not null |
      | data.year | integer | not null |

      | data.CPU model  | string  | not null |
      | data.Hard disk size | string | not null |
      | createdAt | string  | not null |
