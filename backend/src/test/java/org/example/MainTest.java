package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

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

    @Test
    @DisplayName("Tests Game Displays Winner and Terminates")
    void RESP_06_test_01(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();
        Player p1 = game.getPlayer(0);
        p1.addShields(7);

        StringWriter output = new StringWriter();

        game.displayWinners();

        assertTrue(output.toString().contains("Winner: P1"), "The winner ID P1 should be displayed");

        assertTrue(game.isGameOver(), "The game should be over after displaying the winners");
    }

    @Test
    @DisplayName("Tests Game Displays Multiple Winners and Terminates")
    void RESP_06_test_02(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();
        Player p1 = game.getPlayer(0);
        Player p2 = game.getPlayer(1);
        p1.addShields(7);
        p2.addShields(7);

        StringWriter output = new StringWriter();
        game.displayWinners();

        assertTrue(output.toString().contains("Winner: P1"), "The winner ID P1 should be displayed");
        assertTrue(output.toString().contains("Winner: P2"), "The winner ID P2 should be displayed");

        assertTrue(game.isGameOver(), "The game should be over after displaying the winners");
    }

    @Test
    @DisplayName("Tests Game Does not Terminate if No Winner")
    void RESP_06_test_03(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();
        StringWriter output = new StringWriter();
        game.displayWinners();

        assertFalse(game.isGameOver(), "The game should not be over if no player has won");
        assertEquals("", output.toString().trim(), "No output should be displayed if there are no winners");
    }

    @Test
    @DisplayName("Tests Drawing and Displaying Next Event Card")
    void RESP_07_test_01(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        StringWriter output = new StringWriter();

        game.drawNextEventCard();

        String eventCard = output.toString().trim();

        boolean displayedEventCard = eventCard.contains("Q")
                || eventCard.contains("Plague")
                || eventCard.contains("Queen's Favor")
                || eventCard.contains("Prosperity");

        assertTrue(displayedEventCard, "The drawn event card should be displayed");

        assertEquals(16, game.getEventDeck().size(), "Event deck size should decrease after drawing a card");
    }

    @Test
    @DisplayName("Tests Correct Identification of Quest Card")
    void RESP_08_test_01(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        EventCard currentEvent = new EventCard("Q3", "Quest");
        game.setCurrentEvent(currentEvent);

        assertTrue(game.isQuest(), "The drawn card should be a Quest card.");
    }

    @Test
    @DisplayName("Tests Correct Identification of Event Card")
    void RESP_08_test_02(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        EventCard currentEvent = new EventCard("Plague", "Event");
        game.setCurrentEvent(currentEvent);

        assertFalse(game.isQuest(), "The drawn card should be a Event card.");
    }

    @Test
    @DisplayName("Tests the effect of the Plague card")
    void RESP_09_test_01(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        EventCard currentEvent = new EventCard("Plague", "Event");
        game.setCurrentEvent(currentEvent);

        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.addShields(4);
        int initialShields = currentPlayer.getShields();
        StringWriter output = new StringWriter();
        game.processEvent();
        int expectedShields = Math.max(initialShields - 2, 0);
        assertEquals(expectedShields, currentPlayer.getShields(), "The player should lose 2 shields, but not go below 0.");
    }

    @Test
    @DisplayName("Tests that Plague card cannot reduce shields below 0")
    void RESP_09_test_02(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        EventCard currentEvent = new EventCard("Plague", "Event");
        game.setCurrentEvent(currentEvent);

        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.addShields(1);
        assertEquals(1, currentPlayer.getShields(), "The player should start with 1 shield.");
        StringWriter output = new StringWriter();
        game.processEvent();
        assertEquals(0, currentPlayer.getShields(), "The player's shields should not go below 0.");
    }

    @Test
    @DisplayName("Tests the effect of the Queen’s favor card")
    void RESP_10_test_01() {
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        Player currentPlayer = game.getCurrentPlayer();
        StringWriter output = new StringWriter();
        currentPlayer.takeAdventureCards(9, game.getAdventureDeck(), game.getAdventureDiscardPile());

        EventCard currentEvent = new EventCard("Queen's Favor", "Event");
        game.setCurrentEvent(currentEvent);

        assertEquals(9, currentPlayer.getHandSize(), "Player should start with 9 adventure cards");
        game.processEvent();
        assertEquals(11, currentPlayer.getHandSize(), "Player should have exactly 11 cards after drawing 2 adventure cards");
    }

    @Test
    @DisplayName("Tests the effect of the Prosperity card")
    void RESP_11_test_01(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        Player p1 = game.getPlayer(0);
        Player p2 = game.getPlayer(1);
        Player p3 = game.getPlayer(2);
        Player p4 = game.getPlayer(3);

        EventCard currentEvent = new EventCard("Prosperity", "Event");
        game.setCurrentEvent(currentEvent);

        StringWriter output = new StringWriter();
        p1.drawAdventureCards(10, game.getAdventureDeck(), game.getAdventureDiscardPile());
        p4.drawAdventureCards(9, game.getAdventureDeck(), game.getAdventureDiscardPile());

        assertEquals(10, p1.getHandSize(), "P1 should start with 10 adventure cards");
        assertEquals(9, p4.getHandSize(), "P4 should start with 9 adventure cards");

        game.processEvent();

        assertEquals(12, p1.getHandSize(), "P1 should have 12 cards after drawing 2 cards");
        assertEquals(11, p4.getHandSize(), "P4 should have 11 cards after drawing 2 cards");
    }

    @Test
    @DisplayName("Tests hotseat display is cleared")
    void RESP_12_test_01(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        assertEquals(1, game.getCurrentPlayer().getId(), "P1 should be in the hotseat at the start.");

        //check hotsteat is being displayed (p1 is in the hotsteat, p1's cards)
        StringWriter output = new StringWriter();
        assertTrue(output.toString().contains("Current Player in Hotseat: P1"), "The current hotseat should be displayed");

        //Check hotsteat display is cleared (15 new lines)
        assertTrue(output.toString().contains("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"), "The hosteat display should be cleared");
    }

    @Test
    @DisplayName("Tests current player's turn has ended")
    void RESP_12_test_02(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        String input = "\n";
        StringWriter output = new StringWriter();
        game.endTurn();

        assertTrue(output.toString().contains("P1's turn has concluded."), "P1's turn should have concluded.");
        assertEquals(2, game.getCurrentPlayer().getId(), "P2 should be in the hotseat after P1's turn ended");

    }

    @Test
    @DisplayName("Tests that the current player's turn is indicated and hand is displayed")
    void RESP_13_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.nextPlayer();
        assertEquals(2, game.getCurrentPlayer().getId(), "P2 should be in the hotseat.");

        Player currentPlayer = game.getCurrentPlayer();

        game.addAdventureCards("Weapon", "H10", 10, 2);
        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        game.addAdventureCards("Foe", "F5", 5, 1);

        StringWriter output = new StringWriter();
        currentPlayer.takeAdventureCards(5, game.getAdventureDeck(), game.getAdventureDiscardPile());

        game.startTurn();
        assertTrue(output.toString().contains("P2's turn has begun."), "P2's turn should be indicated.");

        boolean isDisplayed = output.toString().contains("[F5, F10, H10, H10, B15]");

        assertTrue(isDisplayed, "Current Player's cards should be displayed and in correct order");
    }

    @Test
    @DisplayName("Tests no Discard Needed at limit")
    void RESP_14_test_01(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        Player currentPlayer = game.getCurrentPlayer();
        StringWriter output = new StringWriter();
        currentPlayer.takeAdventureCards(12, game.getAdventureDeck(), game.getAdventureDiscardPile());
        int cardsToDiscard = currentPlayer.numCardsToDiscard();

        assertEquals(0, cardsToDiscard, "Player should not have to discard any cards");
    }

    @Test
    @DisplayName("Tests discard excess cards")
    void RESP_14_test_02(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        Player currentPlayer = game.getCurrentPlayer();
        StringWriter output = new StringWriter();
        currentPlayer.takeAdventureCards(15, game.getAdventureDeck(), game.getAdventureDiscardPile());
        int cardsToDiscard = currentPlayer.numCardsToDiscard();

        assertEquals(3, cardsToDiscard, "Player should discard 3 cards to have exactly 12");
    }

    @Test
    @DisplayName("Tests no discard under limit")
    void RESP_14_test_03(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        Player currentPlayer = game.getCurrentPlayer();
        StringWriter output = new StringWriter();
        currentPlayer.takeAdventureCards(10, game.getAdventureDeck(), game.getAdventureDiscardPile());
        int cardsToDiscard = currentPlayer.numCardsToDiscard();

        assertEquals(0, cardsToDiscard, "Player should not discard if under the limit");
    }

    @Test
    @DisplayName("Tests game displays player's cards and prompts player for the card(s) to delete")
    void RESP_15_test_01(){
        Main game = new Main();
        game.initPlayers();

        Player currentPlayer = game.getCurrentPlayer();

        game.addAdventureCards("Weapon", "H10", 10, 5);
        game.addAdventureCards("Weapon", "S10", 10, 5);
        game.addAdventureCards("Foe", "F15", 15, 3);
        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Foe", "F5", 5, 1);

        StringWriter output = new StringWriter();
        currentPlayer.takeAdventureCards(15, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "1";
        List<Integer> discard = Arrays.asList(1);
        currentPlayer.discardCard(discard, game.getAdventureDiscardPile());

        assertTrue(output.toString().contains("[F5, F10, F15, F15, F15, S10, S10, S10, S10, S10, H10, H10, H10, H10, H10]"), "Player's hand should be displayed");
        assertTrue(output.toString().contains("Which card you like to discard"), "Game should prompt players to discard a card");

    }

    @Test
    @DisplayName("Tests player enters invalid position")
    void RESP_16_test_01(){
        Main game = new Main();
        game.initPlayers();

        Player currentPlayer = game.getCurrentPlayer();

        game.addAdventureCards("Weapon", "H10", 10, 5);
        game.addAdventureCards("Weapon", "S10", 10, 5);
        game.addAdventureCards("Foe", "F15", 15, 3);
        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Foe", "F5", 5, 1);

        assertEquals(15, game.getAdventureDeck().size(), "Adventure Deck should have 15 cards");

        StringWriter output = new StringWriter();
        currentPlayer.takeAdventureCards(15, game.getAdventureDeck(), game.getAdventureDiscardPile());

        List<Integer> discard = Arrays.asList(15);
        currentPlayer.discardCard(discard, game.getAdventureDiscardPile());

        assertTrue(output.toString().contains("Please Enter a Valid Position!"), "Position should be invalid");

    }

    @Test
    @DisplayName("Tests player enters valid position")
    void RESP_16_test_02(){
        Main game = new Main();
        game.initPlayers();

        Player currentPlayer = game.getCurrentPlayer();

        game.addAdventureCards("Weapon", "H10", 10, 5);
        game.addAdventureCards("Weapon", "S10", 10, 5);
        game.addAdventureCards("Foe", "F15", 15, 3);
        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Foe", "F5", 5, 1);

        assertEquals(15, game.getAdventureDeck().size(), "Adventure Deck should have 15 cards");
        StringWriter output = new StringWriter();
        currentPlayer.takeAdventureCards(15, game.getAdventureDeck(), game.getAdventureDiscardPile());

        List<Integer> discard = Arrays.asList(14);
        currentPlayer.discardCard(discard, game.getAdventureDiscardPile());

        assertTrue(output.toString().contains("H10 has been removed."), "Position should be valid");

        assertEquals(14, currentPlayer.getHandSize(), "Player should have 14 cards after discarding 1 card");

    }

    @Test
    @DisplayName("Tests game displays updated hand after discard")
    void RESP_17_test_01(){
        Main game = new Main();
        game.initPlayers();

        Player currentPlayer = game.getCurrentPlayer();

        game.addAdventureCards("Weapon", "H10", 10, 5);
        game.addAdventureCards("Weapon", "S10", 10, 5);
        game.addAdventureCards("Foe", "F15", 15, 3);
        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Foe", "F5", 5, 1);

        assertEquals(15, game.getAdventureDeck().size(), "Adventure Deck should have 15 cards");

        StringWriter output = new StringWriter();
        currentPlayer.takeAdventureCards(15, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "14";
        List<Integer> discard = Arrays.asList(14);
        currentPlayer.discardCard(discard, game.getAdventureDiscardPile());

        assertTrue(output.toString().contains("[F5, F10, F15, F15, F15, S10, S10, S10, S10, S10, H10, H10, H10, H10]"), "Should display updated hand after discard.");

    }

    @Test
    @DisplayName("Tests game prompting current player to decide to sponsor quest")
    void RESP_18_test_01(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q4", "Quest"));

        Player currentPlayer = game.getCurrentPlayer();

        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Foe", "F20", 20, 1);
        game.addAdventureCards("Foe", "F30", 30, 1);
        game.addAdventureCards("Foe", "F40", 40, 1);

        StringWriter output = new StringWriter();
        currentPlayer.drawAdventureCards(4, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "y";
//        game.findSponsor(new Scanner(input), new PrintWriter(output));

        assertTrue(output.toString().contains("do you want to sponsor this quest"), "Game should prompt player to decide to sponsor or not");

        assertTrue(game.isQuestSponsored(), "Quest should be sponsored when player selects 'y'");
    }

    @Test
    @DisplayName("Tests game handles all player declining sponsor")
    void RESP_19_test_01(){
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q4", "Quest"));

        String input = "\nn\nn\nn\nn";
        StringWriter output = new StringWriter();

//        game.findSponsor(new Scanner(input), new PrintWriter(output));

        assertTrue(output.toString().contains("All players have declined to sponsor the quest."),
                "The game should declare that all players have declined the quest");
        assertTrue(output.toString().contains("The quest has been discarded."),
                "The quest should be discarded when no players sponsor it");

        assertFalse(game.isQuestSponsored(), "The quest should not be sponsored when all players decline");
        assertNull(game.getCurrentEvent(), "The quest card should be discarded after all players decline to sponsor");
    }

    @Test
    @DisplayName("Tests game asks current player to leave the hotseat")
    void RESP_20_test_01(){
        Main game = new Main();
        game.initPlayers();

        Player currentPlayer = game.getCurrentPlayer();

        String input = "\n";
        StringWriter output = new StringWriter();

        game.endTurn();

        assertTrue(output.toString().contains(currentPlayer.toString() + ", Please leave the hotseat by hitting the <return> key"),
                    "The game should prompt the current player to leave the hot seat"
                );
    }

    @Test
    @DisplayName("Tests Player Can Sponsor Quest")
    void RESP_21_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q3", "Quest"));

        Player currentPlayer = game.getCurrentPlayer();

        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Foe", "F20", 20, 1);
        game.addAdventureCards("Foe", "F30", 30, 1);
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Weapon", "S10", 10, 1);

        currentPlayer.drawAdventureCards(5, game.getAdventureDeck(), game.getAdventureDiscardPile());

        boolean canSponsor = currentPlayer.canSponsor(game.getCurrentEvent());

        assertTrue(canSponsor, "Player should be able to sponsor a valid 3 stage quest");
    }

    @Test
    @DisplayName("Tests player cant sponsor quest with insufficient cards")
    void RESP_21_test_02(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q3", "Quest"));

        Player currentPlayer = game.getCurrentPlayer();

        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Foe", "F20", 20, 1);

        currentPlayer.drawAdventureCards(2, game.getAdventureDeck(), game.getAdventureDiscardPile());

        boolean canSponsor = currentPlayer.canSponsor(game.getCurrentEvent());

        assertFalse(canSponsor, "Player should NOT be able to sponsor a quest with insufficient cards");
    }

    @Test
    @DisplayName("Tests player cant sponsor quest with invalid stage order")
    void RESP_21_test_03(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q3", "Quest"));

        Player currentPlayer = game.getCurrentPlayer();

        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Foe", "F20", 20, 1);

        currentPlayer.drawAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());

        boolean canSponsor = currentPlayer.canSponsor(game.getCurrentEvent());

        assertFalse(canSponsor, "Player should NOT be able to sponsor a quest with decreasing/same stage values");
    }

    @Test
    @DisplayName("Test game displays sponsor's and and prompts next card or 'Quit'")
    void RESP_22_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        sponsor.setAsSponsor();

        game.addAdventureCards("Weapon", "H10", 10, 2);
        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        game.addAdventureCards("Foe", "F5", 5, 1);

        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(5, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "0\nQuit\n0\nQuit";

//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        assertTrue(output.toString().contains("Sponsor's hand (P1): [F5, F10, H10, H10, B15]"),
                "Sponsor's hand should be displayed correctly");

        assertTrue(output.toString().contains("Which card would you like to use for Stage 1? (Enter Index), or type 'Quit' to stop:"),
                "The game should prompt the sponsor to select a card or quit");

        assertTrue(output.toString().contains("You selected: F5"), "The game should confirm the selected card");
        assertTrue(output.toString().contains("Stage 1 building complete."), "The game should confirm when sponsor quits");

    }

    @Test
    @DisplayName("Tests sponsor enters a necessarily valid position for Foe")
    void RESP_23_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        sponsor.setAsSponsor();

        game.addAdventureCards("Weapon", "H10", 10, 2);
        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        game.addAdventureCards("Foe", "F5", 5, 1);

        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(5, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "0\n0\nQuit\n0\nQuit";

//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        assertTrue(output.toString().contains("Sorry, you can't have more than ONE foe per stage"),
                "The game should display an explanation of invalid input (No duplicate foes)");
    }

    @Test
    @DisplayName("Tests sponsor enters a necessarily valid position for repeated Weapon")
    void RESP_23_test_02(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        sponsor.setAsSponsor();

        game.addAdventureCards("Weapon", "H10", 10, 2);
        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        game.addAdventureCards("Foe", "F5", 5, 1);

        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(5, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "2\n2\n0\nQuit\n0\n0\nQuit";

//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        assertTrue(output.toString().contains("Sorry, you can't have repeated weapon cards per stage"),
                "The game should display an explanation of invalid input (No duplicate Weapon cards)");
    }

    @Test
    @DisplayName("Tests stage has no Foe")
    void RESP_23_test_03(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        sponsor.setAsSponsor();

        game.addAdventureCards("Weapon", "H10", 10, 2);
        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        game.addAdventureCards("Foe", "F5", 5, 1);

        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(5, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "2\nQuit\n0\nQuit\n0\n0\nQuit";

//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        assertTrue(output.toString().contains("Need a foe to complete stage"),
                "The game should display an explanation of invalid stage (Need a foe card)");
    }

    @Test
    @DisplayName("Tests game handles 'Quit' when stage is empty and displays 'A stage cannot be empty'")
    void RESP_24_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        sponsor.setAsSponsor();

        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Foe", "F5", 5, 1);

        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(2, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "Quit\n0\nQuit\n0\nQuit";

//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        assertTrue(output.toString().contains("A stage cannot be empty. You must add at least one card."),
                "The game should display 'A stage cannot be empty' when trying to quit with an empty stage");
    }

    @Test
    @DisplayName("Tests game handles 'Quit' when stage value is insufficient compared to the previous stage")
    void RESP_25_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        sponsor.setAsSponsor();

        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F5", 5, 2);

        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(4, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "0\nQuit\n0\nQuit\n1\nQuit";

//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        assertTrue(output.toString().contains("Insufficient value for this stage. The value must be greater than the previous stage."),
                "The game should display 'Insufficient value for this stage' when trying to quit with insufficient stage value");

    }

    @Test
    @DisplayName("Tests game displays cards after valid stage")
    void RESP_26_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q3", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        sponsor.setAsSponsor();

        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Weapon", "D5", 5, 2);
        game.addAdventureCards("Foe", "F5", 5, 2);

        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(5, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "0\nQuit\n0\n1\nQuit\n0\n0\nQuit";

//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        assertTrue(output.toString().contains("Stage is valid. Cards used in this stage: [F10, D5]"),
                "The game should display that the stage is valid and list the cards used");
    }

    @Test
    @DisplayName("Tests participant eligibility displayed")
    void RESP_27_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 2);

        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "y";

