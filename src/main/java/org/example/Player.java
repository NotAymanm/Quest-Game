package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private List<AdventureCard> hand;

    public Player(int id){
        this.id = id;
        this.hand = new ArrayList<>();
    }

    public int getId(){
        return id;
    }

    public int getHandSize(){
        return hand.size();
    }

    public void takeAdventureCard(AdventureCard card){
        hand.add(card);
    }

    public void addShields(int num){
    }

    public String toString(){
        return "Player " + id;
    }
}
