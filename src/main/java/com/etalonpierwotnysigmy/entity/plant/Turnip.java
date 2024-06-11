package com.etalonpierwotnysigmy.entity.plant;

import com.etalonpierwotnysigmy.simulation.Position;

public class Turnip extends Plant {
    private boolean buffed;
    public Turnip(Position position){
        super();
        this.position = position;
        foodValue = 40;
        timeToRegrow = 3; // number of simulation iterations before it grows back
        growthState = 0;
        buffed = Math.random() < 0.1;
    }
    public boolean isBuffed() {
        return buffed;
    }
    public void setBuffed(boolean buffed) {
        this.buffed = buffed;
    }
}