//        game.findSponsor(new Scanner(input), new PrintWriter(output));
        input = "0\nQuit\n0\n0\nQuit";
//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        Player p2 = game.getPlayer(1);
        game.addAdventureCards("Weapon", "D5", 5, 1);
        p2.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());
        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        p3.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());
        Player p4 = game.getPlayer(3);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        p4.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());

        game.determineEligibleParticipants(0);

        assertTrue(output.toString().contains("Eligible participants for Stage 1: [P3, P4]"),
                "The eligible participants should be listed as P3 and P4");
    }

    @Test
    @DisplayName("Tests game prompt eligible participants to either withdraw or tackle stage")
    void RESP_28_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 2);
        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "y";

//        game.findSponsor(new Scanner(input), new PrintWriter(output));
        input = "0\nQuit\n0\n0\nQuit";
//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        p3.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());
        Player p4 = game.getPlayer(3);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        p4.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());

        List<Player> eligibleParticipants = game.determineEligibleParticipants( 0);

        input = "1\n2";
//        List<Player> participants = game.promptParticipantsContinue(eligibleParticipants, new Scanner(input), new PrintWriter(output));

//        assertEquals(1, participants.size(), "One participant should continue");
//        assertTrue(participants.contains(p3), "P3 should continue");
//        assertFalse(participants.contains(p4), "P4 should withdraw");

        assertTrue(output.toString().contains("P3, do you want to (1) Tackle or (2) Withdraw the stage?"),
                "P3 should be prompted");

        assertTrue(output.toString().contains("P4, do you want to (1) Tackle or (2) Withdraw the stage?"),
                "P4 should be prompted");

        assertTrue(output.toString().contains("P3 will tackle the stage."));
        assertTrue(output.toString().contains("P4 has withdrawn from the quest."));

    }

    @Test
    @DisplayName("Tests participant draws 1 adventure card when choose to tackle stage")
    void RESP_29_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 2);
        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "y";

