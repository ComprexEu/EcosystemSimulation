package com.etalonpierwotnysigmy;

public abstract class Herbivore extends Animal {
    Herbivore(){
        sightRange = 10;
        thirst = 31;
        saturation = 30;
        maxSaturation = 50;
        maxThirst = 50;
    }


    public Position findNextPosition(Entity[][] entityMap, Terrain[][] terrainMap) {
        Position targetPosition = new Position(position.getX(), position.getY());;
        if (thirst < saturation) { // wybieranie pozycji do której zmierza roślinożerca za pomocą hierarchii
            targetPosition = findWater(terrainMap);
        }
        if (thirst > saturation) {
            targetPosition = findEntity(entityMap, Plant.class);
        }
        Position potentialNewPosition = new Position(position.getX(), position.getY()); // znajdowanie następnej pozycji roślinożercy
        Position positionDifference;
        Position newPosition = new Position(position.getX(), position.getY());
        double closestPositionDistance = Double.MAX_VALUE;
        for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                if (isInBounds(x, y, terrainMap[0].length, terrainMap.length)) {
                    if (terrainMap[y][x] == Terrain.GRASS && entityMap[y][x] == null) {
                        potentialNewPosition.setX(x);
                        potentialNewPosition.setY(y);
                        positionDifference = Position.subtractPositions(targetPosition, potentialNewPosition);
                        double distance = Position.positionVectorLength(positionDifference);
                        if (distance < closestPositionDistance) {
                            closestPositionDistance = distance;
                            newPosition = new Position(potentialNewPosition.getX(), potentialNewPosition.getY());
                        }
                    }
                }
            }
        }
        return newPosition;
    }
    void eatPlant(Plant plant){
        if(plant.isGrown()){
            super.addSaturation(plant.getFoodValue());
            plant.changeGrowthStatus();
            plant.resetGrowthState();
        }
    }
}
