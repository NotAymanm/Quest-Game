package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            assertEquals(12, player.getHandSize(), "P" + player.getId() + " should have 12 adventure cards.");
        }

    }

    @Test
    @DisplayName("Tests For Adventure Deck Update")
    void RESP_03_test_01(){

        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();
        game.distributeAdventureCards();

        List<AdventureCard> adventureDeck = game.getAdventureDeck();

        assertEquals(52, adventureDeck.size(), "The adventure deck should have 52 cards remaining after distribution.");

    }

    @Test
    @DisplayName("Tests Player Turn Order")
    void RESP_04_test_01(){

        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        assertEquals(1, game.getCurrentPlayer().getId(), "Initial player should be P1");
        game.nextPlayer();
        assertEquals(2, game.getCurrentPlayer().getId(), "Next player should be P2");
        game.nextPlayer();
        assertEquals(3, game.getCurrentPlayer().getId(), "Next player should be P3");
        game.nextPlayer();
        assertEquals(4, game.getCurrentPlayer().getId(), "Next player should be P4");
        game.nextPlayer();
        assertEquals(1, game.getCurrentPlayer().getId(), "Next player should be P1 after P4");

    }

    @Test
    @DisplayName("Tests No Winners Initially")
    void RESP_05_test_01(){

        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        List<Player> winners = game.checkForWinners();

        assertTrue(winners.isEmpty(), "There should be no winners at the start of the game");
    }

    @Test
    @DisplayName("Tests Single Winner")
    void RESP_05_test_02(){

        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        Player p1 = game.getPlayer(0);
        p1.addShields(7);

        List<Player> winners = game.checkForWinners();
        assertEquals(1, winners.size(), "There should be exactly 1 winner");
        assertEquals(1, winners.get(0).getId(), "P1 should be the winner");
    }

    @Test
    @DisplayName("Tests Multiple Winners")
    void RESP_05_test_03(){

        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        Player p1 = game.getPlayer(0);
        Player p2 = game.getPlayer(1);
        p1.addShields(7);
        p2.addShields(8);

        List<Player> winners = game.checkForWinners();
        assertEquals(2, winners.size(), "There should be exactly 2 winners");

        Set<Integer> winnersIds = new HashSet<>();
        for(Player player: winners){
            winnersIds.add(player.getId());
        }

        assertTrue(winnersIds.contains(1), "P1 should be a winner");
        assertTrue(winnersIds.contains(2), "P2 should be a winner");

    }

    @Test
    @DisplayName("Tests No Winners if Less Than 7 Shields")
    void RESP_05_test_04(){

        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        Player p1 = game.getPlayer(0);
        p1.addShields(6);

        List<Player> winners = game.checkForWinners();
        assertTrue(winners.isEmpty(), "There should be no winners if no one has 7 shields");

    }

}