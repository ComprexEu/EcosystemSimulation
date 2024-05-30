package com.etalonpierwotnysigmy.entity.animal;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.entity.plant.Mushroom;
import com.etalonpierwotnysigmy.entity.plant.Plant;
import com.etalonpierwotnysigmy.simulation.Map;
import com.etalonpierwotnysigmy.simulation.Position;
import com.etalonpierwotnysigmy.simulation.Terrain;

public class Deer extends Herbivore{
    public Deer(Position position) {
        super();
        health = 40;
        maxHealth = 40;
        speed = 2;
        this.position = position;
    }

    private void findTarget(Entity[][] entityMap, Terrain[][] terrainMap) {
        super.findTargetHerbivore(entityMap, terrainMap);
        if (!(entityMap[targetPosition.getY()][targetPosition.getX()] instanceof Predator) &&
                thirst >= saturation && !metBreedingRequirements) {
            targetPosition = findEntity(entityMap, Plant.class);
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
                if (plant instanceof Mushroom && ((Mushroom) plant).isPoisoned()) {
                    saturation -= plant.getFoodValue();
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
        metBreedingRequirements = thirst > 35 && saturation > 35;
        foundTarget = false;
    }
    public void breed(Entity[][] entityMap, Terrain[][] terrainMap) {
        for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                if (Map.isInBounds(x, y, entityMap[0].length, entityMap.length)) {
                    if (entityMap[y][x] == null && terrainMap[y][x] == Terrain.GRASS) {
                        entityMap[y][x] = new Deer(new Position(x, y));
                        entityMap[y][x].setMoved(true);
                        return;
                    }
                }
            }
        }
    }
}
