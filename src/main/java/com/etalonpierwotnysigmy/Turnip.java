package com.etalonpierwotnysigmy;

import java.util.Random;

public class Turnip extends Plant{
    Turnip(Position position){
        super();
        this.position = position;
        setFoodValue(30);
        setTimeToRegrow(3);   // liczba iteracji symulacji, po której odrośnie
    }

}
