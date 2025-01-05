package org.example;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Main {

    private final Deck deck = new Deck();

    private final List<Player> players = new ArrayList<>();

    private int currentPlayerIndex = 0;
    private EventCard currentEvent = null;

    private boolean gameOver = false;

    private Player questSponsor = null;


    private final List<Player> questParticipants = new ArrayList<>();
    private final List<Player> eligibleSponsors = new ArrayList<>();

    public static void main(String[] args) {
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();
        game.distributeAdventureCards();

        game.startTurn();
        while(!game.gameOver){
            game.startGame();
        }

    }

    public void setUpDecks(){
        deck.initDeck();
    }

    public void initPlayers(){
        addPlayers(4);
    }

    public void distributeAdventureCards(){
        for(Player player: players){
            player.drawAdventureCards(12, deck.adventureDeck, deck.adventureDiscardPile);
        }
    }

    public EventCard drawNextEventCard(){
        currentEvent = deck.drawNextEventCard();
        return currentEvent;
    }

    public List<AdventureCard> getAdventureDeck(){
        return deck.adventureDeck;
    }

    public List<AdventureCard> getAdventureDiscardPile(){
        return deck.adventureDiscardPile;
    }

    public List<EventCard> getEventDeck(){
        return deck.eventDeck;
    }

    public List<EventCard> getEventDiscardPile() {return deck.eventDiscardPile;}

    public List<Player> getPlayers(){
        return players;
    }

    public Player getCurrentPlayer(){
        return players.get(currentPlayerIndex);
    }

    public Player getPlayer(int index){
        return players.get(index);
    }

    public EventCard getCurrentEvent(){
        return currentEvent;
    }

    public Player getQuestSponsor() { return questSponsor; }

    public List<Player> getQuestParticipants() { return questParticipants; }

    public void setCurrentEvent(EventCard card){
        currentEvent = card;
    }

    public void addAdventureCards(String type, String name, int value, int count){
        deck.addAdventureCards(type, name, value, count);
    }

    private void addPlayers(int count) {
        for(int i=0; i < count; i++){
            players.add(new Player(i+1));
        }
    }

    public Deck getDeck(){
        return deck;
    }

    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public List<Player> checkForWinners(){
        List<Player> winners = new ArrayList<>();
        for(Player player: players){
            if(player.getShields() >= 7){
                winners.add(player);
            }
        }

        return winners;
    }

    public List<Player> displayWinners(){
        List<Player> winners = checkForWinners();
        if(!winners.isEmpty()){
            gameOver = true;
            return winners;
        }
        return null;
    }

    public void setGameOver(){
        gameOver = true;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public boolean isQuest(){
        return currentEvent.getType().equals("Quest");
    }

    public boolean isQuestSponsored(){
        return questSponsor != null;
    }

    public Map<String, Object> processEvent(){
        String output = "";
        Player currentPlayer = getCurrentPlayer();
        if(currentEvent.getName().equals("Plague")){
            currentPlayer.loseShields(2);
            output = currentPlayer + " has been hit by the Plague. Lost 2 shields.";
        }
        else if(currentEvent.getName().equals("Queen's Favor")){
            currentPlayer.drawAdventureCards(2, deck.adventureDeck, deck.adventureDiscardPile);
            output = currentPlayer + " has been given the Queen's Favor. draws 2 cards.";
        }
        else if(currentEvent.getName().equals("Prosperity")){
            for(int i = 0; i < players.size(); i++){
                //takes cards in order of current player
                Player player = getPlayer((currentPlayerIndex + i) % players.size());
                player.drawAdventureCards(2, deck.adventureDeck, deck.adventureDiscardPile);
            }
            output = "Prosperity hit! Everyone gets 2 cards.";
        }

        currentEvent = null;

        Map<String, Object> response = new HashMap<>();
        response.put("message", output);
        response.put("player", currentPlayer);

        return response;
    }

    public void startGame(){
        drawNextEventCard();
        if(currentEvent.getType().equals("Event")){
            processEvent();
//            nextTurn();
        }
        else if(currentEvent.getType().equals("Quest")){
//            processQuest();
        }
    }

    public Player startTurn(){
        return getCurrentPlayer();
    }

    public void endTurn(){
        nextPlayer();
    }

    public Map<String, Object> nextTurn(){
        Map<String, Object> result = new HashMap<>();

        endTurn();
        List<Player> winners = displayWinners();
        if(!gameOver){
            Player player = startTurn();
            result.put("nextPlayer", player);
        }

        result.put("message", "Player " + getCurrentPlayer().getId() + "'s turn started.");
        result.put("winners", winners);

        return result;
    }

    public List<Player> determineEligibleSponsors(){
        eligibleSponsors.clear();

        for(int i = 0; i < players.size(); i++){
            Player p = getPlayer((currentPlayerIndex + i) % players.size());
            if(p.canSponsor(currentEvent)){
                eligibleSponsors.add(p);
            }
        }

        return eligibleSponsors;
    }

    public Map<String, Object> promptNextPlayerForSponsor(int currentIndex, Integer decision) {
        Map<String, Object> result = new HashMap<>();

        Player currentPlayer = eligibleSponsors.get(currentIndex - 1);

        // Process the current player's decision
        if(decision != null){
            // if they say yes, set current player as sponsor
            if(decision == 1) {
                currentPlayer.setAsSponsor();
                questSponsor = currentPlayer;
                result.put("sponsored", "yes");
                result.put("message", currentPlayer + " will sponsor this quest.");
                result.put("sponsorId", currentPlayer.getId());
                return result;
            }
        }

        result.put("sponsored", "no");
        result.put("message", currentPlayer + " declined to sponsor this quest.");

        // Check if there are more players to prompt
        if(currentIndex < eligibleSponsors.size()){
            result.put("nextPlayer", eligibleSponsors.get(currentIndex)); // Return the next Player to prompt
            return result;
        }

        //No Sponsor
        result.put("nextPlayer", null);
        currentEvent = null;
        eligibleSponsors.clear();

        return result;
    }

    public List<Player> determineEligibleParticipants(int stage){
        // If it's the first stage everyone is eligible
        if(stage == 0) {
            questParticipants.remove(getQuestSponsor());
            return questParticipants;
        }

        List<List<AdventureCard>> stages = questSponsor.getStages();
        List<Player> ineligiblePlayers = new ArrayList<>();

        for (Player player : questParticipants) {
            //gets responses in order of current player
            if (!player.isSponsor()) {
                int totalWeaponValue = 0;
                for(AdventureCard card : player.getHand()){
                    if(card.getType().equals("Weapon")){
                        totalWeaponValue += card.getValue();
                    }
                }
                int totalStageValue = 0;
                for(AdventureCard card : stages.get(stage)){
                    totalStageValue += card.getValue();
                }
                if(totalWeaponValue < totalStageValue){
                    ineligiblePlayers.add(player);
                }
            }
            else{
                ineligiblePlayers.add(player);
            }
        }

        questParticipants.removeAll(ineligiblePlayers);

        if(questParticipants.isEmpty()){
//            "No eligible participants for Stage "+ (stage+1)+"."
        }
        else{
//            "Eligible participants for Stage "+ (stage+1)+ ": "
        }

        return questParticipants;
    }

    public Map<String, Object> promptNextParticipant(int currentIndex, Integer decision){
        Map<String, Object> response = new HashMap<>();

        // Process the current player's decision
        if (decision != null) {
            Player currentPlayer = questParticipants.get(currentIndex - 1);

            //if they don't tackle
            if (decision != 1) { // 1 = Tackle
                questParticipants.remove(currentPlayer); // removes from participants list
                currentIndex--; //updates indexing
            }
        }

        // Check if there are more players to prompt
        if(currentIndex < questParticipants.size()){
            response.put("nextPlayer", questParticipants.get(currentIndex));
            response.put("currentIndex", currentIndex);
        }
        else{
            response.put("nextPlayer", null);
        }

        response.put("participants", questParticipants);

        return response; //All players have been prompted
    }

    public Boolean participantsDraw(){
        if(questParticipants.isEmpty()) return false;

        for(Player player : questParticipants){
            player.drawAdventureCards(1, deck.adventureDeck, deck.adventureDiscardPile);
        }

        return true;
    }

    public Map<String, Object> resolveAttacks(int stage, int playerIndex, List<Integer> cardIndices){
        Player player = questParticipants.get(playerIndex);
        List<List<AdventureCard>> stages = questSponsor.getStages();

        String attackResult = player.buildAttack(cardIndices);

        Map<String, Object> result = new HashMap<>();
        //if attack is invalid
        if(!attackResult.contains("Attack is valid.")){
            result.put("attackStatus", "invalid");
            result.put("message", attackResult);
            return result;
        }

        result.put("attackStatus", "valid");

        int attackValue = 0;
        for(AdventureCard card : player.getAttack()){
            attackValue += card.getValue();
        }
        int totalStageValue = 0;
        for(AdventureCard card : stages.get(stage)){
            totalStageValue += card.getValue();
        }
        if(attackValue < totalStageValue){
            questParticipants.remove(player);
            playerIndex--;
            result.put("attack", "failed");
            result.put("message", player + "'s attack failed. They cannot proceed to the next stage.");
        }
        else{
            result.put("attack", "success");
            result.put("message", player + "'s attack was successful! They can proceed to the next stage.");
        }

        deck.adventureDiscardPile.addAll(player.getAttack());
        player.getAttack().clear();

        result.put("currentIndex", playerIndex);
        result.put("remainingParticipants", questParticipants);

        return result;
    }

    public void endQuest(){
        List<List<AdventureCard>> stages = questSponsor.getStages();

        int numCards = numCardsSponsored(stages) + stages.size();

        for(List<AdventureCard> stage : stages){
            deck.adventureDiscardPile.addAll(stage);
        }
        questSponsor.clearStages();

        questSponsor.drawAdventureCards(numCards, deck.adventureDeck, deck.adventureDiscardPile);

        currentEvent = null;
        questSponsor.removeSponsor();
        questSponsor = null;

        questParticipants.clear();
    }

    public List<Player> payWinners(int numStages){
        for(Player questWinner : questParticipants){
            questWinner.addShields(numStages);
        }
        return questParticipants;
    }

    public int numCardsSponsored(List<List<AdventureCard>> stages){
        int numCards = 0;
        for(List<AdventureCard> stage : stages){
            numCards += stage.size();
        }
        return numCards;
    }


}