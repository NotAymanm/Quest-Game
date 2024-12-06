package org.example;

import java.util.List;

public class PromptNextRequest {
    private List<Player> eligiblePlayers;
    private int playerIndex;
    private Integer decision;

    // Getters and Setters
    public List<Player> getEligiblePlayers() {
        return eligiblePlayers;
    }

    public void setEligiblePlayers(List<Player> eligiblePlayers) {
        this.eligiblePlayers = eligiblePlayers;
    }

    public int getPlayerIndex() { return playerIndex; }

    public void setPlayerIndex(int currentPlayerIndex) { this.playerIndex = currentPlayerIndex; }

    public Integer getDecision() {
        return decision;
    }

    public void setDecision(Integer decision) {
        this.decision = decision;
    }
}
