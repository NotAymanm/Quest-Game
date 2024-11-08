package org.example;

import java.io.PrintWriter;
import java.util.*;


public class Main {

    private final Deck deck = new Deck();
    private final List<Player> players = new ArrayList<>();

    private int currentPlayerIndex = 0;
    private EventCard currentEvent = null;

    private boolean gameOver = false;

    private Player questSponsor = null;

    public static void main(String[] args) {
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();
        Scanner input = new Scanner(System.in);
        PrintWriter output = new PrintWriter(System.out);
        game.distributeAdventureCards();

        game.startTurn(output);
        while(!game.gameOver){
            game.startGame(input, output);
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
            player.drawAdventureCards(12, deck.adventureDeck, deck.adventureDiscardPile, new Scanner(""), null);
        }
    }
    public void drawNextEventCard(PrintWriter output){
        currentEvent = deck.drawNextEventCard(output);
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

    public Deck getDeck() {
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

    public void displayWinners(PrintWriter output){
        List<Player> winners = checkForWinners();
        if(!winners.isEmpty()){
            for(Player winner: winners){
                output.println("Winner: P" + winner.getId()); output.flush();
            }
            gameOver = true;
        }
    }

    public void displayHotseat(PrintWriter output){
        output.println("Current Player in Hotseat: P" + getCurrentPlayer().getId()); output.flush();
    }

    public void clearHotseat(PrintWriter output){
        for(int i = 0; i < 15; i++){
            output.print("\n"); output.flush();
        }
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

    public void processEvent(Scanner input, PrintWriter output){
        if(currentEvent.getName().equals("Plague")){
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.loseShields(2);
        }
        else if(currentEvent.getName().equals("Queen's Favor")){
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.drawAdventureCards(2, deck.adventureDeck, deck.adventureDiscardPile, input, output);
        }
        else if(currentEvent.getName().equals("Prosperity")){
            for(int i = 0; i < players.size(); i++){
                //takes cards in order of current player
                Player player = getPlayer((currentPlayerIndex + i) % players.size());
                player.drawAdventureCards(2, deck.adventureDeck, deck.adventureDiscardPile, input, output);
                clearHotseat(output);
            }
        }

        currentEvent = null;
    }

    public void startGame(Scanner input, PrintWriter output){
        drawNextEventCard(output);
        output.println(); output.flush();
        if(currentEvent.getType().equals("Event")){
            output.println("Executing " + currentEvent.getName());
            processEvent(input, output);
            output.println("The quest card has been discarded."); output.flush();
            nextTurn(input, output);
        }
        else if(currentEvent.getType().equals("Quest")){
            processQuest(input, output);
        }
    }

    public void startTurn(PrintWriter output){
        Player currentPlayer = getCurrentPlayer();
        output.println("P" + currentPlayer.getId() + "'s turn has begun."); output.flush();
        displayHotseat(output);
        output.print("P" + currentPlayer.getId() + "'s current Hand: ");
        printList(currentPlayer.getHand(), output);
    }

    public void endTurn(Scanner input, PrintWriter output){
        Player currentPlayer = getCurrentPlayer();
        output.println(currentPlayer.toString() + ", Please leave the hotseat by hitting the <return> key..."); output.flush();
        input.nextLine();
        clearHotseat(output);
        output.println("P"+ currentPlayer.getId() + "'s turn has concluded."); output.flush();

        nextPlayer();
    }

    public void nextTurn(Scanner input, PrintWriter output){
        endTurn(input, output);
        displayWinners(output);
        if(!gameOver){
            startTurn(output);
        }
    }

    public Player findSponsor(Scanner input, PrintWriter output){
        int i = 0;
        while(questSponsor == null && i != players.size()){
            Player p = getPlayer((currentPlayerIndex + i) % players.size());
            if(p.canSponsor(currentEvent)){
                if(!p.equals(getCurrentPlayer())) clearHotseat(output);
                output.println(currentEvent.getName() + " has Started."); output.flush();
                if(p.sponsorQuest(input, output)){
                    questSponsor = p;
                }
                clearHotseat(output);
            }
            i++;
        }
        if(questSponsor != null) return questSponsor;

        output.println("All players have declined to sponsor the quest."); output.flush();
        currentEvent = null;
        output.println("The quest has been discarded."); output.flush();

        nextTurn(input, output);
        return null;
    }

    public List<Player> determineEligibleParticipants(PrintWriter output, int stage, List<Player> participantsCheck){
        List<List<AdventureCard>> stages = questSponsor.getStages();
        List<Player> eligibleParticipants = new ArrayList<>();


        for (Player player : participantsCheck) {
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
                if(totalWeaponValue >= totalStageValue){
                    eligibleParticipants.add(player);
                }
            }
        }

        if(eligibleParticipants.isEmpty()){
            output.println("No eligible participants for Stage "+ (stage+1)+"."); output.flush();
        }
        else{
            output.print("Eligible participants for Stage "+ (stage+1)+ ": "); output.flush();
            printList(eligibleParticipants, output);
        }

        return eligibleParticipants;
    }

    public List<Player> promptParticipantsContinue(List<Player> eligiblePlayers, Scanner input, PrintWriter output){
        List<Player> participants = new ArrayList<>();
        for(Player player: eligiblePlayers){
            output.println(player.toString() + ", do you want to (1) Tackle or (2) Withdraw the stage?"); output.flush();

            if(input.hasNextLine()) {
                String inputChoice = input.nextLine();
                int inputNum = Integer.parseInt(inputChoice);
                if(inputNum == 1){
                    participants.add(player);
                    output.println(player.toString() + " will tackle the stage."); output.flush();
                }
                else{
                    output.println(player.toString() + " has withdrawn from the quest."); output.flush();
                }
            }
        }

        if(participants.isEmpty()){
            output.println("No participants remain. The quest has ended."); output.flush();
            endQuest(input, output);
        }

        return participants;
    }

    public List<Player> getParticipants(List<Player> participantsCheck, Scanner input, PrintWriter output, int stage){
        List<Player> eligibleParticipants = determineEligibleParticipants(output, stage, participantsCheck);
        List<Player> participants = promptParticipantsContinue(eligibleParticipants, input, output);

        if(!participants.isEmpty()) output.println("\n\nStage " + (stage+1)+ " begins!"); output.flush();

        for(Player player : participants){
            player.drawAdventureCards(1, deck.adventureDeck, deck.adventureDiscardPile, input, output);
            clearHotseat(output);
        }

        return participants;
    }

    public List<Player> resolveAttacks(List<Player> participants, Scanner input, PrintWriter output, int stage){

        List<List<AdventureCard>> stages = questSponsor.getStages();
        List<Player> eligibleParticipants = new ArrayList<>();
        List<Player> ineligibleParticipants = new ArrayList<>();

        for(Player player : participants){
            clearHotseat(output);
            List<AdventureCard> attack = player.buildAttack(input, output);
            int attackValue = 0;
            for(AdventureCard card : attack){
                attackValue += card.getValue();
            }
            int totalStageValue = 0;
            for(AdventureCard card : stages.get(stage)){
                totalStageValue += card.getValue();
            }
            if(attackValue < totalStageValue){
                ineligibleParticipants.add(player);
            }
            else{
                eligibleParticipants.add(player);
            }

            deck.adventureDiscardPile.addAll(attack);
            attack.clear();
        }

        for(Player player:eligibleParticipants){
            output.println(player.toString() + "'s Attack was successful! Player can proceed to the next stage."); output.flush();
        }
        for(Player player:ineligibleParticipants){
            output.println(player.toString() + "'s Attack failed! Player cannot proceed to the next stage."); output.flush();
        }

        return eligibleParticipants;
    }

    public void processQuest(Scanner input, PrintWriter output){
        findSponsor(input, output);

        if(currentEvent == null) return;

        clearHotseat(output);
        questSponsor.sponsorCard(input, output, currentEvent);
        clearHotseat(output);

        int numStages = Character.getNumericValue(currentEvent.getName().charAt(1));

        List<Player> participantsCheck = new ArrayList<>(players);
        for(int i = 0; i < numStages; i++){
            List<Player> participants = getParticipants(participantsCheck, input, output, i);
            if(currentEvent == null) return;
            participantsCheck.clear();
            participantsCheck.addAll(resolveAttacks(participants,input, output, i));
        }

        output.println("The quest has ended."); output.flush();

        payWinners(participantsCheck, numStages);

        endQuest(input, output);
    }

    public void endQuest(Scanner input, PrintWriter output){
        List<List<AdventureCard>> stages = questSponsor.getStages();

        int numCards = numCardsSponsored(stages) + stages.size();

        for(List<AdventureCard> stage : stages){
            deck.adventureDiscardPile.addAll(stage);
        }
        questSponsor.clearStages();

        questSponsor.drawAdventureCards(numCards, deck.adventureDeck, deck.adventureDiscardPile, input, output);

        currentEvent = null;
        questSponsor.removeSponsor();
        questSponsor = null;
        output.println("The quest card has been discarded."); output.flush();
        nextTurn(input, output);
    }

    public void payWinners(List<Player> winners, int numStages){
        for(Player questWinner : winners){
            questWinner.addShields(numStages);
        }
    }

    public int numCardsSponsored(List<List<AdventureCard>> stages){
        int numCards = 0;
        for(List<AdventureCard> stage : stages){
            numCards += stage.size();
        }
        return numCards;
    }

    public void printList(List<?> myList, PrintWriter output){
        output.print("["); output.flush();
        for(int i = 0; i < myList.size(); i++){
            output.print(myList.get(i)); output.flush();
            if(i < myList.size() -1){
                output.print(", "); output.flush();
            }
        }
        output.println("]"); output.flush();
    }

}