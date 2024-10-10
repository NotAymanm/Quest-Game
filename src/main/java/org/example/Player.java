package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public List<AdventureCard> getHand(){
        return hand;
    }

    public int getHandSize(){
        return hand.size();
    }

    public int getShields(){
        return shields;
    }

    public void takeAdventureCard(AdventureCard card){
        hand.add(card);
        sortHand();
    }

    public void takeAdventureCards(int count, List<AdventureCard> adventureDeck, List<AdventureCard> adventureDiscardPile){
        for(int i = 0; i < count; i++){
            if(adventureDeck.isEmpty()){
                adventureDeck.addAll(adventureDiscardPile);
                adventureDiscardPile.clear();
                Collections.shuffle(adventureDeck);
            }
            takeAdventureCard(adventureDeck.removeLast());

            //TODO: Handle more than 12 cards here
            if(getHandSize() > 12){
                adventureDiscardPile.add(discardAdventureCard(0));
            }
        }
    }

    private void sortHand(){
        Collections.sort(hand, new Comparator<AdventureCard>() {
            @Override
            public int compare(AdventureCard card1, AdventureCard card2) {
                if(card1.getType().equals("Foe") && card2.getType().equals("Weapon")){
                    return -1;
                }
                else if(card1.getType().equals("Weapon") && card2.getType().equals("Foe")){
                    return 1;
                }
                else if(card1.getType().equals(card2.getType())){
                    if(card1.getType().equals("Weapon")){
                        if(card1.getName().charAt(0) == 'S' && card2.getName().charAt(0) =='H'){
                            return -1;
                        }
                        else if(card1.getName().charAt(0) == 'H' && card2.getName().charAt(0) == 'S'){
                            return 1;
                        }
                    }
                    return Integer.compare(card1.getValue(), card2.getValue());
                }
                return 0;
            }
        });
    }

    public int numCardsToDiscard(){
        return -1;
    }

    public AdventureCard discardAdventureCard(int index){
        return hand.remove(index);
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
