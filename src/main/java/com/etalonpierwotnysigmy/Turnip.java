package com.etalonpierwotnysigmy;

import java.util.Random;

public class Turnip extends Plant{
    Turnip(Position position){
        super();
        this.position = position;
        foodValue = 30;
        timeToRegrow = 3; // liczba iteracji symulacji, po której odrośnie
        growthState = 0;
    }

}
