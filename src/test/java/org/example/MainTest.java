package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class MainTest {

    //counts number of cards in a list
    int countCards(List<?> deck, String cardName) {
        int count = 0;
        for (Object card : deck) {
            if (cardName.equals(card.toString())) {
                count++;
            }
        }
        return count;
    }

    @Test
    @DisplayName("Tests Adventure Deck Setup")
    void RESP_01_test_01(){

        Main game = new Main();
        game.setUpDecks();

        List<AdventureCard> adventureDeck = game.getAdventureDeck();

        assertEquals(100, adventureDeck.size(), "Adventure deck should contain 100 cards");

        //Foe cards count
        assertEquals(8, countCards(adventureDeck, "F5"), "There should be 8 F5 foe cards");
        assertEquals(7, countCards(adventureDeck, "F10"), "There should be 7 F10 foe cards");
        assertEquals(8, countCards(adventureDeck, "F15"), "There should be 8 F15 foe cards");
        assertEquals(7, countCards(adventureDeck, "F20"), "There should be 7 F20 foe cards");
        assertEquals(7, countCards(adventureDeck, "F25"), "There should be 7 F25 foe cards");
        assertEquals(4, countCards(adventureDeck, "F30"), "There should be 4 F30 foe cards");
        assertEquals(4, countCards(adventureDeck, "F35"), "There should be 4 F35 foe cards");
        assertEquals(2, countCards(adventureDeck, "F40"), "There should be 2 F40 foe cards");
        assertEquals(2, countCards(adventureDeck, "F50"), "There should be 2 F50 foe cards");
        assertEquals(1, countCards(adventureDeck, "F70"), "There should be 1 F70 foe card");

        //Weapon cards count
        assertEquals(6, countCards(adventureDeck, "D5"), "There should be 6 dagger weapon cards");
        assertEquals(12, countCards(adventureDeck, "H10"), "There should be 12 horse weapon cards");
        assertEquals(16, countCards(adventureDeck, "S10"), "There should be 16 sword weapon cards");
        assertEquals(8, countCards(adventureDeck, "B15"), "There should be 8 battle-axe weapon cards");
        assertEquals(6, countCards(adventureDeck, "L20"), "There should be 6 lance weapon cards");
        assertEquals(2, countCards(adventureDeck, "E30"), "There should be 2 excalibur weapon cards");

    }

    @Test
    @DisplayName("Tests Event Deck Setup")
    void RESP_01_test_02(){

        Main game = new Main();
        game.setUpDecks();

        List<EventCard> eventDeck = game.getEventDeck();

        assertEquals(17, eventDeck.size(), "Event deck should contain 17 cards");

        //Quest cards count
        assertEquals(3, countCards(eventDeck, "Q2"), "There should be 3 Q2 quest cards");
        assertEquals(4, countCards(eventDeck, "Q3"), "There should be 4 Q3 quest cards");
        assertEquals(3, countCards(eventDeck, "Q4"), "There should be 3 Q4 quest cards");
        assertEquals(2, countCards(eventDeck, "Q5"), "There should be 2 Q5 quest cards");

        //Event cards count
        assertEquals(1, countCards(eventDeck, "Plague"), "There should be 1 Plague event card");
        assertEquals(2, countCards(eventDeck, "Queen's Favor"), "There should be 2 Queen's Favor event cards");
        assertEquals(2, countCards(eventDeck, "Prosperity"), "There should be 2 Prosperity event cards");

    }

    @Test
    @DisplayName("Tests Player Initialization")
    void RESP_02_test_01(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        List<Player> players = game.getPlayers();

        assertEquals(4, players.size(), "There should be 4 Players");

    }

    @Test
    @DisplayName("Tests Distribution of Adventure Cards")
    void RESP_02_test_02(){

        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();
        game.distributeAdventureCards();

        List<Player> players = game.getPlayers();

        assertEquals(4, players.size(), "Players not initialized");

        for(Player player: players){
            assertEquals(12, player.getHandSize(), "Player " + player.getId() + " should have 12 adventure cards.");
        }

    }

    @Test
    @DisplayName("Tests for adventure deck update")
    void RESP_03_test_01(){

        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();
        game.distributeAdventureCards();

        List<AdventureCard> adventureDeck = game.getAdventureDeck();

        // Additional check: Ensure deck has been reduced by 48 cards
        assertEquals(52, adventureDeck.size(), "The adventure deck should have 52 cards remaining after distribution.");

    }

}