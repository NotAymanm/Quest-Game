package org.example;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import java.util.Collections;
import java.util.Scanner;

public class Main {

    private List<AdventureCard> adventureDeck = new ArrayList<>();
    private List<EventCard> eventDeck = new ArrayList<>();

    private List<AdventureCard> adventureDiscardPile = new ArrayList<>();
    private List<EventCard> eventDiscardPile = new ArrayList<>();

    private List<Player> players = new ArrayList<>();

    private int currentPlayerIndex = 0;
    private EventCard currentEvent = null;

    private boolean gameOver = false;

    private Player questSponsor = null;

    public static void main(String[] args) {
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();
        game.distributeAdventureCards();

        Scanner input = new Scanner(System.in);
        PrintWriter output = new PrintWriter(System.out);

        game.displayWinners(output);

        game.drawNextEventCard(output);

        game.findSponsor(input, output);

    }

    public void setUpDecks(){
        // Setting up adventure deck
        addFoes();
        addWeapons();

        // Setting up event deck
        addQuestCards();
        addEventCards();

        Collections.shuffle(adventureDeck);
        Collections.shuffle(eventDeck);
    }

    public void initPlayers(){
        addPlayers(4);
    }

    public void distributeAdventureCards(){
        for(Player player: players){
            player.drawAdventureCards(12, adventureDeck, adventureDiscardPile);
        }
    }

    public void drawNextEventCard(PrintWriter output){
        if(eventDeck.isEmpty()){
            eventDeck.addAll(eventDiscardPile);
            eventDiscardPile.clear();
            Collections.shuffle(eventDeck);
        }

        currentEvent = eventDeck.removeLast();
        eventDiscardPile.add(currentEvent);

        output.println("Event Card Drawn: " + currentEvent); output.flush();
    }

    public List<AdventureCard> getAdventureDeck(){
        return adventureDeck;
    }

    public List<AdventureCard> getAdventureDiscardPile(){
        return adventureDiscardPile;
    }

    public List<EventCard> getEventDeck(){
        return eventDeck;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public Player getCurrentPlayer(){
        return players.get(currentPlayerIndex);
    }

    public Player getPlayer(int index){
        return players.get(index);
    }

    public void setCurrentEvent(EventCard card){
        currentEvent = card;
    }

    public void addAdventureCards(String type, String name, int value, int count){
        for(int i = 0; i < count; i++){
            adventureDeck.add(new AdventureCard(type, name, value));
        }
    }

    private void addEventCards(String name, String type, int count){
        for(int i=0; i < count; i++){
            eventDeck.add(new EventCard(name, type));
        }
    }

    private void addPlayers(int count) {
        for(int i=0; i < count; i++){
            players.add(new Player(i+1));
        }
    }

    private void addFoes(){
        addAdventureCards("Foe", "F5", 5, 8);
        addAdventureCards("Foe", "F10", 10, 7);
        addAdventureCards("Foe", "F15", 15, 8);
        addAdventureCards("Foe", "F20", 20, 7);
        addAdventureCards("Foe", "F25", 25, 7);
        addAdventureCards("Foe", "F30", 30, 4);
        addAdventureCards("Foe", "F35", 35, 4);
        addAdventureCards("Foe", "F40", 40, 2);
        addAdventureCards("Foe", "F50", 50, 2);
        addAdventureCards("Foe", "F70", 70, 1);
    }

    private void addWeapons(){
        addAdventureCards("Weapon", "D5", 5, 6); // Daggers
        addAdventureCards("Weapon", "H10", 10, 12); // Horses
        addAdventureCards("Weapon", "S10", 10, 16); // Swords
        addAdventureCards("Weapon", "B15", 15, 8); // Battle-Axes
        addAdventureCards("Weapon", "L20", 20, 6); // Lances
        addAdventureCards("Weapon", "E30", 30, 2); // Excaliburs
    }

    private void addQuestCards(){
        addEventCards("Q2", "Quest", 3);
        addEventCards("Q3", "Quest", 4);
        addEventCards("Q4", "Quest", 3);
        addEventCards("Q5", "Quest", 2);
    }

    private void addEventCards(){
        addEventCards("Plague", "Event", 1);
        addEventCards("Queen's Favor", "Event", 2);
        addEventCards("Prosperity", "Event", 2);
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

    public boolean isGameOver(){
        return gameOver;
    }

    public boolean isQuest(){
        return currentEvent.getType().equals("Quest");
    }

    public boolean isQuestSponsored(){
        return questSponsor != null;
    }

    public void processEvent(){
        if(currentEvent.getName().equals("Plague")){
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.loseShields(2);
        }
        else if(currentEvent.getName().equals("Queen's Favor")){
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.drawAdventureCards(2, adventureDeck, adventureDiscardPile);
        }
        else if(currentEvent.getName().equals("Prosperity")){
            for(int i = 0; i < players.size(); i++){
                //takes cards in order of current player
                Player player = getPlayer((currentPlayerIndex + i) % players.size());
                player.drawAdventureCards(2, adventureDeck, adventureDiscardPile);
            }
        }
    }

    public void startTurn(PrintWriter output){
        Player currentPlayer = getCurrentPlayer();
        output.println("P" + currentPlayer.getId() + "'s turn has begun."); output.flush();
        displayHotseat(output);
        output.print("P" + currentPlayer.getId() + "'s current Hand: ");
        printList(currentPlayer.getHand(), output);
    }

    public void endTurn(PrintWriter output){
        clearHotseat(output);
        Player currentPlayer = getCurrentPlayer();
        output.println("P"+ currentPlayer.getId() + "'s turn has concluded."); output.flush();

        nextPlayer();
    }

    public void findSponsor(Scanner input, PrintWriter output){
        int i = 0;
        while(questSponsor == null && i != players.size()){
            Player p = getPlayer((currentPlayerIndex + i) % players.size());
            if(p.sponsorQuest(input, output)){
                questSponsor = p;
            }
            i++;
        }
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