package com.etalonpierwotnysigmy.entity.animal;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.entity.plant.Mushroom;
import com.etalonpierwotnysigmy.entity.plant.Plant;
import com.etalonpierwotnysigmy.simulation.Map;
import com.etalonpierwotnysigmy.simulation.Position;
import com.etalonpierwotnysigmy.simulation.Terrain;

import java.util.Random;

public class Deer extends Herbivore{
    public Deer(Position position) {
        super();
        health = 40;
        maxHealth = 40;
        speed = 2;
        this.position = position;
    }

    @Override
    public void updateStats(Entity[][] entityMap, Terrain[][] terrainMap) {
        super.updateStatsHerbivore();

        if (findingPlant && foundTarget) {
            Plant plant = (Plant) entityMap[targetPosition.getY()][targetPosition.getX()];

            if (plant.isGrown() && saturation != maxSaturation) {

                if (plant instanceof Mushroom && ((Mushroom) plant).isPoisoned()) {
                    saturation += plant.getFoodValue();
                    health -= 20;
                    ((Mushroom) plant).setPoisoned(Math.random() < 0.2);
                }
                else {
                    saturation += plant.getFoodValue();
                }
                plant.setGrown(false);
                plant.resetGrowthState();

                if (saturation > maxSaturation) saturation = maxSaturation;
            }
        }
        metBreedingRequirements = thirst > 30 && saturation > 30;
        foundTarget = false;
    }
    @Override
    protected Deer spawnAnimal(Position position) {
        return new Deer(position);
    }

    @Override
    protected String getTileColor() {
        return "\033[48;2;170;85;0m";
    }

    @Override
    protected String getText() {
        return " D";
    }

    @Override
    public String getLegendRow() {
        return getTileColor() + getText() + " \033[0m - Deer";
    }
}
