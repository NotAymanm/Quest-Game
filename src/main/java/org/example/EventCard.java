package org.example;

public class EventCard {
    private String name;
    private String type;

    public EventCard(String name, String type){
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType(){
        return type;
    }

    @Override
    public String toString(){
        return name;
    }

}
