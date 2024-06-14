package com.etalonpierwotnysigmy.entity.plant;

import com.etalonpierwotnysigmy.simulation.Position;

import java.util.Random;

public class Mushroom extends Plant {

    private boolean poisoned;
    public Mushroom(Position position){
        super();
        this.position = position;
        foodValue = 20;
        timeToRegrow = 1; // number of simulation iterations before it grows back
        growthState = 0;
        poisoned = Math.random() < 0.2;
    }

    public boolean isPoisoned() {
        return poisoned;
    }

    public void setPoisoned(boolean poisoned) {
        this.poisoned = poisoned;
    }

    @Override
    protected String getTileColor() {
        return "\033[48;2;255;85;85m";
    }

    @Override
    protected String getText() {
        return " M";
    }

    @Override
    public String getLegendRow() {
        return getTileColor() + getText() + " \033[0m - Mushroom";
    }
}