//        game.findSponsor(new Scanner(input), new PrintWriter(output));
        input = "0\nQuit\n0\n0\nQuit";
//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        p3.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());
        Player p4 = game.getPlayer(3);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        p4.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());

        assertEquals(1, p3.getHandSize(), "P3 should have 1 card");

        game.addAdventureCards("Foe", "F15", 15, 1);

        input = "1\n2";
//        game.getParticipants(game.getPlayers(), new Scanner(input), new PrintWriter(output), 0);

        assertEquals(2, p3.getHandSize(), "P3 should have drawn 1 adventure card");
    }

    @Test
    @DisplayName("Tests quest ends if no participants for stage")
    void RESP_30_test_01(){
        Main game = new Main();
        game.initPlayers();
        game.setCurrentEvent(new EventCard("Q2", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 2);
        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());

        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        p3.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());
        Player p4 = game.getPlayer(3);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        p4.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());

        assertEquals(1, p3.getHandSize(), "P3 should have 1 card");

        game.addAdventureCards("Foe", "F15", 15, 1);
        game.addAdventureCards("Weapon", "S10", 10, 6);

        String input = "y\n0\nQuit\n0\n0\nQuit\n2\n2\n\n";

//        game.processQuest(new Scanner(input), new PrintWriter(output));

        assertNull(game.getCurrentEvent(), "Quest should end as there are no participants left");

        assertTrue(output.toString().contains("No participants remain. The quest has ended."),
                "Quest should end as there are no participants left");
    }

    @Test
    @DisplayName("Tests quest continues if there are participants for stage")
    void RESP_30_test_02(){
        Main game = new Main();
        game.initPlayers();
        game.setCurrentEvent(new EventCard("Q2", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 2);
        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());

        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        p3.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());
        Player p4 = game.getPlayer(3);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        p4.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());

        assertEquals(1, p3.getHandSize(), "P3 should have 1 card");

        game.addAdventureCards("Foe", "F15", 15, 2);

        String input = "y\n0\nQuit\n0\n0\nQuit\n1\n1\n1\nQuit\n1\nQuit\n\n";

