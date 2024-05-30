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
        if (entityMap[targetPosition.getY()][targetPosition.getX()] instanceof Plant && foundTarget) {
            Plant plant = ((Plant) entityMap[targetPosition.getY()][targetPosition.getX()]);
            if (plant.isGrown() && saturation != maxSaturation) {
                saturation += plant.getFoodValue();
                plant.setGrown(false);
                plant.resetGrowthState();
                if (saturation > maxSaturation) saturation = maxSaturation;
            }
        }
        metBreedingRequirements = thirst > 35 && saturation > 35;
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
