package org.example;

import java.io.PrintWriter;
import java.util.*;

public class Player {
    private int id;
    private List<AdventureCard> hand;
    private int shields;
    private boolean isSponsor;
    private List<Set<AdventureCard>> stages;

    public Player(int id){
        this.id = id;
        this.hand = new ArrayList<>();
        this.shields = 0;
        this.isSponsor = false;
        stages = new ArrayList<>();
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

    public void setAsSponsor(){
        isSponsor = true;
    }

    public void sponsorCard(Scanner input, PrintWriter output, EventCard eventCard){

        if(!isSponsor) return;

        int numStages = Character.getNumericValue(eventCard.getName().charAt(1));

        for(int i = 0; i < numStages; i++) {
            boolean quit = false;
            Set<AdventureCard> stageCards = new HashSet<>();

            while (!quit) {
                output.print("Sponsor's hand (" + this + "): ");
                output.flush();
                printList(hand, output);
                output.println("Which card would you like to use for Stage " + (i + 1) + "? (Enter Index), or type 'Quit' to stop: ");
                output.flush();

                String indexInput = input.nextLine();

                if (indexInput.equalsIgnoreCase("quit")) {
                    output.println("Stage " + (i + 1) + " building complete.\n");
                    output.flush();
                    quit = true;
                } else {
                    try {
                        int index = Integer.parseInt(indexInput);
                        if (index >= 0 && index < hand.size()) {
                            AdventureCard sponsoredCard = hand.remove(index);
                            output.println("You selected: " + sponsoredCard.getName());
                            stageCards.add(sponsoredCard);
                        } else {
                            output.println("Invalid card number. Try again.");
                            output.flush();
                        }
                    } catch (NumberFormatException e) {
                        output.println("Invalid input. Please enter a card number or 'Quit'");
                        output.flush();
                    }

                }
            }

            stages.add(stageCards);
            stageCards.clear();
        }

    }

    public boolean sponsorQuest(Scanner input, PrintWriter output){
        output.print("P" + id + "'s Cards: "); output.flush();
        printList(hand, output);
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

    public boolean canSponsor(EventCard currentEvent){
        if(!currentEvent.getType().equals("Quest")) return false;

        List<AdventureCard> foeCards = new ArrayList<>();
        List<AdventureCard> weaponCards = new ArrayList<>();

        for(AdventureCard card: hand){
            if(card.getType().equals("Foe")){
                foeCards.add(card);
            }
            else if(card.getType().equals("Weapon")){
                weaponCards.add(card);
            }
        }

        int numStages = Character.getNumericValue(currentEvent.getName().charAt(1));

        int lastStageValue = 0;
        for(int i = 0; i < numStages; i++){
            if(foeCards.isEmpty()) return false;

            int currentStageValue = foeCards.getFirst().getValue();
            foeCards.removeFirst();

            Set<String> usedWeaponTypes = new HashSet<>();

            while(currentStageValue <= lastStageValue && !weaponCards.isEmpty()){
                AdventureCard weapon = weaponCards.getFirst();
                if(!usedWeaponTypes.contains(weapon.getName())){
                    currentStageValue += weapon.getValue();
                    usedWeaponTypes.add(weapon.getName());
                    weaponCards.removeFirst();
                }
                else{
                    weaponCards.removeFirst();
                }
            }

            usedWeaponTypes.clear();

            if(currentStageValue <= lastStageValue){
                return false;
            }

            lastStageValue = currentStageValue;
        }

        return true;
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
