package com.etalonpierwotnysigmy.entity.animal;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.entity.plant.Turnip;
import com.etalonpierwotnysigmy.simulation.Map;
import com.etalonpierwotnysigmy.simulation.Position;
import com.etalonpierwotnysigmy.simulation.Terrain;

import java.util.Random;

public class Sheep extends Herbivore {
    public Sheep(Position position) {
        super();
        health = 50;
        maxHealth = 50;
        speed = 1;
        this.position = position;
    }

    @Override
    public void updateStats(Entity[][] entityMap, Terrain[][] terrainMap) {
        super.updateStatsHerbivore();

        if (findingPlant && foundTarget) {
            Turnip turnip = (Turnip)entityMap[targetPosition.getY()][targetPosition.getX()];

            if (turnip.isGrown() && saturation != maxSaturation) {

                if (turnip.isBuffed()){
                    saturation += turnip.getFoodValue();
                    double randomNumber = Math.random();

                    if (randomNumber < 0.3) {
                        health += 10;
                        saturation += 10;
                    }
                    else if (randomNumber < 0.5)
                        speed++;
                    else if (randomNumber < 0.7){
                        saturation = maxSaturation;
                        thirst = maxThirst;
                    }
                    else if (randomNumber < 0.99)
                        sightRange += 5;
                    else
                        saturation = 0;
                    turnip.setBuffed(Math.random() < 0.2);
                }
                else {
                    saturation += turnip.getFoodValue();
                }
                turnip.setGrown(false);
                turnip.resetGrowthState();

                if (saturation > maxSaturation)
                    saturation = maxSaturation;
            }
        }
        metBreedingRequirements = thirst > 30 && saturation > 30;
        foundTarget = false;
    }

    @Override
    protected Sheep spawnAnimal(Position position) {
        return new Sheep(position);
    }

    @Override
    protected String getTileColor() {
        return "\033[48;2;160;160;160m";
    }

    @Override
    protected String getText() {
        return " S";
    }

    @Override
    public String getLegendRow() {
        return getTileColor() + getText() + " \033[0m - Sheep";
    }


}
