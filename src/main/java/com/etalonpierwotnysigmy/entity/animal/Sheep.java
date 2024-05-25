package com.etalonpierwotnysigmy.entity.animal;

import com.etalonpierwotnysigmy.entity.animal.Herbivore;
import com.etalonpierwotnysigmy.simulation.Position;

public class Sheep extends Herbivore {
    public Sheep(Position position) {
        super();
        health = 30;
        maxHealth = 50;
        speed = 1;
        this.position = position;
    }

}
