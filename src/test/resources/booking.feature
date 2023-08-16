@createbooking
Feature: Create Booking

  Scenario Outline: Validate that a user can make a booking
    Given the guest has a first name "<first_name>", a last name "<last_name>"
    And the guest check-in date is "<check-in>" and check-out "<check-out>" days away from today
    And the guests total price is "<total_price>" and deposit paid is "<deposit_paid>"
    And the booking additional information "<additional_info>"
    When a user creates a booking using xml
    Then the new booking is made
    Examples:
      | first_name | last_name | check-in | check-out | total_price | deposit_paid | additional_info  |
      | Ken        | Smith     | 1        | 2         | 200         | 100          | breakfast        |
