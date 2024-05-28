package com.etalonpierwotnysigmy.entity.plant;

import com.etalonpierwotnysigmy.simulation.Position;

public class Turnip extends Plant {
    public Turnip(Position position){
        super();
        this.position = position;
        foodValue = 40;
        timeToRegrow = 3; // liczba iteracji symulacji, po której odrośnie
        growthState = 0;
    }

}
