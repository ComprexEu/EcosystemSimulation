package com.etalonpierwotnysigmy.entity.animal;

import com.etalonpierwotnysigmy.simulation.Map;
import com.etalonpierwotnysigmy.entity.plant.Plant;
import com.etalonpierwotnysigmy.simulation.Position;
import com.etalonpierwotnysigmy.simulation.Terrain;
import com.etalonpierwotnysigmy.entity.Entity;

public abstract class Herbivore extends Animal {
    private Position targetPosition;
    private boolean foundTarget;
    public Herbivore(){
        super();
        sightRange = 5;
        thirst = 30;
        saturation = 30;
        maxSaturation = 50;
        maxThirst = 50;
    }

    public Position findNextPosition(Entity[][] entityMap, Terrain[][] terrainMap) {
        targetPosition = position;
        if (metBreedingRequirements){
            targetPosition = findLove(entityMap);
        }
        else if (thirst < saturation) { // wybieranie pozycji do której zmierza roślinożerca za pomocą hierarchii
            targetPosition = findWater(terrainMap);
        }
        else {
            targetPosition = findEntity(entityMap, Plant.class);
        }
        Position potentialNewPosition = new Position(position.getX(), position.getY()); // znajdowanie następnej pozycji roślinożercy
        Position positionDifference;
        Position newPosition = new Position(position.getX(), position.getY());
        double closestPositionDistance = Double.MAX_VALUE;
        for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                if (Map.isInBounds(x, y, terrainMap[0].length, terrainMap.length)) {
                    if (terrainMap[y][x] == Terrain.GRASS && (entityMap[y][x] == null || entityMap[y][x] == entityMap[position.getY()][position.getX()])) {
                        potentialNewPosition.setX(x);
                        potentialNewPosition.setY(y);
                        positionDifference = Position.subtractPositions(targetPosition, potentialNewPosition);
                        double distance = Position.positionVectorLength(positionDifference);
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
        return newPosition;
    }

    public void updateStats(Entity[][] entityMap, Terrain[][] terrainMap) {
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
        if (targetPosition == null) {
            targetPosition = position; // wyeliminowanie przypadku kiedy findNextPosition zmieniło targetPosition na potencjalnego partnera, ale on przemieścił się
        }
        if (terrainMap[targetPosition.getY()][targetPosition.getX()].equals(Terrain.WATER) && foundTarget) {
            thirst += 10;
            if (thirst > maxThirst) thirst = maxThirst;
        }
        else if (entityMap[targetPosition.getY()][targetPosition.getX()] instanceof Plant && foundTarget) {
            Plant plant = ((Plant) entityMap[targetPosition.getY()][targetPosition.getX()]);
            if (plant.isGrown() && saturation != maxSaturation) {
                saturation += plant.getFoodValue();
                plant.setGrown(false);
                plant.resetGrowthState();
                if (saturation > maxSaturation) saturation = maxSaturation;
            }
        }
        metBreedingRequirements = thirst > 40 && saturation > 40;
        foundTarget = false;
    }
}