//        game.processQuest(new Scanner(input), new PrintWriter(output));
        assertTrue(output.toString().contains("Stage 1 begins!"), "Quest should NOT end as there are participants left");
    }

    @Test
    @DisplayName("Test game displays player's hand and prompts for card input or 'Quit'")
    void RESP_31_test_01(){
        Main game = new Main();
        game.initPlayers();
        game.setCurrentEvent(new EventCard("Q2", "Quest"));

        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 2);
        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());
        sponsor.setAsSponsor();

        String input = "y\n0\nQuit\n0\n0\nQuit";
//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "B15", 15, 1);
        p3.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());

        input = "0\nQuit";
        List<Integer> attack = Arrays.asList(0);
        p3.buildAttack(attack);

        assertTrue(output.toString().contains("P3's hand: [B15]"),
                "Should display participant's hand");

        assertTrue(output.toString().contains("Which card would you like to include in your attack? (Enter Index), or 'Quit' to stop:"),
                "The game should prompt for a card position or 'Quit'.");
    }

    @Test
    @DisplayName("Tests participant enters a necessarily valid position for repeated Weapon")
    void RESP_32_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));
        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 2);
        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());
        sponsor.setAsSponsor();

        String input = "y\n0\nQuit\n0\n0\nQuit";
//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "B15", 15, 2);
        p3.takeAdventureCards(2, game.getAdventureDeck(), game.getAdventureDiscardPile());

        input = "0\n0\nQuit";
        List<Integer> attack = Arrays.asList(0, 0);
        p3.buildAttack(attack);

        assertTrue(output.toString().contains("Sorry, you can't have repeated weapon cards in an attack."),
                "The game should display an explanation of invalid input (No duplicate Weapon cards)");
    }

    @Test
    @DisplayName("Tests valid card included in attack and the updated attack is displayed")
    void RESP_33_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));
        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 2);
        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());
        sponsor.setAsSponsor();

        String input = "y\n0\nQuit\n0\n0\nQuit";
