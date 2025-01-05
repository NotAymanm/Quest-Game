package org.example;

import java.util.List;

public class DetermineEligibleRequest {
    private int stage;
    private List<Player> participants;

    // Getters and Setters
    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public List<Player> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Player> participants) {
        this.participants = participants;
    }
}


