Feature: Item management

  Scenario: Verify an item can be created
    Given an item exists with name "Apple MacBook Pro 16", CPU model "Intel Core i9", and price 1849.99
    When the request to add the item is made
    Then a 200 response code is returned
    And the item with name "Apple MacBook Pro 16" is created

  Scenario: Ability to return an item
    Given an item exists with name "Apple MacBook Pro 16", CPU model "Intel Core i9", and price 1849.99
    When the request to get the item by id is made
    Then the response contains the item with name "Apple MacBook Pro 16"

  Scenario: Ability to list multiple items
    Given multiple items exist
      | name                 | cpuModel      | price   |
      | Apple MacBook Pro 16 | Intel Core i9 | 1849.99 |
      | Dell XPS 13          | Intel Core i7 | 1399.00 |
    When the request to list all items is made
    Then the response contains multiple items

  Scenario: Ability to delete an item
    Given an item exists with name "Apple MacBook Pro 16", CPU model "Intel Core i9", and price 1849.99
    When the request to delete the item is made
    Then a 200 response code is returned
    And the item with name "Apple MacBook Pro 16" no longer exists