//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "B15", 15, 2);
        p3.takeAdventureCards(2, game.getAdventureDeck(), game.getAdventureDiscardPile());

        input = "0\nQuit";
        List<Integer> attack = Arrays.asList(0, 0);
        p3.buildAttack(attack);

        assertTrue(output.toString().contains("P3's current attack: [B15]"),
                "The current attack should display be updated and displayed.");
    }

    @Test
    @DisplayName("Tests player's attack succeeded")
    void RESP_34_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));
        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 2);
        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "y";
//        game.findSponsor(new Scanner(input), new PrintWriter(output));

        input = "0\nQuit\n0\n0\nQuit";
//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "B15", 15, 2);
        p3.takeAdventureCards(2, game.getAdventureDeck(), game.getAdventureDiscardPile());


        List<Player> participants = new ArrayList<>();
        participants.add(p3);

        input = "0\nQuit\n0\nQuit";
//        game.resolveAttacks(participants, new Scanner(input), new PrintWriter(output), 0);

        assertTrue(output.toString().contains("P3's Attack was successful!"),
                "P3's attack should have been successful.");
    }

    @Test
    @DisplayName("Tests player's attack failed")
    void RESP_34_test_02(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));
        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 2);
        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "y";
//        game.findSponsor(new Scanner(input), new PrintWriter(output));

        input = "0\nQuit\n0\n0\nQuit";
