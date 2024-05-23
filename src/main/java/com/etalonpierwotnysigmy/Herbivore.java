package com.etalonpierwotnysigmy;

public abstract class Herbivore extends Animal {
    public Herbivore(){
        super();
        sightRange = 10;
        thirst = 31;
        saturation = 30;
        maxSaturation = 50;
        maxThirst = 50;
    }

    public Position findNextPosition(Entity[][] entityMap, Terrain[][] terrainMap) {
        Position targetPosition = new Position(position.getX(), position.getY());;
        if (thirst < saturation && !breedable) { // wybieranie pozycji do której zmierza roślinożerca za pomocą hierarchii
            targetPosition = findWater(terrainMap);
        }
        else if (thirst >= saturation && !breedable) {
            targetPosition = findEntity(entityMap, Plant.class);
        }
        if (breedable){
                targetPosition = findLove(entityMap);
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
        Position closestPosition;
        if (thirst < saturation) {
            closestPosition = findWater(terrainMap);
            Position differenceVector = Position.subtractPositions(closestPosition, position);
            if (Position.positionVectorLength(differenceVector) < 2) {
                thirst += 10;
                if (thirst > maxThirst) thirst = maxThirst;
            }
        }
        if (thirst >= saturation) {
            closestPosition = findEntity(entityMap, Plant.class);
            Position differenceVector = Position.subtractPositions(closestPosition, position);
            if (Position.positionVectorLength(differenceVector) < 2) {
                Plant plant = ((Plant) entityMap[closestPosition.getY()][closestPosition.getX()]);
                if (plant.isGrown() && saturation != maxSaturation) {
                    saturation += ((Plant) entityMap[closestPosition.getY()][closestPosition.getX()]).getFoodValue();
                    plant.setGrown(false);
                    plant.resetGrowthState();
                    if (saturation > maxSaturation) saturation = maxSaturation;
                }
            }
        }
        breedable = thirst > 40 && saturation > 40;
    }
}
