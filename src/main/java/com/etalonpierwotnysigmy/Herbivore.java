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
        if (thirst < saturation && thirst < 30) { // wybieranie pozycji do której zmierza roślinożerca za pomocą hierarchii
            targetPosition = findWater(terrainMap);
        }
        else if (thirst > saturation && saturation < 30) {
            targetPosition = findEntity(entityMap, Plant.class);
        }
        if(breedable){
            if(this instanceof Sheep)
                targetPosition = findEntity(entityMap, Sheep.class);
        }
        Position potentialNewPosition = new Position(position.getX(), position.getY()); // znajdowanie następnej pozycji roślinożercy
        Position positionDifference;
        Position newPosition = new Position(position.getX(), position.getY());
        double closestPositionDistance = Double.MAX_VALUE;
        for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                if (Map.isInBounds(x, y, terrainMap[0].length, terrainMap.length)) {
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

    public void updateStats(Entity[][] entityMap, Terrain[][] terrainMap) {
        if (saturation == 0 || thirst == 0) {
            health -= 5;
            if (health <= 0){
                return;
            }
        }
        else {
            saturation --;
            thirst --;
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
                if (((Plant) entityMap[closestPosition.getY()][closestPosition.getX()]).isGrown()) {
                    saturation += ((Plant) entityMap[closestPosition.getY()][closestPosition.getX()]).getFoodValue();
                    if (saturation > maxSaturation) saturation = maxSaturation;
                }
            }
        }
        breedable = thirst > 40 && saturation > 40;
    }
}
