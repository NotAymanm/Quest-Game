Feature: Game Acceptance Tests

  Scenario: A1_scenario
    Given the game starts with decks created and 4 players initialized
    And player 1 has hand "F5, F5, F15, F15, D5, S10, S10, H10, H10, B15, B15, L20"
    And player 2 has hand "F5, F5, F15, F15, F40, D5, S10, H10, H10, B15, B15, E30"
    And player 3 has hand "F5, F5, F5, F15, D5, S10, S10, S10, H10, H10, B15, L20"
    And player 4 has hand "F5, F15, F15, F40, D5, D5, S10, H10, H10, B15, L20, E30"

    When player 1 draws a quest of 4 stages
    And player 1 is asked to sponsor and "declines"
    And player 2 is asked to sponsor and "accepts"
    And player 2 builds the 4 stages as "[F5, H10], [F15, S10], [F15, D5, B15], [F40, B15]"

    Then player 1 participates in stage 1
    And player 3 participates in stage 1
    And player 4 participates in stage 1
    And player 1 draws "F30" and discards "F5"
    And player 3 draws "S10" and discards "F5"
    And player 4 draws "B15" and discards "F5"
    And player 1 builds an attack "D5, S10" for stage 1
    And player 3 builds an attack "S10, D5" for stage 1
    And player 4 builds an attack "D5, H10" for stage 1

    And player 1 participates in stage 2
    And player 3 participates in stage 2
    And player 4 participates in stage 2
    And player 1 draws "F10"
    And player 3 draws "L20"
    And player 4 draws "L20"
    And player 1 builds an attack "H10, S10" for stage 2
    And player 3 builds an attack "B15, S10" for stage 2
    And player 4 builds an attack "H10, B15" for stage 2

    And player 1 loses stage 2 with hand as "F5, F10, F15, F15, F30, H10, B15, B15, L20" and 0 shields

    And player 3 participates in stage 3
    And player 4 participates in stage 3
    And player 3 draws "B15"
    And player 4 draws "S10"
    And player 3 builds an attack "L20, H10, S10" for stage 3
    And player 4 builds an attack "B15, S10, L20" for stage 3

    And player 3 participates in stage 4
    And player 4 participates in stage 4
    And player 3 draws "F30"
    And player 4 draws "L20"
    And player 3 builds an attack "B15, H10, L20" for stage 4
    And player 4 builds an attack "D5, S10, L20, E30" for stage 4

    And player 3 loses stage 4 with hand as "F5, F5, F15, F30, S10" and 0 shields

    And player "4" wins with 4 shields and hand as "[F15, F15, F40, L20]"
    And player 2 discards all quest cards and draws 13 cards, then trims to 12 cards


Scenario: 2winner_game_2winner_quest





Scenario: 1winner_game_with_events





Scenario: 0_winner_quest
