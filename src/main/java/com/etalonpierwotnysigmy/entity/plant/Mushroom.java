package com.etalonpierwotnysigmy.entity.plant;

import com.etalonpierwotnysigmy.simulation.Position;

import java.util.Random;

public class Mushroom extends Plant {

    private boolean poisoned;
    public Mushroom(Position position){
        super();
        this.position = position;
        foodValue = 20;
        timeToRegrow = 1; // liczba iteracji symulacji, po której odrośnie
        growthState = 0;
        poisoned = Math.random() < 0.2;
    }

    public boolean isPoisoned() {
        return poisoned;
    }

    public void setPoisoned(boolean poisoned) {
        this.poisoned = poisoned;
    }
}
