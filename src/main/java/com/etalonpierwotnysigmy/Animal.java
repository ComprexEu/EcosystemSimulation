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
    protected Gender gender;
    protected boolean breedable;

    Animal(){
        if (Math.random() < 0.5) gender = Gender.MALE;
        else gender = Gender.FEMALE;
        breedable = false;
    }
    abstract Position findNextPosition(Entity[][] entityMap, Terrain[][] terrainMap);

    abstract void updateStats(Entity[][] entityMap, Terrain[][] terrainMap);

    enum SearchType {
        WATER,
        ENTITY,
        LOVE
    }

    protected Position findClosest(Terrain[][] terrainMap, Entity[][] entityMap, Class<?> entityType, SearchType searchType) {
        Position closestPosition = new Position(position.getX(), position.getY()); // znajdowanie najbliższego zapotrzebowania
        Position tempPosition = new Position(position.getX(), position.getY());
        double closestPositionDistance = sightRange;
        for (int y = position.getY() - sightRange; y < position.getY() + sightRange; y++) {
            for (int x = position.getX() - sightRange; x < position.getX() + sightRange; x++) {
                if (Map.isInBounds(x, y, terrainMap != null ? terrainMap[0].length : entityMap[0].length,
                        terrainMap != null ? terrainMap.length : entityMap.length)) {
                    boolean isValid = false;

                    switch (searchType) {
                        case WATER:
                            isValid = terrainMap[y][x] == Terrain.WATER;
                            break;
                        case ENTITY:
                            isValid = entityType.isInstance(entityMap[y][x]);
                            break;
                        case LOVE:
                            isValid = entityMap[y][x] != null && entityMap[y][x].getClass().equals(entityMap[position.getY()][position.getX()].getClass()) &&
                                    ((Animal) entityMap[y][x]).isBreedable() && ((Animal) entityMap[y][x]).getGender() != gender;
                            break;
                    }
                    if (isValid) {
                        tempPosition.setX(x);
                        tempPosition.setY(y);
                        Position positionDifference = Position.subtractPositions(position, tempPosition);
                        double distance = Position.positionVectorLength(positionDifference);
                        if (distance <= closestPositionDistance) {
                            closestPositionDistance = distance;
                            closestPosition = new Position(tempPosition.getX(), tempPosition.getY());
                        }
                    }
                }
            }
        }
        return closestPosition;
    }
    protected Position findWater(Terrain[][] terrainMap) { // metoda do znajdywania najbliższej wody
        return findClosest(terrainMap, null, null, SearchType.WATER);
    }

    protected Position findEntity(Entity[][] entityMap, Class<?> entityType) { // metoda do znajdywania najbliższego wybranego przez nas entity
        return findClosest(null, entityMap, entityType, SearchType.ENTITY);
    }

    protected Position findLove(Entity[][] entityMap) { // metoda do znajdywania najbliższego partnera
        return findClosest(null, entityMap, null, SearchType.LOVE);
    }

    public void breed(Entity[][] entityMap, Terrain[][] terrainMap) { // chwiliwo tylko dla sheepów
        if (breedable && gender == Gender.FEMALE) {
            Position closestPosition;
            closestPosition = findLove(entityMap);
            Position differenceVector = Position.subtractPositions(closestPosition, position);
            if (Position.positionVectorLength(differenceVector) < 2 &&
                    ((Animal)entityMap[closestPosition.getY()][closestPosition.getX()]).isBreedable()) {
                for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
                    for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                        if (Map.isInBounds(x, y, entityMap[0].length, entityMap.length)) {
                            if (entityMap[y][x] == null && terrainMap[y][x] == Terrain.GRASS) {
                                entityMap[y][x] = new Sheep(new Position(x, y));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void move(Entity[][] entityMap, Position nextPosition) { // logika odpowiadająca za zmianę pozycji
        entityMap[nextPosition.getY()][nextPosition.getX()] = entityMap[position.getY()][position.getX()];
        entityMap[position.getY()][position.getX()] = null;
        position = new Position(nextPosition.getX(), nextPosition.getY());
    }

    public void updatePosition(Entity[][] entityMap, Terrain[][] terrainMap) { // poruszenie animala na następne miejsce
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

    public Gender getGender() {
        return gender;
    }

    public boolean isBreedable() {
        return breedable;
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
