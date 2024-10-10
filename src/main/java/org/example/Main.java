package org.example;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import java.util.Collections;

public class Main {

    private List<AdventureCard> adventureDeck = new ArrayList<>();
    private List<EventCard> eventDeck = new ArrayList<>();

    private List<AdventureCard> adventureDiscardPile = new ArrayList<>();
    private List<EventCard> eventDiscardPile = new ArrayList<>();

    private List<Player> players = new ArrayList<>();

    private int currentPlayerIndex = 0;
    private EventCard currentEvent = null;

    private boolean gameOver = false;

    public static void main(String[] args) {
        Main game = new Main();
        game.setUpDecks();
        game.initPlayers();
        game.distributeAdventureCards();

        PrintWriter output = new PrintWriter(System.out);

        game.displayWinners(output);

        game.drawNextEventCard(output);

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
            for(int i = 0; i < 12; i++){
                if(!adventureDeck.isEmpty()){
                    player.takeAdventureCard(adventureDeck.removeLast());
                }
            }
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

    private void addAdventureCards(String type, String name, int value, int count){
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
    }

    public void clearHotseat(PrintWriter output){
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public boolean isQuest(){
        return currentEvent.getType().equals("Quest");
    }

    public void processEvent(){
        if(currentEvent.getName().equals("Plague")){
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.loseShields(2);
        }
        else if(currentEvent.getName().equals("Queen's Favor")){
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.takeAdventureCards(2, adventureDeck, adventureDiscardPile);
        }
        else if(currentEvent.getName().equals("Prosperity")){
            for(int i = 0; i < players.size(); i++){
                //takes cards in order of current player
                Player player = getPlayer((currentPlayerIndex + i) % players.size());
                player.takeAdventureCards(2, adventureDeck, adventureDiscardPile);
            }
        }
    }

    public void endTurn(PrintWriter output){
    }

    //For testing
    public void printList(List<?> myList){
        System.out.print("[");
        for(int i = 0; i < myList.size(); i++){
            System.out.print(myList.get(i));
            if(i < myList.size() -1){
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

}