package org.example;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<AdventureCard> getAttack() { return attack; }

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

    public String buildAttack(List<Integer> cardIndices){
        List<AdventureCard> attack = new ArrayList<>();

        cardIndices.sort((a, b) -> Integer.compare(b, a));

        String output;

        for(Integer index : cardIndices){
            if(index >= 0 && index < hand.size()){
                attack.add(hand.get(index));
            }
        }

        // Cant have a foe in an attack
        if (attack.stream().anyMatch(card -> "Foe".equals(card.getType()))) {
            return "Sorry, you can't use a foe to attack.";
        }
        // Cant have repeated weapons
        if(attack.stream().map(AdventureCard::getName).distinct().count() < attack.size()){
            return "Sorry, you can't have repeated weapon cards in an attack.";
        }

        output = "Attack is valid. " + this + "'s attack building complete.";

        hand.removeAll(attack);
        this.attack.addAll(attack);
        attack.clear();

        return output;
    }

    public String buildStage(int stageNumber, List<Integer> selectedCardIndices){
        List<AdventureCard> stageCards = new ArrayList<>();

        // Sort the indices in descending order
        selectedCardIndices.sort((a, b) -> Integer.compare(b, a));

        String output;

        for(int index : selectedCardIndices){
            if (index >= 0 && index < hand.size()) {
                stageCards.add(hand.get(index));
            }
        }

        //Checks if there is more than 1 foe
        if(stageCards.stream().filter(card -> "Foe".equals(card.getType())).count() > 1){
            return "Sorry, you can't have more than ONE foe per stage.";
        }
        //checks if there is duplicate cards
        if(stageCards.stream().map(AdventureCard::getName).distinct().count() < stageCards.size()){
            return "Sorry, you can't have repeated cards per stage.";
        }

        output = processStageBuilding(stageCards, stageNumber);
        if(!output.contains("Stage is valid.")) return output;

        for(int index : selectedCardIndices){
            hand.remove(index);
        }

        stages.add(new ArrayList<>(stageCards));
        stageCards.clear();

        return output;
    }

    private String processStageBuilding(List<AdventureCard> stageCards, int stageNumber){
        if(stageCards.isEmpty()){
            return "A stage cannot be empty. You must add at least one card.";
        }
        if (stageCards.stream().noneMatch(card -> "Foe".equals(card.getType()))) {
            return "Need a foe to complete stage.";
        }
        if(!prevValueCurValue(stageCards).isEmpty()){
            if(!(prevValueCurValue(stageCards).get(0) < prevValueCurValue(stageCards).get(1))){
                return "Insufficient value for this stage. The value must be greater than the previous stage.";
            }
        }

        return "Stage is valid. Stage " + (stageNumber + 1) + " building complete.";
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

    public boolean canSponsor(EventCard currentEvent){
        if(!currentEvent.getType().equals("Quest")) return false;

        // Extract Foe and Weapon cards
        List<AdventureCard> foeCards = hand.stream()
                .filter(card -> "Foe".equals(card.getType())) // Filter cards of type "Foe"
                .collect(Collectors.toList()); // Collect to List

        List<AdventureCard> weaponCards = hand.stream()
                .filter(card -> "Weapon".equals(card.getType()))
                .collect(Collectors.toList());

        int numStages = Character.getNumericValue(currentEvent.getName().charAt(1));

        return canBuildStages(foeCards, weaponCards, numStages, 0);
    }

    // Try to build a specific number of stages
    public boolean canBuildStages(List<AdventureCard> foes, List<AdventureCard> weapons, int numStages, int prevStageValue) {

        //Get value of weapons
        List<Integer> weaponValues = weapons.stream()
                .map(AdventureCard::getValue)  // Map each AdventureCard to its value
                .collect(Collectors.toList());  // Collect the results into a List

        // Generate all possible weapon sums
        List<Map.Entry<Integer, List<Integer>>> weaponSums = getAllSums(weaponValues);

        // Try selecting numStages where each stage has a greater value than the last
        return checkStage(foes, weapons, weaponSums, numStages, prevStageValue);
    }

    // Recursive function to try building numStages with available weapon sums and foes
    public boolean checkStage(
            List<AdventureCard> foes,
            List<AdventureCard> weapons,
            List<Map.Entry<Integer, List<Integer>>> weaponSums,
            int numStages,
            int prevStageValue) {

        // Base case: if we need 0 stages left, we've built all required stages
        if (numStages == 0) {
            return true;
        }

        // For each Foe card, try to build a stage using the remaining available weapon sums
        for(int i = 0; i < foes.size(); i++) {
            AdventureCard foe = foes.get(i);

            // Try different combinations of weapon sums, ensuring no weapon is reused
            for (Map.Entry<Integer, List<Integer>> entry : weaponSums) {
                int sum = entry.getKey();
                List<Integer> subset = entry.getValue();

                // Calculate the total value for the current stage (foe + weapon sum)
                int stageValue = foe.getValue() + sum;

                // if stage is value return
                if(stageValue > prevStageValue){
                    // Updates previous stage value
                    prevStageValue = stageValue;

                    // Remove used foe card
                    foes.remove(foe);

                    // Remove used weapon cards
                    for(int usedCard : subset){
                        for (int j = 0; j < weapons.size(); j++) {
                            if(weapons.get(i).getValue() == usedCard){
                                weapons.remove(weapons.get(i));
                                break;
                            }
                        }
                    }

                    return canBuildStages(foes, weapons, numStages-1, prevStageValue);
                }
            }
        }

        // If no valid combination was found, return false
        return false;
    }

    public List<Map.Entry<Integer, List<Integer>>> getAllSums(List<Integer> values) {
        List<Map.Entry<Integer, List<Integer>>> sumsWithValues = new ArrayList<>();
        int n = values.size();

        // Iterate through all subsets
        for (int i = 0; i < (1 << n); i++) {
            int sum = 0;
            List<Integer> subset = new ArrayList<>();

            // Generate the subset and calculate the sum
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    sum += values.get(j);
                    subset.add(values.get(j));
                }
            }

            // Store the sum and its corresponding subset
            sumsWithValues.add(new AbstractMap.SimpleEntry<>(sum, subset));
        }

        return sumsWithValues;
    }

    public void takeAdventureCard(AdventureCard card){
        hand.add(card);
        sortHand(hand);
    }

    public void takeAdventureCards(int count, List<AdventureCard> adventureDeck, List<AdventureCard> adventureDiscardPile){
        for(int i = 0; i < count; i++){
            if(adventureDeck.isEmpty()){
                adventureDeck.addAll(adventureDiscardPile);
                adventureDiscardPile.clear();
                Collections.shuffle(adventureDeck);
            }
            AdventureCard drawnCard = adventureDeck.remove(adventureDeck.size()-1);
            takeAdventureCard(drawnCard);

        }
    }

    public void drawAdventureCards(int count, List<AdventureCard> adventureDeck, List<AdventureCard> adventureDiscardPile) {
        takeAdventureCards(count, adventureDeck, adventureDiscardPile);

        // Trigger discard if needed
        if (numCardsToDiscard() > 0) {
            GameController.markPlayerNeedsToDiscard(this, numCardsToDiscard());
        }
    }

    public Boolean discardCard(List<Integer> selectedCardIndices, List<AdventureCard> adventureDiscardPile) {
        //if user didn't select the exact number of cards to discard return
        if(selectedCardIndices.size() != numCardsToDiscard()){
            return false;
        }

        // Sort the indices in descending order
        selectedCardIndices.sort((a, b) -> Integer.compare(b, a));

        for(int index : selectedCardIndices){
            if (index >= 0 && index < hand.size()) {
                AdventureCard card = hand.remove(index);
                adventureDiscardPile.add(card);
                sortHand(hand);
            }
        }

        return true;
    }

    public void sortHand(List<AdventureCard> hand){
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


    @Override
    public String toString(){
        return "Player " + id;
    }
}
