package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/game")
@CrossOrigin()
public class GameController {
    @Autowired
    private Main game;

    private static LinkedHashMap<Integer, Integer> playersNeedingDiscard = null;

    // Mark a player as needing to discard
    public static void markPlayerNeedsToDiscard(Player player, int numToDiscard) {
        playersNeedingDiscard.put(player.getId(), numToDiscard);
    }

    @PostMapping("/initGame")
    public String initGame(
            @RequestParam(required = false) String test) {
        playersNeedingDiscard = new LinkedHashMap<>();
        game = new Main();
        game.setUpDecks();

        // Check if it's a test
        if (test != null && !test.isEmpty()) {
            List<EventCard> eventCards = TestDecks.getEventCardsForScenario(test);
            List<AdventureCard> adventureCards = TestDecks.getAdventureCardsForScenario(test);

            if (eventCards != null && adventureCards != null) {
                game.getDeck().rigAdventureDeck(adventureCards);
                game.getDeck().rigEventDeck(eventCards);
            }
        }

        game.initPlayers();
        game.distributeAdventureCards();
        return "Game Started.";
    }

    @GetMapping("/playerHand")
    public List<AdventureCard> getPlayerHand(
            @RequestParam int playerId) {
        Player player = game.getPlayer(playerId-1);
        return player.getHand();
    }

    @GetMapping("/playerShields")
    public int getShields(
            @RequestParam int playerId) {
        Player player = game.getPlayer(playerId-1);
        return player.getShields();
    }

    // Endpoint to draw the next event card
    @PostMapping("/drawEvent")
    public EventCard drawNextEvent() {
        return game.drawNextEventCard();
    }


    @PostMapping("/checkForDiscard")
    public List<Map.Entry<Integer, Integer>> checkDiscard(){
        //Removes players who don't need to discard
        playersNeedingDiscard.entrySet().removeIf(entry -> entry.getValue() == 0);
        return new ArrayList<>(playersNeedingDiscard.entrySet());
    }

    @PostMapping("/discardCards")
    public Boolean discardCards(
            @RequestParam int playerId,
            @RequestBody List<Integer> selectedCardIndices) {
        Player player = game.getPlayer(playerId-1);

        Boolean result =  player.discardCard(selectedCardIndices, game.getAdventureDiscardPile());

        if (player.numCardsToDiscard() <= 0) playersNeedingDiscard.remove(player.getId());

        return result;
    }

    // Endpoint to handle event processing
    @PostMapping("/processEvent")
    public Map<String, Object> processEvent() {
        return game.processEvent();
    }

    // Endpoint to advance to the next turn
    @PostMapping("/nextTurn")
    public Map<String, Object> nextTurn() {
        return game.nextTurn();
    }

    @PostMapping("/determineEligibleSponsors")
    public List<Player> determineEligibleSponsors() {
        return game.determineEligibleSponsors();
    }

    @PostMapping("/promptNextSponsor")
    public Map<String, Object> promptNextSponsor(
            @RequestBody PromptNextRequest request) {

        return game.promptNextPlayerForSponsor(
                request.getPlayerIndex(),
                request.getDecision()
        );
    }

    // Endpoint to handle the start of the quest and return the number of stages
    @PostMapping("/getQuest")
    public Map<String, Integer> startQuest() {
        Player player = game.getQuestSponsor();

        // Calculate numStages based on the eventCard
        int numStages = Character.getNumericValue(game.getCurrentEvent().getName().charAt(1));

        //Initializes all participants
        game.getQuestParticipants().addAll(game.getPlayers());

        // Send the number of stages to the frontend
        Map<String, Integer> response = new HashMap<>();
        response.put("numStages", numStages);
        response.put("player", player.getId());

        return response; // Return numStages in JSON response
    }

    // Endpoint to handle each stage being built
    @PostMapping("/buildStage")
    public String buildStage(
            @RequestParam int playerId,
            @RequestParam int stageNumber,
            @RequestBody List<Integer> selectedCardIndices) {

        Player player = game.getPlayer(playerId-1);

        // build the stage with the selected cards
        return player.buildStage(stageNumber, selectedCardIndices);
    }

    @PostMapping("/determineEligibleParticipants")
    public List<Player> determineEligibleParticipants(
            @RequestBody DetermineEligibleRequest request) {
        return game.determineEligibleParticipants(
                request.getStage());
    }

    @PostMapping("/promptNextParticipant")
    public Map<String, Object> promptNextParticipant(
            @RequestBody PromptNextRequest request) {
        return game.promptNextParticipant(
                request.getPlayerIndex(),
                request.getDecision()
        );
    }

    @PostMapping("/participantsDraw")
    public Boolean participantsDraw() {
        // Call the backend method to handle drawing adventure cards
        return game.participantsDraw();
    }

    @PostMapping("/resolveAttacks")
    public Map<String, Object> resolveAttacks(
            @RequestParam int stage,
            @RequestParam int playerIndex,
            @RequestBody List<Integer> cardIndices) {
        return game.resolveAttacks(stage, playerIndex, cardIndices);
    }

    @PostMapping("/payWinners")
    public List<Player> payWinners(
            @RequestParam int numStages){
        return game.payWinners(numStages);
    }

    @PostMapping("/endQuest")
    public void endQuest(){
        game.endQuest();
    }

    @PostMapping("/displayWinners")
    public List<Player> displayWinners() {
        return game.displayWinners();
    }

}


