package org.example;

import java.io.PrintWriter;
import java.util.*;

public class Player {
    private int id;
    private List<AdventureCard> hand;
    private int shields;
    private boolean isSponsor;

    public Player(int id){
        this.id = id;
        this.hand = new ArrayList<>();
        this.shields = 0;
        this.isSponsor = false;
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

    public boolean sponsorQuest(Scanner input, PrintWriter output){
        output.println("P"+ id +", do you want to sponsor this quest? (y/n):"); output.flush();
        String answer = input.nextLine();

        if(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")){
            isSponsor = true;
            return true;
        }
        else if(answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")){
            isSponsor = false;
            return false;
        }

        return false;
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
        }
    }

    public void drawAdventureCards(int count, List<AdventureCard> adventureDeck, List<AdventureCard> adventureDiscardPle){
        takeAdventureCards(count, adventureDeck, adventureDiscardPle);

        if(numCardsToDiscard() > 0){
            Scanner input = new Scanner(System.in);
            PrintWriter output = new PrintWriter(System.out);
            discardCards(input,output);
        }
    }

    public void discardCards(Scanner input, PrintWriter output){
        output.println("P" + getId() + "'s current Hand: "); output.flush();
        printList(hand, output);

        while(numCardsToDiscard() > 0){
            output.println("Which card you like to discard (Enter Index): "); output.flush();

            if(input.hasNextLine()){
                String indexInput = input.nextLine();
                int index = Integer.parseInt(indexInput);

                if(index >= 0 && index < getHandSize()){
                    AdventureCard card = getHand().remove(index);
                    output.println(card.getName() + " has been removed."); output.flush();
                    output.println("P" + getId() + "'s updated Hand: "); output.flush();
                    printList(getHand(), output);
                }
                else{
                    output.println("Please Enter a Valid Position!"); output.flush();
                }
            }
            else{
                output.println("No input available. Please Try again."); output.flush();
                break;
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
        return Math.max(0, (getHandSize() - 12));
    }

    public void addShields(int num){
        shields += num;
    }

    public void loseShields(int num){
        shields = Math.max(shields - num, 0);
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

    public String toString(){
        return "P" + id;
    }
}