//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "D5", 5, 2);
        p3.takeAdventureCards(2, game.getAdventureDeck(), game.getAdventureDiscardPile());

        List<Player> participants = new ArrayList<>();
        participants.add(p3);

        input = "0\nQuit";
//        game.resolveAttacks(participants, new Scanner(input), new PrintWriter(output), 0);

        assertTrue(output.toString().contains("P3's Attack failed!"),
                "P3's attack should have been Unsuccessful.");
    }

    @Test
    @DisplayName("Tests game discards cards used for attack")
    void RESP_35_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));
        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 2);
        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());

        String input = "y";
//        game.findSponsor(new Scanner(input), new PrintWriter(output));

        input = "0\nQuit\n0\n0\nQuit";
//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "D5", 5, 2);
        p3.takeAdventureCards(2, game.getAdventureDeck(), game.getAdventureDiscardPile());

        List<Player> participants = new ArrayList<>();
        participants.add(p3);

        input = "0\nQuit";
//        game.resolveAttacks(participants, new Scanner(input), new PrintWriter(output), 0);

        assertFalse(game.getAdventureDiscardPile().isEmpty(), "Discard Pile should not be empty");

        assertEquals("D5", game.getAdventureDiscardPile().get(game.getAdventureDiscardPile().size()-1).getName(),
                "P3's attack should have been Unsuccessful.");
    }

    @Test
    @DisplayName("Tests shields given to winners")
    void RESP_36_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));
        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 2);
        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());

        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "E30", 30, 1);
        p3.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());

        game.addAdventureCards("Weapon", "S10", 10, 6);
        game.addAdventureCards("Weapon", "E30", 30, 1);

        assertEquals(0, p3.getShields(), "P3 should have 0 shields initially.");

        String input = "y\n0\nquit\n0\n0\nquit\n1\n0\nquit\n1\n1\nquit\n\n";
//        game.processQuest(new Scanner(input), new PrintWriter(output));

        assertEquals(2, p3.getShields(), "Should give P3 2 shields after beating the quest.");
    }

    @Test
    @DisplayName("Tests game discards sponsor's cards")
    void RESP_37_test_01(){
        Main game = new Main();
        game.initPlayers();

        game.setCurrentEvent(new EventCard("Q2", "Quest"));
        Player sponsor = game.getCurrentPlayer();
        game.addAdventureCards("Weapon", "D5", 5, 1);
        game.addAdventureCards("Foe", "F10", 10, 1);
        game.addAdventureCards("Foe", "F15", 15, 1);
        StringWriter output = new StringWriter();
        sponsor.takeAdventureCards(3, game.getAdventureDeck(), game.getAdventureDiscardPile());

        Player p3 = game.getPlayer(2);
        game.addAdventureCards("Weapon", "E30", 30, 1);
        p3.takeAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile());

        game.addAdventureCards("Weapon", "E30", 30, 1);

        game.addAdventureCards("Weapon", "S10", 10, 6);

        assertEquals(0, p3.getShields(), "P3 should have 0 shields initially.");

        String input = "y\n0\nquit\n0\n0\nquit\n1\n0\nquit\n1\n0\nquit\n\n";

