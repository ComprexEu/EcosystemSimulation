package com.etalonpierwotnysigmy.entity.plant;

import com.etalonpierwotnysigmy.simulation.Position;

public class Mushroom extends Plant{
    public Mushroom(Position position){
        super();
        this.position = position;
        foodValue = 20;
        timeToRegrow = 1; // liczba iteracji symulacji, po której odrośnie
        growthState = 0;
    }
}
