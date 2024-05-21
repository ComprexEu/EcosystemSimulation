package com.etalonpierwotnysigmy;

public abstract class Herbivore extends Animal {
    Herbivore(){
        super.setSightRange(10);
        super.setThirst(50);
        super.setSaturation(50);
    }

    public Position findNextPosition(Entity[][] entityMap, Terrain[][] terrainMap) {
        Position targetPosition = findWater(terrainMap); // później nie tylko water tylko w zależności od potrzeb (hierarchia)
        Position potentialNewPosition = new Position(position.getX(), position.getY());
        Position positionDifference;
        Position newPosition = new Position(position.getX(), position.getY());
        double closestPositionDistance = Double.MAX_VALUE;
        for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                if (isInBounds(x, y, terrainMap[0].length, terrainMap.length)) {
                    if (y == position.getY() && x == position.getX()) continue;
                    if (terrainMap[y][x] == Terrain.GRASS) {
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
