package com.etalonpierwotnysigmy.entity.animal;

import com.etalonpierwotnysigmy.simulation.Map;
import com.etalonpierwotnysigmy.simulation.Position;
import com.etalonpierwotnysigmy.simulation.Terrain;
import com.etalonpierwotnysigmy.entity.Entity;

public abstract class Herbivore extends Animal {
    protected Position targetPosition;
    protected boolean foundTarget;
    protected boolean findingPredator;
    protected boolean findingLove;
    protected boolean findingWater;
    protected boolean findingPlant;
    public Herbivore(){
        super();
        sightRange = 7;
        thirst = 30;
        saturation = 30;
        maxSaturation = 50;
        maxThirst = 50;
    }

    protected void findTargetHerbivore(Entity[][] entityMap, Terrain[][] terrainMap) {
        // finding target based on hierarchy of animal needs
        findingLove = false;
        findingPlant = false;
        findingWater = false;
        findingPredator = false;
        targetPosition = findEntity(entityMap, Predator.class);
        if (targetPosition != null) findingPredator = true;
        if (metBreedingRequirements && !findingPredator) {
            targetPosition = findLove(entityMap);
            if (targetPosition != null) findingLove = true;
        }
        if (thirst < saturation && !findingPredator && !findingLove) {
            targetPosition = findWater(terrainMap);
            if (targetPosition != null) findingWater = true;
        }
        // next conditions separately for individual species
    }

    protected Position findNextPositionHerbivore(Entity[][] entityMap, Terrain[][] terrainMap) {
        // finding the next herbivore position (best field out of 9 possible)
        Position potentialNewPosition = new Position(position.getX(), position.getY());
        Position positionDifference;
        Position newPosition = new Position(position.getX(), position.getY());
        double closestPositionDistance = Double.MAX_VALUE;
        double furthestPositionDistance = -1;
        for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                if (Map.isInBounds(x, y, terrainMap[0].length, terrainMap.length)) {
                    if (terrainMap[y][x] == Terrain.GRASS &&
                            (entityMap[y][x] == null || entityMap[y][x] == entityMap[position.getY()][position.getX()])) {
                        potentialNewPosition.setX(x);
                        potentialNewPosition.setY(y);
                        positionDifference = Position.subtractPositions(targetPosition, potentialNewPosition);
                        double distance = Position.positionVectorLength(positionDifference);
                        if (entityMap[targetPosition.getY()][targetPosition.getX()] instanceof Predator) {
                            if (distance > furthestPositionDistance) {
                                furthestPositionDistance = distance;
                                newPosition = new Position(potentialNewPosition.getX(), potentialNewPosition.getY());
                                if (distance < 2 && distance > 0) {
                                    foundTarget = true;
                                }
                            }
                        }
                        else {
                            if (distance < closestPositionDistance) {
                                closestPositionDistance = distance;
                                newPosition = new Position(potentialNewPosition.getX(), potentialNewPosition.getY());
                                if (distance < 2 && distance > 0) {
                                    foundTarget = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return newPosition;
    }

    protected void updateStatsHerbivore() {
        if (saturation <= 0 || thirst <= 0) {
            health -= 5;
            if (health <= 0){
                return;
            }
        }
        else {
            saturation -= 2;
            thirst -= 2;
        }
        if (findingWater && foundTarget) {
            thirst += 10;
            if (thirst > maxThirst) thirst = maxThirst;
        }
        if (metBreedingRequirements)
            health += 5;
    }
}
