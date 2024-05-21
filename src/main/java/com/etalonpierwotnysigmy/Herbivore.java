package com.etalonpierwotnysigmy;

public abstract class Herbivore extends Animal {
    Herbivore(){
        super.setSightRange(5);
        super.setThirst(50);
        super.setSaturation(50);
    }
    void eatPlant(Plant plant){
        if(plant.isGrown()){
            super.addSaturation(plant.getFoodValue());
            plant.changeGrowthStatus();
            plant.resetGrowthState();
        }
    }
}
