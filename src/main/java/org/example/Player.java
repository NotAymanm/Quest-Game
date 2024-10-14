package org.example;

import java.io.PrintWriter;
import java.util.*;

public class Player {
    private int id;
    private List<AdventureCard> hand;
    private int shields;
    private boolean isSponsor;
    private List<List<AdventureCard>> stages;
    private List<AdventureCard> attack;

    public Player(int id){
        this.id = id;
        this.hand = new ArrayList<>();
        this.shields = 0;
        this.isSponsor = false;
        stages = new ArrayList<>();
        attack = new ArrayList<>();
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

    public List<List<AdventureCard>> getStages(){
        return stages;
    }

    public void setAsSponsor(){
        isSponsor = true;
    }

    public void removeSponsor(){
        isSponsor = false;
    }

    public boolean isSponsor() {
        return isSponsor;
    }

    public void clearStages(){
        stages.clear();
    }

    public List<AdventureCard> buildAttack(Scanner input, PrintWriter output){
        output.println(this + "'s attack building Started.");
        boolean quit = false;
        while(!quit){
            output.print(this + "'s hand: "); output.flush();
            printList(hand, output);
            output.println("Which card would you like to include in your attack? (Enter Index), or 'Quit' to stop: "); output.flush();
            String indexInput = input.nextLine();
            quit = processAttackInput(indexInput, output);
        }
        return attack;
    }

    public boolean processAttackInput(String indexInput, PrintWriter output){
        if (indexInput.equalsIgnoreCase("quit")) {
            output.print("Attack is valid. Cards used in this attack: ");
            printList(attack, output);
            output.println(this + "'s attack building complete.\n"); output.flush();
            return true;
        }
        return handleAttackSelection(indexInput, output);
    }

    private boolean handleAttackSelection(String indexInput, PrintWriter output){
        try {
            int index = Integer.parseInt(indexInput);
            if (index >= 0 && index < hand.size()) {
                AdventureCard selectedAttack = hand.get(index);
                if(selectedAttack.getType().equals("Foe")){
                    output.println("Sorry, you can't use a foe to attack."); output.flush();
                }
                else if (attack.stream().noneMatch(card -> selectedAttack.getName().equals(card.getName()))) {
                    output.println("You selected: " + selectedAttack.getName()); output.flush();
                    hand.remove(index);
                    attack.add(selectedAttack);
                    output.print(this + "'s current attack: "); output.flush();
                    printList(attack, output);
                } else {
                    output.println("Sorry, you can't have repeated weapon cards in an attack."); output.flush();
                }

            } else {
                output.println("Invalid card number. Try again."); output.flush();
            }
        } catch (NumberFormatException e) {
            output.println("Invalid input. Please enter a card number or 'Quit'"); output.flush();
        }
        return false;
    }

    public void sponsorCard(Scanner input, PrintWriter output, EventCard eventCard){
        if(!isSponsor) return;
        int numStages = Character.getNumericValue(eventCard.getName().charAt(1));
        for(int i = 0; i < numStages; i++){
            buildStage(input, output, i);
        }
    }

    private void buildStage(Scanner input, PrintWriter output, int stageNumber){
        output.println("Stage " + (stageNumber + 1) + " building Started.");
        boolean quit = false;
        List<AdventureCard> stageCards = new ArrayList<>();
        while(!quit){
            output.print("Sponsor's hand (" + this + "): "); output.flush();
            printList(hand, output);
            output.println("Which card would you like to use for Stage " + (stageNumber + 1) +
                    "? (Enter Index), or type 'Quit' to stop: "); output.flush();

            String indexInput = input.nextLine();
            quit = processStageBuildingInput(indexInput, stageCards, output, stageNumber);
        }

        stages.add(new ArrayList<>(stageCards));
        stageCards.clear();
    }

    private boolean processStageBuildingInput(String indexInput, List<AdventureCard> stageCards, PrintWriter output, int stageNumber){
        if (indexInput.equalsIgnoreCase("quit")) {
            if(stageCards.isEmpty()){
                output.println("A stage cannot be empty. You must add at least one card."); output.flush();
                return false;
            }
            if (stageCards.stream().noneMatch(card -> "Foe".equals(card.getType()))) {
                output.println("Need a foe to complete stage."); output.flush();
                return false;
            }
            if(!prevValueCurValue(stageCards).isEmpty()){
                if(!(prevValueCurValue(stageCards).get(0) < prevValueCurValue(stageCards).get(1))){
                    output.println("Insufficient value for this stage. The value must be greater than the previous stage."); output.flush();
                    return false;
                }
            }
            output.print("Stage is valid. Cards used in this stage: ");
            printList(stageCards, output);
            output.println("Stage " + (stageNumber + 1) + " building complete.\n"); output.flush();
            return true;
        }
        return handleStageBuildingCardSelection(indexInput, stageCards,output);
    }

    private List<Integer> prevValueCurValue(List<AdventureCard> currentStageCards){
        List<Integer> values = new ArrayList<>();

        if(stages.isEmpty()) return values;

        int currentStageValue = 0;
        for(AdventureCard card: currentStageCards){
            currentStageValue += card.getValue();
        }
        int prevStageValue = getStageValue(stages.size()-1);
        values.add(prevStageValue);
        values.add(currentStageValue);
        return values;
    }

    private int getStageValue(int index){
        int value = 0;
        for(AdventureCard card : stages.get(index)){
            value += card.getValue();
        }
        return value;
    }

    private boolean handleStageBuildingCardSelection(String indexInput, List<AdventureCard> stageCards, PrintWriter output){
        try {
            int index = Integer.parseInt(indexInput);
            if (index >= 0 && index < hand.size()) {
                AdventureCard sponsoredCard = hand.get(index);
                if(stageCards.stream().anyMatch(card -> "Foe".equals(card.getType())) && sponsoredCard.getType().equals("Foe")){
                    output.println("Sorry, you can't have more than ONE foe per stage."); output.flush();
                }
                else if (stageCards.stream().noneMatch(card -> sponsoredCard.getName().equals(card.getName()))) {
                    output.println("You selected: " + sponsoredCard.getName()); output.flush();
                    hand.remove(index);
                    stageCards.add(sponsoredCard);
                } else {
                    output.println("Sorry, you can't have repeated weapon cards per stage."); output.flush();
                }

            } else {
                output.println("Invalid card number. Try again."); output.flush();
            }
        } catch (NumberFormatException e) {
            output.println("Invalid input. Please enter a card number or 'Quit'"); output.flush();
        }
        return false;
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

            List<String> usedWeaponTypes = new ArrayList<>();

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

    public void takeAdventureCards(int count, List<AdventureCard> adventureDeck, List<AdventureCard> adventureDiscardPile, PrintWriter output){
        for(int i = 0; i < count; i++){
            if(adventureDeck.isEmpty()){
                adventureDeck.addAll(adventureDiscardPile);
                adventureDiscardPile.clear();
                Collections.shuffle(adventureDeck);
            }
            AdventureCard drawnCard = adventureDeck.removeLast();
            takeAdventureCard(drawnCard);

            if(output != null) output.println(this + " drew a: " + drawnCard.getName());
        }
    }

    public void drawAdventureCards(int count, List<AdventureCard> adventureDeck, List<AdventureCard> adventureDiscardPile){
        takeAdventureCards(count, adventureDeck, adventureDiscardPile, null);

        if(numCardsToDiscard() > 0){
            Scanner input = new Scanner(System.in);
            PrintWriter output = new PrintWriter(System.out);
            discardCards(input,output, adventureDiscardPile);
        }
    }
    public void drawAdventureCards(int count, List<AdventureCard> adventureDeck, List<AdventureCard> adventureDiscardPile, Scanner input, PrintWriter output){
        takeAdventureCards(count, adventureDeck, adventureDiscardPile, output);

        if(numCardsToDiscard() > 0){
            discardCards(input,output, adventureDiscardPile);
            clearHotseat(output);
        }
    }

    public void discardCards(Scanner input, PrintWriter output, List<AdventureCard> adventureDiscardPile){
        output.print("P" + getId() + "'s current Hand: "); output.flush();
        printList(hand, output);

        while(numCardsToDiscard() > 0){
            output.println(this + " is holding too many cards.");
            output.println("Which card you like to discard (Enter Index): "); output.flush();

            if(input.hasNextLine()){
                String indexInput = input.nextLine();
                int index = Integer.parseInt(indexInput);

                if(index >= 0 && index < getHandSize()){
                    AdventureCard card = getHand().remove(index);
                    adventureDiscardPile.add(card);
                    output.println(card.getName() + " has been removed."); output.flush();
                    output.print("P" + getId() + "'s updated Hand: "); output.flush();
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

    public void clearHotseat(PrintWriter output){
        for(int i = 0; i < 15; i++){
            output.print("\n"); output.flush();
        }
    }

    @Override
    public String toString(){
        return "P" + id;
    }
}
