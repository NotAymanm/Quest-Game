package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private List<AdventureCard> hand;
    private int shields;

    public Player(int id){
        this.id = id;
        this.hand = new ArrayList<>();
        this.shields = 0;
    }

    public int getId(){
        return id;
    }

    public int getHandSize(){
        return hand.size();
    }

    public int getShields(){
        return shields;
    }

    public void takeAdventureCard(AdventureCard card){
        hand.add(card);
    }

    public void addShields(int num){
        shields += num;
    }

    public void loseShields(int num){
        shields = Math.max(shields - num, 0);
    }

    public String toString(){
        return "Player " + id;
    }
}
