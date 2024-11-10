Feature: Game Acceptance Tests

  Scenario: A1_scenario
    Given the game starts with rigged deck for A1_scenario
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

    And player "4" wins the quest, gains 4 shields and has hand as "[F15, F15, F40, L20]"
    And player 2 discards all quest cards and draws 13 cards, then trims to 12 cards


Scenario: 2winner_game_2winner_quest
  Given the game starts with rigged deck for 2winner_game_2winner_quest
  And player 1 has hand "F5, F5, F10, F10, F15, F20, F30, D5, S10, S10, H10, B15"
  And player 2 has hand "F5, F15, F20, F35, D5, S10, S10, H10, B15, B15, B15, L20"
  And player 3 has hand "F5, F5, F10, F10, F15, F20, D5, D5, S10, S10, H10, H10"
  And player 4 has hand "F5, F15, F20, F25, F25, D5, S10, S10, S10, H10, B15, L20"

  When player 1 draws a quest of 4 stages
  And player 1 is asked to sponsor and "accepts"
  And player 1 builds the 4 stages as "[F10], [F5, S10], [F15, D5], [F30]"

  Then player 2 participates in stage 1
  And player 3 participates in stage 1
  And player 4 participates in stage 1
  And player 2 draws "S10" and discards "F5"
  And player 3 draws "F40" and discards "F5"
  And player 4 draws "L20" and discards "F5"
  And player 2 builds an attack "B15" for stage 1
  And player 3 builds an attack "D5" for stage 1
  And player 4 builds an attack "S10" for stage 1

  And player 3 loses stage 1 with hand as "F5, F10, F10, F15, F20, F40, D5, S10, S10, H10, H10" and 0 shields

  And player 2 participates in stage 2
  And player 4 participates in stage 2
  And player 2 draws "F10"
  And player 4 draws "H10"
  And player 2 builds an attack "B15, S10" for stage 2
  And player 4 builds an attack "B15, D5" for stage 2

  And player 2 participates in stage 3
  And player 4 participates in stage 3
  And player 2 draws "D5"
  And player 4 draws "F25"
  And player 2 builds an attack "D5, L20" for stage 3
  And player 4 builds an attack "S10, L20" for stage 3

  And player 2 participates in stage 4
  And player 4 participates in stage 4
  And player 2 draws "H10"
  And player 4 draws "E30"
  And player 2 builds an attack "S10, H10, B15" for stage 4
  And player 4 builds an attack "E30" for stage 4

  And players "2, 4" win the quest, gain 4 shields and has hands as "[F10, F15, F20, F35, D5, S10, H10], [F15, F20, F25, F25, F25, S10, H10, H10, L20]"
  And player 1 discards all quest cards and draws 10 cards, then trims to 12 cards

  And player 2 draws a quest of 3 stages
  And player 2 is asked to sponsor and "declines"
  And player 3 is asked to sponsor and "accepts"
  And player 3 builds the 3 stages as "[F5], [F10], [F10, D5]"

  And player 1 doesn't participate in stage 1 and the rest of the quest
  And player 2 participates in stage 1
  And player 4 participates in stage 1
  And player 2 draws "H10"
  And player 4 draws "S10"
  And player 2 builds an attack "D5" for stage 1
  And player 4 builds an attack "S10" for stage 1

  And player 2 participates in stage 2
  And player 4 participates in stage 2
  And player 2 draws "F5"
  And player 4 draws "S10"
  And player 2 builds an attack "H10" for stage 2
  And player 4 builds an attack "S10" for stage 2

  And player 2 participates in stage 3
  And player 4 participates in stage 3
  And player 2 draws "E30"
  And player 4 draws "L20"
  And player 2 builds an attack "E30" for stage 3
  And player 4 builds an attack "S10, L20" for stage 3

  And players "2, 4" win the quest, gain 3 shields and has hands as "[F5, F10, F15, F20, F35, S10, H10], [F15, F20, F25, F25, F25, H10, H10, L20]"
  And player 3 discards all quest cards and draws 9 cards, then trims to 12 cards

  And players "2, 4" won the game




