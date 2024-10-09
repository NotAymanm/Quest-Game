package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private List<AdventureCard> adventureDeck = new ArrayList<>();
    private List<EventCard> eventDeck = new ArrayList<>();

    public static void main(String[] args) {
        Main game = new Main();
    }

    public void setUpDecks(){

    }

    public List<AdventureCard> getAdventureDeck(){
        return adventureDeck;
    }

    public List<EventCard> getEventDeck(){
            return eventDeck;
    }

}