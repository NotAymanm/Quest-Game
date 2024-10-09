package org.example;

public class AdventureCard {

    private String type;
    private String name;
    private int value;

    public AdventureCard(String type, String name, int value){
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        return name;
    }
}
