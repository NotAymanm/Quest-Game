package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private List<AdventureCard> adventureDeck = new ArrayList<>();
    private List<EventCard> eventDeck = new ArrayList<>();

    private List<Player> players = new ArrayList<>();

    public static void main(String[] args) {
        Main game = new Main();
    }

    public void setUpDecks(){
        // Setting up adventure deck
        addFoes();
        addWeapons();

        // Setting up event deck
        addQuestCards();
        addEventCards();
    }

    public void initPlayers(){

    }

    public void distributeAdventureCards(){

    }

    public List<AdventureCard> getAdventureDeck(){
        return adventureDeck;
    }

    public List<EventCard> getEventDeck(){
        return eventDeck;
    }

    public List<Player> getPlayers(){
        return players;
    }

    private void addAdventureCards(String type, String name, int value, int count){
        for(int i = 0; i < count; i++){
            adventureDeck.add(new AdventureCard(type, name, value));
        }
    }

    private void addEventCards(String name, int count){
        for(int i=0; i < count; i++){
            eventDeck.add(new EventCard(name));
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
        addEventCards("Q2", 3);
        addEventCards("Q3", 4);
        addEventCards("Q4", 3);
        addEventCards("Q5", 2);
    }

    private void addEventCards(){
        addEventCards("Plague", 1);
        addEventCards("Queen's Favor", 2);
        addEventCards("Prosperity", 2);
    }

}