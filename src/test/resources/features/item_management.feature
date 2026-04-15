Feature: Item management

  @smoke
  Scenario: Verify an item can be created
    Given an item exists
    When the request to create the item is made
    Then the response status code is 200
    And the item is created successfully

  @smoke
  Scenario: Verify a specific item can be created
    Given an item exists with name "Apple MacBook Pro 16", CPU model "Intel Core i9", and price 1849.99
    When the request to create the item is made
    Then the response status code is 200
    And the item is created successfully

  @smoke
  Scenario: Ability to fetch an item by id
    Given an item exists
    When the request to create the item is made
    And the request to get the item by id is made
    Then the response contains the item

  @regression
  Scenario: Ability to list multiple items
    Given multiple items exist
      | name                 | cpuModel      | price   |
      | Apple MacBook Pro 16 | Intel Core i9 | 1849.99 |
      | Dell XPS 13          | Intel Core i7 | 1399.00 |
    When the request to list all items is made
    Then the response contains multiple items

  @regression
  Scenario: Ability to delete an item
    Given an item exists
    When the request to create the item is made
    Then the response status code is 200
    And the request to delete the item is made
    And the item is deleted successfully