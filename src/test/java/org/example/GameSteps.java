package org.example;

import io.cucumber.java.en.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class GameSteps {

    private final Main game = new Main();

    // Store data for players participating in each stage (so it's consistent between operations)
    private final Map<Integer, List<Player>> stageEligibleParticipants = new HashMap<>();
    private final List<Player> playersConnect = new ArrayList<>();
    StringBuilder input = new StringBuilder();
    StringWriter output = new StringWriter();


    /* HELPER FUNCTIONS! */
    List<List<String>> StringTo2DList(String input){
        // Remove the outer brackets
        input = input.substring(1, input.length() - 1);

        // Split into subLists
        String[] subLists = input.split("], \\[");

        // Create a 2D list
        List<List<String>> result = new ArrayList<>();

        for (String sublist : subLists) {
            // Remove any remaining brackets and split the sublist by commas
            List<String> elements = Arrays.asList(sublist.replace("[", "").replace("]", "").split(",\\s*"));
            result.add(elements);
        }

        // Print the 2D list to verify
        //System.out.println(result);

        return result;
    }
    int findCardIndexByName(List<AdventureCard> cards, String name){
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getName().equals(name)) {
                cards.remove(i);
                return i;
            }
        }
        return -1; // Return -1 if not found
    }


    @Given("the game starts with decks created and 4 players initialized")
    public void setupGameWithDecksAndPlayers() {
        game.setUpDecks();

        List<EventCard> eventCards = Arrays.asList(
                new EventCard("Q4", "Quest")
        );

        List<AdventureCard> adventureCards = Arrays.asList(
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
                new AdventureCard("Weapon", "L20", 20)
        );

        game.getDeck().rigAdventureDeck(adventureCards);
        game.getDeck().rigEventDeck(eventCards);

        game.initPlayers();
        game.distributeAdventureCards();
    }

    @And("player {int} has hand {string}")
    public void setPlayerHand(int playerId, String cardList) {
        Player player = game.getPlayer(playerId-1);
        String[] cards = cardList.split(", ");
        List<String> correctHand = Arrays.asList(cards);
        List<String> playerHand = player.getHand().stream().map(AdventureCard::toString).collect(Collectors.toList());
        assertEquals(correctHand, playerHand, "P" + playerId + "'s hand is incorrect!");
    }

    @When("player {int} draws a quest of {int} stages")
    public void drawQuest(int playerId, int stages) {
        output = new StringWriter();
        game.drawNextEventCard(new PrintWriter(output));

//        assertEquals(event, game.getCurrentEvent().getName(), "Incorrect Event Card!");
    }

    @And("player {int} is asked to sponsor and {string}")
    public void findingSponsor(int playerId, String decision) {
        //Clears previous input and output
        output = new StringWriter();

        Player player = game.getPlayer(playerId-1);

        if(player.canSponsor(game.getCurrentEvent())) playersConnect.add(player);


        if(decision.equalsIgnoreCase("declines")) input.append("n\n");
        else if (decision.equalsIgnoreCase("accepts")){
            input.append("y\n");
            game.findSponsor(new Scanner(input.toString()), new PrintWriter(output));
            input.setLength(0);

            for(Player p : playersConnect){
                assertTrue(output.toString().contains("P" + playerId + ", do you want to sponsor this quest?"),
                        "Should ask P" + playerId + " to sponsor quest!");
                assertTrue(output.toString().contains("P" + p.getId() + ", do you want to sponsor this quest?"),
                        "Should ask P" + p.getId() + " to sponsor quest!");
                assertTrue(game.isQuestSponsored(), "Game Quest Should be Sponsored!");
            }

            playersConnect.clear();
            return;
        }

        int numCanSponsor = 0;
        for(Player p : game.getPlayers()){
            if(p.canSponsor(game.getCurrentEvent())) numCanSponsor += 1;
        }
        //Last Player that can sponsor (Everyone Declined)
        if(playersConnect.size() == numCanSponsor){
            game.findSponsor(new Scanner(input.toString()), new PrintWriter(output));
            input.setLength(0);
            playersConnect.clear();
        }
    }

    @And("player {int} builds the {int} stages as {string}")
    public void buildStages(int playerId, int stages, String builtStages) {
        //Clears previous input and output
        output = new StringWriter();
        input.setLength(0);

        List<List<String>> builtStagesList = StringTo2DList(builtStages);
        Player player = game.getPlayer(playerId - 1);

        List<AdventureCard> duplicateHand = new ArrayList<>(player.getHand());
        // Go through each stage and find the index of the card the player wants to use
        for(List<String> stage : builtStagesList){
            for(String card : stage){
                int index  = findCardIndexByName(duplicateHand, card);
                input.append(index).append("\n");
            }
            input.append("Quit\n");
        }

        player.sponsorCard(new Scanner(input.toString()), new PrintWriter(output), game.getCurrentEvent());

        for(List<String> stage : builtStagesList){
            assertTrue(output.toString().contains("Stage is valid. Cards used in this stage: " + stage),
                    "Should Contain stage: " + stage + "!");
        }
        //Clears for next
        input.setLength(0);
    }

    @Then("player {int} participates in stage {int}")
    public void playerParticipatesInStage(int playerId, int stageNum) {
        output = new StringWriter();

        Player player = game.getPlayer(playerId-1);

        playersConnect.add(player);

        //Find eligible
        List<Player> eligibleParticipants;
        if(stageNum == 1){
            eligibleParticipants = game.determineEligibleParticipants(new PrintWriter(output), 0, game.getPlayers());
        }
        else{
            eligibleParticipants = game.determineEligibleParticipants(new PrintWriter(output), (stageNum-1), stageEligibleParticipants.get(stageNum));
        }

        //Participates
        if(eligibleParticipants.contains(player)){
            input.append("1\n");
        }
        else{
            input.append("0\n");
        }

        //Prompts participants to continue if everyone made their choice
        if (!eligibleParticipants.isEmpty() && player.equals(eligibleParticipants.getLast())) {
            List<Player> participants = game.promptParticipantsContinue(eligibleParticipants, new Scanner(input.toString()), new PrintWriter(output));
            stageEligibleParticipants.put(stageNum, participants);

            for (Player correctParticipant : playersConnect) {
                assertTrue(output.toString().contains(correctParticipant.toString() + " will tackle the stage."));
            }

            playersConnect.clear();
            input.setLength(0);
        }
    }

    @And("player {int} draws {string} and discards {string}")
    public void playerDrawsSpecificCardAndDiscard(int playerId, String drawnCards, String discardCards){
        output = new StringWriter();
        input.setLength(0);

        Player player = game.getPlayer(playerId-1);

        int correctHandSize = player.getHandSize();

        //DISCARD GIVEN CARDS if given
        if(!discardCards.isEmpty()) {
            String[] cardsToDiscard = discardCards.split(", ");
            correctHandSize -= cardsToDiscard.length;
            for (String card : cardsToDiscard) {
                List<AdventureCard> duplicateHand = new ArrayList<>(player.getHand());
                int index = findCardIndexByName(duplicateHand, card);
                input.append(index).append("\n");
            }
        }

        //Players draws cards
        String[] cardsToDraw = drawnCards.split(", ");
        correctHandSize += cardsToDraw.length;
        player.drawAdventureCards(1, game.getAdventureDeck(), game.getAdventureDiscardPile(), new Scanner(input.toString()), new PrintWriter(output));

        assertEquals(correctHandSize, player.getHandSize(), "P" + player.getId() + "'s hand is incorrect!");

        input.setLength(0);
    }

    @And("player {int} draws {string}")
    public void playerDrawsSpecificCard(int playerId, String drawnCards){
        playerDrawsSpecificCardAndDiscard(playerId, drawnCards, "");
    }

    @And("player {int} builds an attack {string} for stage {int}")
    public void playerBuildsAnAttack(int playerId, String attacks, int stageNum) {
        //Clears previous output
        output = new StringWriter();

        Player player = game.getPlayer(playerId-1);
        List<Player> participants = stageEligibleParticipants.get(stageNum);

        String[] attacksList = attacks.split(", ");;
        List<AdventureCard> duplicateHand = new ArrayList<>(player.getHand());
        for (String card : attacksList) {
            int index = findCardIndexByName(duplicateHand, card);
            input.append(index).append("\n");
        }
        input.append("Quit\n");

//        System.out.println(input);

        if (!participants.isEmpty() && player.equals(participants.getLast())){
            List<Player> stageWinners = game.resolveAttacks(participants, new Scanner(input.toString()), new PrintWriter(output), stageNum-1);
            stageEligibleParticipants.put((stageNum + 1), stageWinners);

            input.setLength(0);
        }

    }


    @And("player {int} loses stage {int} with hand as {string} and {int} shields")
    public void playerLoses(int playerId, int stage, String hand, int numShields) {
        Player player = game.getPlayer(playerId-1);

        List<String> correctHand = Arrays.asList(hand.split(", "));
        List<String> playerHand = player.getHand().stream().map(AdventureCard::toString).toList();
        assertEquals(correctHand, playerHand,
                "Player " + playerId + "'s hand is incorrect!");
        assertEquals(numShields, player.getShields(),
                "Player " + playerId + " should have " + numShields + " shields!");
        assertFalse(stageEligibleParticipants.get(stage + 1).contains(player),
                "Player " + playerId + " should be out of the quest!");
    }

    @And("player(s) {string} wins with {int} shields and hand as {string}")
    public void playerWins(String playersId, int numShields, String hand) {
        List<Player> winners = stageEligibleParticipants.get(stageEligibleParticipants.size());
        game.payWinners(winners, numShields);

        List<List<String>> winnersHands = StringTo2DList(hand);
        List<Integer> winnersId = Arrays.stream(playersId.split(", ")).map(Integer::parseInt).toList();
        for(int i = 0; i < winners.size(); i++){
            Player player = winners.get(i);

            assertEquals(winnersId.get(i), player.getId(),
                    "Winner should be Player " + winnersId.get(i) + " not Player " + player.getId());

            List<String> playerHand = player.getHand().stream().map(AdventureCard::toString).toList();
            assertEquals(winnersHands.get(i), playerHand,
                    "Player " + i + "'s hand is incorrect!");

            assertEquals(numShields, player.getShields(),
                    "Player " + i + " should have " + numShields + " shields!");
        }
    }

    @And("player {int} discards all quest cards and draws {int} cards, then trims to {int} cards")
    public void sponsorUpdatesCards(int playerId, int numCardsDraw, int numCardsAfterDiscard) {
        //Clears previous input and output
        output = new StringWriter();
        input.setLength(0);

        Player sponsor = game.getPlayer(playerId-1);

        int numDiscards = (sponsor.getHandSize() + numCardsDraw) - 12;

        if(numDiscards > 0){
            input.append("0\n".repeat(numDiscards));
        }
        input.append("\n");

        stageEligibleParticipants.clear();
        game.endQuest(new Scanner(input.toString()), new PrintWriter(output));

        assertEquals(numCardsAfterDiscard, sponsor.getHandSize(),
                "Player "+ sponsor.getId() +" should have " + numCardsAfterDiscard + " cards in hand!");
    }

}
