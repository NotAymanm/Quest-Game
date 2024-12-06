package org.example;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    // list to represent each card in the deck
    public List<AdventureCard> adventureDeck = new ArrayList<>();
    public List<EventCard> eventDeck = new ArrayList<>();

    public List<AdventureCard> adventureDiscardPile = new ArrayList<>();
    public List<EventCard> eventDiscardPile = new ArrayList<>();

    public void initDeck(){
        // Initialize AdventureCards
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

        addAdventureCards("Weapon", "D5", 5, 6); // Daggers
        addAdventureCards("Weapon", "H10", 10, 12); // Horses
        addAdventureCards("Weapon", "S10", 10, 16); // Swords
        addAdventureCards("Weapon", "B15", 15, 8); // Battle-Axes
        addAdventureCards("Weapon", "L20", 20, 6); // Lances
        addAdventureCards("Weapon", "E30", 30, 2); // Excaliburs

        // Initialize EventCards
        addEventCards("Q2", "Quest", 3);
        addEventCards("Q3", "Quest", 4);
        addEventCards("Q4", "Quest", 3);
        addEventCards("Q5", "Quest", 2);

        addEventCards("Plague", "Event", 1);
        addEventCards("Queen's Favor", "Event", 2);
        addEventCards("Prosperity", "Event", 2);

        Collections.shuffle(adventureDeck);
        Collections.shuffle(eventDeck);
    }

    public void addAdventureCards(String type, String name, int value, int count){
        for(int i = 0; i < count; i++){
            adventureDeck.add(new AdventureCard(type, name, value));
        }
    }

    public void addEventCards(String name, String type, int count){
        for(int i=0; i < count; i++){
            eventDeck.add(new EventCard(name, type));
        }
    }

    public EventCard drawNextEventCard(){
        if(eventDeck.isEmpty()){
            eventDeck.addAll(eventDiscardPile);
            eventDiscardPile.clear();
            Collections.shuffle(eventDeck);
        }

        EventCard currentEvent = eventDeck.remove(eventDeck.size()-1);
        eventDiscardPile.add(currentEvent);

        return currentEvent;
    }

    public void rigAdventureDeck(List<AdventureCard> riggedAdventure) {
        //Removes rigged Cards from deck
        for (AdventureCard riggedCard : riggedAdventure) {
            for (int i = 0; i < adventureDeck.size(); i++) {
                if (adventureDeck.get(i).getName().equals(riggedCard.getName())) {
                    adventureDeck.remove(i);
                    break; // Exit the loop after removing the first match
                }
            }
        }

        // Reverse the order of riggedAdventure before adding to the end of adventureDeck
        Collections.reverse(riggedAdventure);
        //adds rigged cards back in deck
        adventureDeck.addAll(riggedAdventure);
    }
    public void rigEventDeck(List<EventCard> riggedEvent) {
        //Removes rigged cards from deck
        for (EventCard riggedCard : riggedEvent) {
            for (int i = 0; i < eventDeck.size(); i++) {
                if (eventDeck.get(i).getName().equals(riggedCard.getName())) {
                    eventDeck.remove(i);
                    break; // Exit the loop after removing the first match
                }
            }
        }

        // Reverse the order of riggedEvent before adding to the end of eventDeck
        Collections.reverse(riggedEvent);
        //adds rigged cards back in deck
        eventDeck.addAll(riggedEvent);
    }

    public void printDecks() {
        System.out.println("Adventure Deck: " + adventureDeck);
        System.out.println("Event Deck: " + eventDeck);
    }
}
