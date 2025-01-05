package org.example;

import java.util.Arrays;
import java.util.List;

public class TestDecks {
    public static List<EventCard> getEventCardsForScenario(String scenario) {

        if ("A1_scenario".equals(scenario)) {
            return Arrays.asList(
                    new EventCard("Q4", "Quest")
            );
        }

        if("2winner_game_2winner_quest".equals(scenario)){
            return Arrays.asList(
                    new EventCard("Q4", "Quest"),
                    new EventCard("Q3", "Quest")
            );
        }

        if("1winner_game_with_events".equals(scenario)){
            return Arrays.asList(
                    new EventCard("Q4", "Quest"),
                    new EventCard("Plague", "Event"),
                    new EventCard("Prosperity", "Event"),
                    new EventCard("Queen's Favor", "Event"),
                    new EventCard("Q3", "Quest")
            );
        }

        if("0_winner_quest".equals(scenario)){
            return Arrays.asList(
                    new EventCard("Q2", "Quest")
            );
        }

        return null;
    }

    public static List<AdventureCard> getAdventureCardsForScenario(String scenario) {

        if ("A1_scenario".equals(scenario)) {
            return Arrays.asList(
                    //P1
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "L20", 20),

                    //P2
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F40", 40),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "E30", 30),

                    //P3
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "L20", 20),

                    //P4
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F40", 40),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "L20", 20),
                    new AdventureCard("Weapon", "E30", 30),

                    //Draws
                    new AdventureCard("Foe", "F30", 30),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "B15", 15),

                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Weapon", "L20", 20),
                    new AdventureCard("Weapon", "L20", 20),

                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "S10", 10),

                    new AdventureCard("Foe", "F30", 30),
                    new AdventureCard("Weapon", "L20", 20),

                    // P2 draws 13
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F35", 35),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10)

            );
        }


        if("2winner_game_2winner_quest".equals(scenario)){
            return Arrays.asList(
                    //P1
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "L20", 20),

                    //P2
                    new AdventureCard("Foe", "F40", 40),
                    new AdventureCard("Foe", "F50", 50),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "L20", 20),
                    new AdventureCard("Weapon", "L20", 20),
                    new AdventureCard("Weapon", "E30", 30),

                    //P3
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),

                    //P4
                    new AdventureCard("Foe", "F50", 50),
                    new AdventureCard("Foe", "F70", 70),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "L20", 20),
                    new AdventureCard("Weapon", "L20", 20),
                    new AdventureCard("Weapon", "E30", 30),

                    //Draws
                    //Stage 1
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F40", 40),
                    new AdventureCard("Foe", "F10", 10),
                    //Stage 2
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F30", 30),
                    //Stage 3
                    new AdventureCard("Foe", "F30", 30),
                    new AdventureCard("Foe", "F15", 15),
                    //Stage 4
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F20", 20),

                    //P1 draws 11 cards
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F30", 30),

                    //Draws
                    //Stage 1
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "D5", 5),
                    //Stage 2
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),
                    //Stage 3
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F25", 25),

                    //P2 draws 8 cards
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F30", 30),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "L20", 20)

            );
        }


        if("1winner_game_with_events".equals(scenario)){
            return Arrays.asList(
                    //P1
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "D5", 5),

                    //P2
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F30", 30),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "L20", 20),
                    new AdventureCard("Weapon", "L20", 20),
                    new AdventureCard("Weapon", "E30", 30),

                    //P3
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F30", 30),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "L20", 20),
                    new AdventureCard("Weapon", "L20", 20),
                    new AdventureCard("Weapon", "E30", 30),

                    //P4
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F30", 30),
                    new AdventureCard("Foe", "F70", 70),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "L20", 20),
                    new AdventureCard("Weapon", "L20", 20),

                    //Draws
                    //Stage 1
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F20", 20),
                    //Stage 2
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F25", 25),
                    //Stage 3
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F20", 20),
                    //Stage 4
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F20", 20),

                    //P1 draws 8 cards
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),

                    //Prosperity Draws
                    //P3
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Foe", "F40", 40),
                    //P4
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "D5", 5),
                    //P1
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F25", 25),
                    //P2
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "S10", 10),

                    //Queen's Favor
                    new AdventureCard("Foe", "F30", 30),
                    new AdventureCard("Foe", "F25", 25),

                    //Draws
                    //Stage 1
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Foe", "F50", 50),
                    //Stage 2
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    //Stage 3
                    new AdventureCard("Foe", "F40", 40),
                    new AdventureCard("Foe", "F50", 50),

                    //P1 draws 8 Cards
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Foe", "F35", 35)
            );
        }


        if("0_winner_quest".equals(scenario)){
            return Arrays.asList(
                    //P1
                    new AdventureCard("Foe", "F50", 50),
                    new AdventureCard("Foe", "F70", 70),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "B15", 15),
                    new AdventureCard("Weapon", "L20", 20),
                    new AdventureCard("Weapon", "L20", 20),

                    //P2
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F30", 30),
                    new AdventureCard("Foe", "F30", 30),
                    new AdventureCard("Foe", "F40", 40),
                    new AdventureCard("Weapon", "E30", 30),

                    //P3
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F30", 30),
                    new AdventureCard("Foe", "F40", 40),
                    new AdventureCard("Weapon", "L20", 20),

                    //P4
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F20", 20),
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F25", 25),
                    new AdventureCard("Foe", "F30", 30),
                    new AdventureCard("Foe", "F50", 50),
                    new AdventureCard("Weapon", "E30", 30),

                    //Draws
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Foe", "F10", 10),

                    // P1 Draws 14 cards
                    new AdventureCard("Foe", "F5", 5),
                    new AdventureCard("Foe", "F10", 10),
                    new AdventureCard("Foe", "F15", 15),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "D5", 5),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "H10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10),
                    new AdventureCard("Weapon", "S10", 10)


            );
        }

        return null;
    }

}
