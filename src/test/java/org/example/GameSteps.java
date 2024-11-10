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


    @Given("the game starts with rigged deck for A1_scenario")
    public void setupA1Scenario() {
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

    @Given("the game starts with rigged deck for 2winner_game_2winner_quest")
    public void setup2winner_game_2winner_quest() {
        game.setUpDecks();

        List<EventCard> eventCards = Arrays.asList(
                new EventCard("Q4", "Quest"),
                new EventCard("Q3", "Quest")
        );

        List<AdventureCard> adventureCards = Arrays.asList(
                //P1
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Foe", "F15", 15),
                new AdventureCard("Foe", "F20", 20),
                new AdventureCard("Foe", "F30", 30),
                new AdventureCard("Weapon", "D5", 5),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "B15", 15),

                //P2
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F15", 15),
                new AdventureCard("Foe", "F20", 20),
                new AdventureCard("Foe", "F35", 35),
                new AdventureCard("Weapon", "D5", 5),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "B15", 15),
                new AdventureCard("Weapon", "B15", 15),
                new AdventureCard("Weapon", "B15", 15),
                new AdventureCard("Weapon", "L20", 20),

                //P3
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Foe", "F15", 15),
                new AdventureCard("Foe", "F20", 20),
                new AdventureCard("Weapon", "D5", 5),
                new AdventureCard("Weapon", "D5", 5),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "H10", 10),

                //P4
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F15", 15),
                new AdventureCard("Foe", "F20", 20),
                new AdventureCard("Foe", "F25", 25),
                new AdventureCard("Foe", "F25", 25),
                new AdventureCard("Weapon", "D5", 5),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "B15", 15),
                new AdventureCard("Weapon", "L20", 20),

                //Draws
                //Stage 1
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Foe", "F40", 40),
                new AdventureCard("Weapon", "L20", 20),
                //Stage 2
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Weapon", "H10", 10),
                //Stage 3
                new AdventureCard("Weapon", "D5", 5),
                new AdventureCard("Foe", "F25", 25),
                //Stage 4
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "E30", 30),

                //P1 draws 10 cards
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Foe", "F15", 15),
                new AdventureCard("Foe", "F15", 15),
                new AdventureCard("Foe", "F20", 20),
                new AdventureCard("Foe", "F20", 20),
                new AdventureCard("Foe", "F25", 25),
                new AdventureCard("Foe", "F30", 30),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "S10", 10),

                //Draws
                //Stage 1
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "S10", 10),
                //Stage 2
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Weapon", "S10", 10),
                //Stage 3
                new AdventureCard("Weapon", "E30", 30),
                new AdventureCard("Weapon", "L20", 20)
        );


        game.getDeck().rigAdventureDeck(adventureCards);
        game.getDeck().rigEventDeck(eventCards);

        //System.out.println(game.getAdventureDeck().size());

        game.initPlayers();
        game.distributeAdventureCards();
    }

    @Given("the game starts with rigged deck for 1winner_game_with_events")
    public void setup1winner_game_with_events() {
        game.setUpDecks();

        List<EventCard> eventCards = Arrays.asList(
                new EventCard("Q4", "Quest"),
                new EventCard("Plague", "Event"),
                new EventCard("Prosperity", "Event"),
                new EventCard("Queen's Favor", "Event"),
                new EventCard("Q3", "Quest")
        );

        List<AdventureCard> adventureCards = Arrays.asList(
                //P1
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Foe", "F15", 15),
                new AdventureCard("Foe", "F25", 25),
                new AdventureCard("Foe", "F40", 40),
                new AdventureCard("Weapon", "D5", 5),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "B15", 15),

                //P2
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F15", 15),
                new AdventureCard("Foe", "F20", 20),
                new AdventureCard("Weapon", "D5", 5),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "B15", 15),
                new AdventureCard("Weapon", "L20", 20),
                new AdventureCard("Weapon", "E30", 30),

                //P3
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F15", 15),
                new AdventureCard("Foe", "F15", 15),
                new AdventureCard("Foe", "F25", 25),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "B15", 15),
                new AdventureCard("Weapon", "B15", 15),
                new AdventureCard("Weapon", "L20", 20),

                //P4
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F20", 20),
                new AdventureCard("Foe", "F20", 20),
                new AdventureCard("Foe", "F25", 25),
                new AdventureCard("Foe", "F30", 30),
                new AdventureCard("Foe", "F40", 40),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "B15", 15),
                new AdventureCard("Weapon", "B15", 15),
                new AdventureCard("Weapon", "B15", 15),
                new AdventureCard("Weapon", "L20", 20),

                //Draws
                //Stage 1
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "L20", 20),
                new AdventureCard("Weapon", "D5", 5),
                //Stage 2
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "S10", 10),
                //Stage 3
                new AdventureCard("Foe", "F20", 20),
                new AdventureCard("Weapon", "D5", 5),
                new AdventureCard("Foe", "F15", 15),
                //Stage 4
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "L20", 20),
                new AdventureCard("Weapon", "D5", 5),

                //P1 draws 9 cards
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Foe", "F15", 15),
                new AdventureCard("Foe", "F20", 20),
                new AdventureCard("Foe", "F25", 25),
                new AdventureCard("Foe", "F25", 25),
                new AdventureCard("Foe", "F30", 30),
                new AdventureCard("Foe", "F35", 35),

                //Prosperity Draws
                //P3
                new AdventureCard("Weapon", "E30", 30),
                new AdventureCard("Weapon", "D5", 5),
                //P4
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Foe", "F5", 5),
                //P1
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Weapon", "B15", 15),
                //P2
                new AdventureCard("Weapon", "H10", 10),
                new AdventureCard("Weapon", "L20", 20),

                //Queen's Favor
                new AdventureCard("Foe", "F20", 20),
                new AdventureCard("Weapon", "H10", 10),

                //Draws
                //Stage 1
                new AdventureCard("Weapon", "D5", 5),
                new AdventureCard("Foe", "F10", 10),
                new AdventureCard("Foe", "F35", 35),
                //Stage 2
                new AdventureCard("Foe", "F5", 5),
                new AdventureCard("Foe", "F35", 35),
                //Stage 3
                new AdventureCard("Weapon", "S10", 10),
                new AdventureCard("Foe", "F50", 50)
        );


        game.getDeck().rigAdventureDeck(adventureCards);
        game.getDeck().rigEventDeck(eventCards);

        //System.out.println(game.getAdventureDeck().size());

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
        input.setLength(0);
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

    private void questParticipation(int playerId, int stageNum, int decision){
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
        if(eligibleParticipants.contains(player) && decision == 1){
            input.append("1\n");
        }
        else if(eligibleParticipants.contains(player) && decision == 0){
            input.append("0\n");
        }

        //Prompts participants to continue if everyone made their choice
        if (!eligibleParticipants.isEmpty() && player.equals(eligibleParticipants.getLast())) {
            List<Player> participants = game.promptParticipantsContinue(eligibleParticipants, new Scanner(input.toString()), new PrintWriter(output));
            stageEligibleParticipants.put(stageNum, participants);

            for (Player participant : participants) {
                assertTrue(output.toString().contains(participant.toString() + " will tackle the stage."),
                        "Player " + participant.getId() + " should be tackling the stage.");
            }

            playersConnect.clear();
            input.setLength(0);
        }
    }

    @Then("player {int} participates in stage {int}")
    public void playerParticipatesInStage(int playerId, int stageNum) {
        questParticipation(playerId, stageNum, 1);
    }

    @And("player {int} doesn't participate in stage {int} and the rest of the quest")
    public void playerDeclinesParticipation(int playerId, int stageNum){
        questParticipation(playerId, stageNum, 0);
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

    @And("player(s) {string} win(s) the quest, gain(s) {int} shields and has hand(s) as {string}")
    public void playerWinsQuest(String playersId, int numShields, String hand) {
        List<Player> winners = stageEligibleParticipants.get(stageEligibleParticipants.size());
        List<Integer> winnersNumShield = new ArrayList<>();
        for(Player winner : winners){
            winnersNumShield.add(winner.getShields());
        }

        game.payWinners(winners, numShields);

        List<List<String>> winnersHands = StringTo2DList(hand);
        List<Integer> winnersId = Arrays.stream(playersId.split(", ")).map(Integer::parseInt).toList();
        for(int i = 0; i < winners.size(); i++){
            Player player = winners.get(i);

            assertEquals(winnersId.get(i), player.getId(),
                    "Winner should be Player " + winnersId.get(i) + " not Player " + player.getId());

            List<String> playerHand = player.getHand().stream().map(AdventureCard::toString).toList();
            assertEquals(winnersHands.get(i), playerHand,
                    "Player " + player.getId() + "'s hand is incorrect!");

            assertEquals(winnersNumShield.get(i) + numShields, player.getShields(),
                    "Player " + player.getId() + " should have " + numShields + " shields!");
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

    @And("player(s) {string} won the game")
    public void playersWonTheGame(String playerIdWinners) {
        //Clears previous input and output
        output = new StringWriter();
        input.setLength(0);

        game.displayWinners(new PrintWriter(output));

        List<Integer> winnersId = Arrays.stream(playerIdWinners.split(", ")).map(Integer::parseInt).toList();

        for(int i = 0; i < game.getPlayers().size(); i++){
            Player player = game.getPlayer(i);
            if(winnersId.contains(player.getId())){
                assertTrue(output.toString().contains("Winner: P" + player.getId()),
                        "Player " + player.getId() + " Should be displayed as A Winner!");
            }
            else{
                assertFalse(output.toString().contains("Winner: P" + player.getId()),
                        "Player " + player.getId() + " Should NOT be displayed as A Winner!");
            }
        }
    }

    private void playerDrawsEvent(String input){
        output = new StringWriter();
        game.drawNextEventCard(new PrintWriter(output));
        game.processEvent(new Scanner(input), new PrintWriter(output));

        game.nextTurn(new Scanner(input), new PrintWriter(output));
    }

    @And("player {int} draws a {string} event and loses {int} shields")
    public void playerDrawsPlague(int playerId, String event, int numShields) {
        Player player = game.getPlayer(playerId-1);
        int playerShieldsBefore = player.getShields();

        int correctShields =  Math.max(playerShieldsBefore - numShields, 0);

        playerDrawsEvent("\n");

        //asserts
        assertEquals(correctShields, player.getShields(),
                "Player " + playerId + " has the wrong number of shields!");

    }

    @And("player {int} draws a {string} event and all players draw {int} adventure cards")
    public void playerDrawsProsperity(int playerId, String event, int numCards) {
        List<Integer> playerHandSize = new ArrayList<>();
        for(Player player : game.getPlayers()){
            playerHandSize.add(Math.min(player.getHandSize() + numCards, 12));
        }

        playerDrawsEvent("\n");

        //asserts
        for(int i = 0; i < game.getPlayers().size(); i++){
            assertEquals(playerHandSize.get(i), game.getPlayer(i).getHandSize(),
                    "Player " + game.getPlayer(i).getId() + "'s hand size is incorrect!");
        }
    }

    @And("player {int} draws a {string} event and all players draw {int} adventure cards, discarding {string} in order of players")
    public void playerDrawsProsperity(int playerId, String event, int numCards, String discardCards) {
        input.setLength(0);

        List<Integer> playerHandSize = new ArrayList<>();
        for(Player player : game.getPlayers()){
            playerHandSize.add(Math.min(player.getHandSize() + numCards, 12));
        }

        List<List<String>> discardList = StringTo2DList(discardCards);
        // Go through each stage and find the index of the card the player wants to use
        for(int i = 0; i < discardList.size(); i++){
            List<String> discard = discardList.get(i);

            if((discard.size() == 1 && (discard.getFirst().isEmpty()))){ continue; }

            Player player = game.getPlayer(((playerId-1) + i) % game.getPlayers().size());
            List<AdventureCard> duplicateHand = new ArrayList<>(player.getHand());
            for(String card : discard){
                int index  = findCardIndexByName(duplicateHand, card);
                input.append(index).append("\n");
            }
        }

        input.append("\n");
        playerDrawsEvent(input.toString());

        //asserts
        for(int i = 0; i < game.getPlayers().size(); i++){
            assertEquals(playerHandSize.get(i), game.getPlayer(i).getHandSize(),
                    "Player " + game.getPlayer(i).getId() + "'s hand size is incorrect!");
        }

    }

    @And("player {int} draws a {string} event and draws {int} adventure cards")
    public void playerDrawsQueensFavor(int playerId, String event, int numCards) {
        Player player = game.getPlayer(playerId-1);
        int correctHandSize = Math.min(player.getHandSize() + numCards, 12);

        playerDrawsEvent("\n");

        //asserts
        assertEquals(correctHandSize, player.getHandSize(),
                "Player " + playerId + "'s hand size is incorrect!");
    }

    @And("player {int} draws a {string} event and draws {int} adventure cards, discarding {string}")
    public void playerDrawsQueensFavor(int playerId, String event, int numCards, String discardCards) {
        input.setLength(0);
        String[] cards = discardCards.split(", ");
        Player player = game.getPlayer(playerId-1);

        int correctHandSize = Math.min(player.getHandSize() + numCards, 12);

        List<AdventureCard> duplicateHand = new ArrayList<>(player.getHand());
        for(String card : cards){
            int index  = findCardIndexByName(duplicateHand, card);
            input.append(index).append("\n");
        }

        input.append("\n");
        playerDrawsEvent(input.toString());

        //asserts
        assertEquals(correctHandSize, player.getHandSize(),
                "Player " + playerId + "'s hand size is incorrect!");
    }



}
