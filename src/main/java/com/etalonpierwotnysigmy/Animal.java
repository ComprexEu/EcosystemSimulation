package com.etalonpierwotnysigmy;

public abstract class Animal extends Entity{
    protected int health;
    protected int maxHealth;
    protected int sightRange;
    protected int speed;
    protected int saturation;
    protected int maxSaturation;
    protected int thirst;
    protected int maxThirst;

    abstract Position findNextPosition(Entity[][] entityMap, Terrain[][] terrainMap);

    public boolean isInBounds(int x, int y, int maxSizeX, int maxSizeY) {
        return x >= 0 && y >= 0 && x < maxSizeX && y < maxSizeY;
    }
    protected Position findWater(Terrain[][] terrainMap) {
        Position closestPosition = new Position(position.getX(), position.getY());
        Position positionDifference;
        Position tempWaterPosition = new Position(position.getX(), position.getY());
        double closestPositionDistance = Double.MAX_VALUE;
        for (int y = position.getY() - getSightRange(); y < position.getY() + getSightRange(); y++) {
            for (int x = position.getX() - getSightRange(); x < position.getX() + getSightRange(); x++) {
                if (isInBounds(x, y, terrainMap[0].length, terrainMap.length)) {
                    if (terrainMap[y][x] == Terrain.WATER) {
                        tempWaterPosition.setX(x);
                        tempWaterPosition.setY(y);
                        positionDifference = Position.subtractPositions(position, tempWaterPosition);
                        double distance = Position.positionVectorLength(positionDifference);
                        if (distance < closestPositionDistance) {
                            closestPositionDistance = distance;
                            closestPosition = new Position(tempWaterPosition.getX(), tempWaterPosition.getY());
                        }
                    }
                }
            }
        }
        return closestPosition;
    }

    protected Position findPlant(Entity[][] entityMap) {
        Position closestPosition = new Position(position.getX(), position.getY());
        Position positionDifference;
        Position tempPlantPosition = new Position(position.getX(), position.getY());
        double closestPositionDistance = Double.MAX_VALUE;
        for (int y = position.getY() - getSightRange(); y < position.getY() + getSightRange(); y++) {
            for (int x = position.getX() - getSightRange(); x < position.getX() + getSightRange(); x++) {
                if (isInBounds(x, y, entityMap[0].length, entityMap.length)) {
                    if (entityMap[y][x] instanceof Plant) {
                        tempPlantPosition.setX(x);
                        tempPlantPosition.setY(y);
                        positionDifference = Position.subtractPositions(position, tempPlantPosition);
                        double distance = Position.positionVectorLength(positionDifference);
                        if (distance < closestPositionDistance) {
                            closestPositionDistance = distance;
                            closestPosition = new Position(tempPlantPosition.getX(), tempPlantPosition.getY());
                        }
                    }
                }
            }
        }
        return closestPosition;
    }



    public void move(Entity[][] entityMap, Position nextPosition) {
        entityMap[nextPosition.getY()][nextPosition.getX()] = entityMap[position.getY()][position.getX()];
        entityMap[position.getY()][position.getX()] = null;
        position = new Position(nextPosition.getX(), nextPosition.getY());
    }


    public void update(Entity[][] entityMap, Terrain[][] terrainMap) {
        Position nextPosition = findNextPosition(entityMap, terrainMap);
        if (entityMap[nextPosition.getY()][nextPosition.getX()] == null) move(entityMap, nextPosition);
    }

    public int getHealth() {
        return health;
    }

    public int getSightRange() {
        return sightRange;
    }

    public int getSpeed() {
        return speed;
    }

    public int getSatiety() {
        return saturation;
    }

    public int getThirst() {
        return thirst;
    }

    public void addSaturation(int saturation) {
        this.saturation += saturation;
    }

    public void setThirst(int thirst) {
        this.thirst = thirst;
    }

    public void setSightRange(int sightRange) {
        this.sightRange = sightRange;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }
}
