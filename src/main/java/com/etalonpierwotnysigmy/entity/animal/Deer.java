package com.etalonpierwotnysigmy.entity.animal;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.entity.plant.Mushroom;
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
        if (thirst >= saturation && !findingPredator && !findingLove && !findingWater) {
            targetPosition = findEntity(entityMap, Mushroom.class);
            if (targetPosition != null) findingPlant = true;
        }
        if (targetPosition == null) targetPosition = position;
    }

    @Override
    public Position findNextPosition(Entity[][] entityMap, Terrain[][] terrainMap) {
        findTarget(entityMap, terrainMap);
        return super.findNextPositionHerbivore(entityMap, terrainMap);
    }

    @Override
    public void updateStats(Entity[][] entityMap, Terrain[][] terrainMap) {
        super.updateStatsHerbivore(entityMap, terrainMap);
        if (findingPlant && foundTarget) {
            Mushroom mushroom = (Mushroom) entityMap[targetPosition.getY()][targetPosition.getX()];
            if (mushroom.isGrown() && saturation != maxSaturation) {
                if (mushroom.isPoisoned()) {
                    saturation += mushroom.getFoodValue();
                    health -= 20;
                    mushroom.setPoisoned(Math.random() < 0.2);
                }
                else {
                    saturation += mushroom.getFoodValue();
                }
                mushroom.setGrown(false);
                mushroom.resetGrowthState();
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
                        entityMap[y][x] = new Deer(new Position(x, y));
                        entityMap[y][x].setMoved(true);
                        return;
                    }
                }
            }
        }
    }
}