Scenario: 1winner_game_with_events
  Given the game starts with rigged deck for 1winner_game_with_events
  And player 1 has hand "F5, F5, F10, F10, F15, F25, F40, D5, S10, S10, H10, B15"
  And player 2 has hand "F5, F15, F20, D5, S10, S10, S10, H10, H10, B15, L20, E30"
  And player 3 has hand "F5, F15, F15, F25, S10, S10, H10, H10, H10, B15, B15, L20"
  And player 4 has hand "F5, F20, F20, F25, F30, F40, S10, H10, B15, B15, B15, L20"

  When player 1 draws a quest of 4 stages
  And player 1 is asked to sponsor and "accepts"
  And player 1 builds the 4 stages as "[F5], [F10], [F15], [F10, S10]"

  Then player 2 participates in stage 1
  And player 3 participates in stage 1
  And player 4 participates in stage 1
  And player 2 draws "H10" and discards "F5"
  And player 3 draws "L20" and discards "F5"
  And player 4 draws "D5" and discards "F5"
  And player 2 builds an attack "D5" for stage 1
  And player 3 builds an attack "S10" for stage 1
  And player 4 builds an attack "S10" for stage 1

  And player 2 participates in stage 2
  And player 3 participates in stage 2
  And player 4 participates in stage 2
  And player 2 draws "F10"
  And player 3 draws "S10"
  And player 4 draws "S10"
  And player 2 builds an attack "S10" for stage 2
  And player 3 builds an attack "H10" for stage 2
  And player 4 builds an attack "D5, S10" for stage 2

  And player 2 participates in stage 3
  And player 3 participates in stage 3
  And player 4 participates in stage 3
  And player 2 draws "F20"
  And player 3 draws "D5"
  And player 4 draws "F15"
  And player 2 builds an attack "B15" for stage 3
  And player 3 builds an attack "D5, S10" for stage 3
  And player 4 builds an attack "H10, B15" for stage 3

  And player 2 participates in stage 4
  And player 3 participates in stage 4
  And player 4 participates in stage 4
  And player 2 draws "S10"
  And player 3 draws "L20"
  And player 4 draws "D5"
  And player 2 builds an attack "S10, H10, L20" for stage 4
  And player 3 builds an attack "H10, B15" for stage 4
  And player 4 builds an attack "B15, L20" for stage 4

  And players "2, 3, 4" win the quest, gain 4 shields and has hands as "[F10, F15, F20, F20, S10, S10, H10, H10, E30], [F15, F15, F25, S10, H10, B15, L20, L20, L20], [F15, F20, F20, F25, F30, F40, D5, B15]"
  And player 1 discards all quest cards and draws 9 cards, then trims to 12 cards

  And player 2 draws a "Plague" event and loses 2 shields
  And player 3 draws a "Prosperity" event and all players draw 2 adventure cards, discarding "[], [], [F40, F25], []" in order of players
  And player 4 draws a "Queen's favor" event and draws 2 adventure cards

  And player 1 draws a quest of 3 stages
  And player 1 is asked to sponsor and "accepts"
  And player 1 builds the 3 stages as "[F15], [F20], [F25]"

  And player 2 participates in stage 1
  And player 3 participates in stage 1
  And player 4 participates in stage 1
  And player 2 draws "D5"
  And player 3 draws "F10"
  And player 4 draws "F35" and discards "F10"
  And player 2 builds an attack "D5, H10" for stage 1
  And player 3 builds an attack "D5, S10" for stage 1
  And player 4 builds an attack "H10" for stage 1

  And player 4 loses stage 1 with hand as "F5, F15, F20, F20, F20, F25, F30, F35, F40, D5, B15" and 4 shields

  And player 2 participates in stage 2
  And player 3 participates in stage 2
  And player 2 draws "F5"
  And player 3 draws "F35"
  And player 2 builds an attack "S10, H10" for stage 2
  And player 3 builds an attack "L20" for stage 2

  And player 2 participates in stage 3
  And player 3 participates in stage 3
  And player 2 draws "S10"
  And player 3 draws "F50"
  And player 2 builds an attack "L20, S10" for stage 3
  And player 3 builds an attack "B15, E30" for stage 3

  And players "2, 3" win the quest, gain 3 shields and has hands as "[F5, F10, F15, F20, F20, S10, H10, E30], [F10, F15, F15, F25, F35, F50, H10, L20, L20]"
  And player 1 discards all quest cards and draws 6 cards, then trims to 12 cards

  And players "3" won the game


Scenario: 0_winner_quest
