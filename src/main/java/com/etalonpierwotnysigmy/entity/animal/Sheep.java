package com.etalonpierwotnysigmy.entity.animal;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.entity.plant.Plant;
import com.etalonpierwotnysigmy.entity.plant.Turnip;
import com.etalonpierwotnysigmy.simulation.Map;
import com.etalonpierwotnysigmy.simulation.Position;
import com.etalonpierwotnysigmy.simulation.Terrain;

public class Sheep extends Herbivore {
    public Sheep(Position position) {
        super();
        health = 50;
        maxHealth = 50;
        speed = 1;
        this.position = position;
    }

    private void findTarget(Entity[][] entityMap, Terrain[][] terrainMap) {
        super.findTargetHerbivore(entityMap, terrainMap);
        if (!(entityMap[targetPosition.getY()][targetPosition.getX()] instanceof Predator) &&
                thirst >= saturation && !metBreedingRequirements) {
            targetPosition = findEntity(entityMap, Turnip.class);
        }
    }

    @Override
    public Position findNextPosition(Entity[][] entityMap, Terrain[][] terrainMap) {
        findTarget(entityMap, terrainMap);
        return super.findNextPositionHerbivore(entityMap, terrainMap);
    }

    @Override
    public void updateStats(Entity[][] entityMap, Terrain[][] terrainMap) {
        super.updateStatsHerbivore(entityMap, terrainMap);
        if (entityMap[targetPosition.getY()][targetPosition.getX()] instanceof Turnip turnip && foundTarget) {
            if (turnip.isGrown() && saturation != maxSaturation) {
                if (turnip.isBuffed()){
                    saturation += turnip.getFoodValue();
                    double randomNumber = Math.random();
                    if (randomNumber < 0.3)
                        health += 10;
                    else if(randomNumber < 0.5)
                        speed++;
                    else if(randomNumber < 0.7){
                        saturation=maxSaturation;
                        thirst=maxThirst;
                    }
                    else if(randomNumber < 0.99)
                        sightRange+=5;
                    else
                        saturation = 0;
                    turnip.setBuffed(Math.random() < 0.2);
                }
                else {
                    saturation += turnip.getFoodValue();
                }
                turnip.setGrown(false);
                turnip.resetGrowthState();
                if (saturation > maxSaturation) saturation = maxSaturation;
            }
        }
        metBreedingRequirements = thirst > 30 && saturation > 30;
        foundTarget = false;
    }

    public void breed(Entity[][] entityMap, Terrain[][] terrainMap) {
        for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                if (Map.isInBounds(x, y, entityMap[0].length, entityMap.length)) {
                    if (entityMap[y][x] == null && terrainMap[y][x] == Terrain.GRASS) {
                        entityMap[y][x] = new Sheep(new Position(x, y));
                        entityMap[y][x].setMoved(true);
                        return;
                    }
                }
            }
        }
    }

}