//        game.processQuest(new Scanner(input), new PrintWriter(output));

        assertFalse(game.getAdventureDiscardPile().isEmpty(), "Discard Pile should not be empty");

        boolean discarded = game.getAdventureDiscardPile().get(game.getAdventureDiscardPile().size()-1).getName().equals("D5");
        discarded = discarded && game.getAdventureDiscardPile().get(game.getAdventureDiscardPile().size()-2).getName().equals("F15");
        discarded = discarded && game.getAdventureDiscardPile().get(game.getAdventureDiscardPile().size()-3).getName().equals("F10");

        assertTrue(discarded, "Sponsor Cards should be discarded");

        assertEquals(5, game.getPlayer(0).getHandSize(), "Sponsor should have 5 cards");

    }

    @Test
    @DisplayName("Compulsory scenario")
    void A_TEST_JP_Scenario(){
        Main game = new Main();

        String input = "";
        StringWriter output = new StringWriter();

        //Decks are created
        game.setUpDecks();
        game.initPlayers();
        //hands of the 4 players are set up with random cards
        game.distributeAdventureCards();

        Player p1 = game.getPlayer(0);
        Player p2 = game.getPlayer(1);
        Player p3 = game.getPlayer(2);
        Player p4 = game.getPlayer(3);

        //Rig cards
        List<String> p1Cards = Arrays.asList("F5", "F5", "F15", "F15", "D5", "S10", "S10", "H10", "H10", "B15", "B15", "L20");
        rigAdventureCards(p1, game.getAdventureDeck(), p1Cards);
        List<String> p2Cards = Arrays.asList("F5", "F5", "F15", "F15", "F40", "D5", "S10", "H10", "H10", "B15", "B15", "E30");
        rigAdventureCards(p2, game.getAdventureDeck(), p2Cards);
        List<String> p3Cards = Arrays.asList("F5", "F5", "F5", "F15", "D5", "S10", "S10", "S10", "H10", "H10", "B15", "L20");
        rigAdventureCards(p3, game.getAdventureDeck(), p3Cards);
        List<String> p4Cards = Arrays.asList("F5", "F15", "F15", "F40", "D5", "D5", "S10", "H10", "H10", "B15", "L20", "E30");
        rigAdventureCards(p4, game.getAdventureDeck(), p4Cards);

        //Rigs event
        EventCard riggedEventCard = rigEventCard(game.getEventDeck(), game.getEventDiscardPile(), "Q4");
        game.setCurrentEvent(riggedEventCard);

        //P1 declines
        input = "n\n";
        //P2 accepts
        input += "y\n";

//        Player sponsor = game.findSponsor(new Scanner(input), new PrintWriter(output));

        //P2 builds Stage 1
        input = "0\n6\nQuit\n";
        //P2 builds Stage 2
        input += "1\n4\nQuit\n";
        //P2 builds Stage 3
        input += "1\n2\n3\nQuit\n";
        //P2 builds Stage 4
        input += "1\n2\nQuit\n";

//        sponsor.sponsorCard(new Scanner(input), new PrintWriter(output), game.getCurrentEvent());

//        List<Player> eligibleParticipants = game.determineEligibleParticipants(new PrintWriter(output), 0, game.getPlayers());

        //P1 decides to participate in Stage 1
        input = "1\n";
        //P3 decides to participate in Stage 1
        input += "1\n";
        //P4 decides to participate in Stage 1
        input += "1\n";

//        List<Player> participants = game.promptParticipantsContinue(eligibleParticipants, new Scanner(input), new PrintWriter(output));

        //P1 discards an F5
        input = "0\n";
        //P3 discards an F5
        input += "0\n";
        //P4 discards an F5
        input += "0\n";

        List<AdventureCard> riggedDraw  = new ArrayList<>();
        riggedDraw.add(getSpecificAdventureCard(game.getAdventureDeck(), "F30"));
        p1.drawAdventureCards(1, riggedDraw, game.getAdventureDiscardPile());
        riggedDraw.add(getSpecificAdventureCard(game.getAdventureDeck(), "S10"));
        p3.drawAdventureCards(1, riggedDraw, game.getAdventureDiscardPile());
        riggedDraw.add(getSpecificAdventureCard(game.getAdventureDeck(), "B15"));
        p4.drawAdventureCards(1, riggedDraw, game.getAdventureDiscardPile());

        //P1 builds their attack
        input = "4\n4\nQuit\n";
        //P3 builds their attack
        input += "4\n3\nQuit\n";
        //P4 builds their attack
        input += "3\n5\nQuit\n";

//        List<Player> stage1Winners = game.resolveAttacks(participants, new Scanner(input), new PrintWriter(output), 0);
//        List<Player> eligibleStage2 = game.determineEligibleParticipants(new PrintWriter(output), 1, stage1Winners);

        //P1 decides to participate in Stage 2
        input = "1\n";
        //P3 decides to participate in Stage 2
        input += "1\n";
        //P4 decides to participate in Stage 2
        input += "1\n";

//        List<Player> participantsStage2 = game.promptParticipantsContinue(eligibleStage2, new Scanner(input), new PrintWriter(output));

        riggedDraw.add(getSpecificAdventureCard(game.getAdventureDeck(), "F10"));
        p1.drawAdventureCards(1, riggedDraw, game.getAdventureDiscardPile());
        riggedDraw.add(getSpecificAdventureCard(game.getAdventureDeck(), "L20"));
        p3.drawAdventureCards(1, riggedDraw, game.getAdventureDiscardPile());
        riggedDraw.add(getSpecificAdventureCard(game.getAdventureDeck(), "L20"));
        p4.drawAdventureCards(1, riggedDraw, game.getAdventureDiscardPile());

        //P1 builds their attack
        input = "6\n5\nQuit\n";
        //P3 builds their attack
        input += "8\n3\nQuit\n";
        //P4 builds their attack
        input += "5\n5\nQuit\n";

//        List<Player> stage2Winners = game.resolveAttacks(participantsStage2, new Scanner(input), new PrintWriter(output), 1);

        List<String> p1CorrectHand = Arrays.asList("F5", "F10", "F15", "F15", "F30", "H10", "B15", "B15", "L20");
        List<String> p1Hand = p1.getHand().stream().map(AdventureCard::toString).collect(Collectors.toList());
        assertEquals(p1CorrectHand, p1Hand, "P1's hand after losing stage 2 is incorrect.");
        assertEquals(0, p1.getShields(), "P1 should have no shields after losing Stage 2.");

//        List<Player> eligibleStage3 = game.determineEligibleParticipants(new PrintWriter(output), 2, stage2Winners);

        //P3 decides to participate in Stage 3
        input = "1\n";
        //P4 decides to participate in Stage 3
        input += "1\n";

//        List<Player> participantsStage3 = game.promptParticipantsContinue(eligibleStage3, new Scanner(input), new PrintWriter(output));

        riggedDraw.add(getSpecificAdventureCard(game.getAdventureDeck(), "B15"));
        p3.drawAdventureCards(1, riggedDraw, game.getAdventureDiscardPile());
        riggedDraw.add(getSpecificAdventureCard(game.getAdventureDeck(), "S10"));
        p4.drawAdventureCards(1, riggedDraw, game.getAdventureDiscardPile());

        //P3 builds their attack
        input = "8\n5\n3\nQuit\n";
        //P4 builds their attack
        input += "6\n4\n5\nQuit\n";

//        List<Player> stage3Winners = game.resolveAttacks(participantsStage3, new Scanner(input), new PrintWriter(output), 2);
//        List<Player> eligibleStage4 = game.determineEligibleParticipants(new PrintWriter(output), 3, stage3Winners);

        //P3 decides to participate in Stage 4
        input = "1\n";
        //P4 decides to participate in Stage 4
        input += "1\n";

//        List<Player> participantsStage4 = game.promptParticipantsContinue(eligibleStage4, new Scanner(input), new PrintWriter(output));

        riggedDraw.add(getSpecificAdventureCard(game.getAdventureDeck(), "F30"));
        p3.drawAdventureCards(1, riggedDraw, game.getAdventureDiscardPile());
        riggedDraw.add(getSpecificAdventureCard(game.getAdventureDeck(), "L20"));
        p4.drawAdventureCards(1, riggedDraw, game.getAdventureDiscardPile());

        //P3 builds their attack
        input = "6\n5\n5\nQuit\n";
        //P4 builds their attack
        input += "3\n3\n3\n4\nQuit\n";

//        List<Player> stage4Winners = game.resolveAttacks(participantsStage4, new Scanner(input), new PrintWriter(output), 3);

        int numStages = Character.getNumericValue(game.getCurrentEvent().getName().charAt(1));
//        game.payWinners(stage4Winners, numStages);

        List<String> p3CorrectHand = Arrays.asList("F5", "F5", "F15", "F30", "S10");
        List<String> p3Hand = p3.getHand().stream().map(AdventureCard::toString).collect(Collectors.toList());
        assertEquals(p3CorrectHand, p3Hand, "P3's hand after losing stage 4 is incorrect.");
        assertEquals(0, p3.getShields(), "P3 should have no shields after losing Stage 4.");

        List<String> p4CorrectHand = Arrays.asList("F15", "F15", "F40", "L20");
        List<String> p4Hand = p4.getHand().stream().map(AdventureCard::toString).collect(Collectors.toList());
        assertEquals(p4CorrectHand, p4Hand, "P4's hand after winning stage 4 is incorrect.");
        assertEquals(4, p4.getShields(), "P4 should have 4 shields after winning Stage 4.");


        game.setGameOver();
        input = "0\n0\n0\n0\n\n";
        game.endQuest();

        assertEquals(12, p2.getHandSize(), "P2 should have 12 cards in hand");
    }

    void rigAdventureCards(Player player, List<AdventureCard> adventureDeck, List<String> riggedCards){
        //Puts the cards back into the deck
        List<AdventureCard> hand = player.getHand();
        adventureDeck.addAll(hand);
        hand.clear();

        //rigs the cards
        for(String card : riggedCards){
            AdventureCard riggedCardFound = getSpecificAdventureCard(adventureDeck, card);
            if(riggedCardFound != null){
                player.getHand().add(riggedCardFound);
            }
        }
    }

    AdventureCard getSpecificAdventureCard(List<AdventureCard> adventureDeck, String cardName) {
        for (AdventureCard card : adventureDeck) {
            if (card.getName().equals(cardName)) {
                adventureDeck.remove(card);
                return card;
            }
        }
        return null;

    }

    EventCard rigEventCard(List<EventCard> eventDeck, List<EventCard> eventDiscardPile, String cardName){
        for (EventCard card : eventDeck) {
            if (card.getName().equals(cardName)) {
                eventDeck.remove(card);
                eventDiscardPile.add(card);
                return card;
            }
        }
        return null;
    }

}
