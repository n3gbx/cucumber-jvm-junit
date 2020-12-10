Feature: Flight payment
  As an Ryanair user I want to book a flight
  As a result of entering invalid card details
  I want to get payment declined message

  # put freshly created user's credentials here
  Background:
    Given I am on the Ryanair home page
    And I am logged in with "" email using "" password

  Scenario: Receive booking payment error on invalid card details
    Given I make a booking from "DUB" to "BER" airports on "2021-03-28" for following passengers count
      | ADULT | 2 |
      | CHILD | 1 |
      | TEEN  | 1 |
    When I set booking payment card details "5555 5555 5555 5554", "10/26", "265" and "CARD HOLDER"
    And I set any billing details for booking payment
    Then I submit payment and should get payment declined message
