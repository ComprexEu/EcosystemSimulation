package com.etalonpierwotnysigmy.entity.animal;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.simulation.Map;
import com.etalonpierwotnysigmy.simulation.Position;
import com.etalonpierwotnysigmy.simulation.Terrain;

public abstract class Animal extends Entity {
    protected int health;
    protected int maxHealth;
    protected int sightRange;
    protected int speed;
    protected int saturation;
    protected int maxSaturation;
    protected int thirst;
    protected int maxThirst;
    protected Gender gender;
    protected boolean metBreedingRequirements;
    protected boolean breeding;

    public Animal(){
        if (Math.random() < 0.5)
            gender = Gender.MALE;
        else
            gender = Gender.FEMALE;

        metBreedingRequirements = false;
    }
    abstract Position findNextPosition(Entity[][] entityMap, Terrain[][] terrainMap);

    public abstract void updateStats(Entity[][] entityMap, Terrain[][] terrainMap);

    protected abstract Animal spawnAnimal(Position position);

    public void breed(Entity[][] entityMap, Terrain[][] terrainMap) {
        for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {

            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {

                if (Map.isInBounds(x, y, entityMap[0].length, entityMap.length)) {

                    if (entityMap[y][x] == null && terrainMap[y][x] == Terrain.GRASS) {
                        entityMap[y][x] = spawnAnimal(new Position(x, y));
                        entityMap[y][x].setMoved(true);
                        return;
                    }
                }
            }
        }
    }
    enum SearchType {
        WATER,
        ENTITY,
        LOVE
    }

    protected Position findClosest(Terrain[][] terrainMap, Entity[][] entityMap, Class<?> entityType, SearchType searchType) {
        Position closestPosition = null; // finding nearest need
        Position tempPosition = new Position(position.getX(), position.getY());
        double closestPositionDistance = sightRange;

        for (int y = position.getY() - sightRange; y < position.getY() + sightRange; y++) {

            for (int x = position.getX() - sightRange; x < position.getX() + sightRange; x++) {

                if(Map.isInBounds(
                                x,
                                y,
                                terrainMap != null ? terrainMap[0].length : entityMap[0].length,
                                terrainMap != null ? terrainMap.length : entityMap.length)
                ){
                    boolean isValid = false;

                    switch (searchType) {
                        case WATER:
                            assert terrainMap != null;
                            isValid = terrainMap[y][x] == Terrain.WATER;
                            break;
                        case ENTITY:
                            isValid = entityMap[y][x] != null && entityType.isInstance(entityMap[y][x]);
                            break;
                        case LOVE:
                            isValid = entityMap[y][x] != null &&
                                    entityMap[y][x].getClass().equals(entityMap[position.getY()][position.getX()].getClass()) &&
                                    ((Animal) entityMap[y][x]).metBreedingRequirements() &&
                                    ((Animal) entityMap[y][x]).getGender() != gender;
                            break;
                    }
                    if (isValid) {
                        tempPosition.setX(x);
                        tempPosition.setY(y);

                        Position positionDifference = Position.subtractPositions(position, tempPosition);

                        double distance = Position.positionVectorLength(positionDifference);

                        if (distance <= closestPositionDistance && distance > 0) {
                            closestPositionDistance = distance;
                            closestPosition = new Position(tempPosition.getX(), tempPosition.getY());
                        }
                    }
                }
            }
        }
        return closestPosition;
    }
    protected Position findWater(Terrain[][] terrainMap) {
        return findClosest(terrainMap, null, null, SearchType.WATER);
    }

    protected Position findEntity(Entity[][] entityMap, Class<?> entityType) {
        return findClosest(null, entityMap, entityType, SearchType.ENTITY);
    }

    protected Position findLove(Entity[][] entityMap) { // metoda do znajdywania najbliższego partnera
        return findClosest(null, entityMap, null, SearchType.LOVE);
    }

    public boolean isBreeding(Entity[][] entityMap) {
        if (metBreedingRequirements && gender == Gender.FEMALE) {
            Position closestPosition = findLove(entityMap);

            if (closestPosition != null) {
                Position differenceVector = Position.subtractPositions(closestPosition, position);

                if (Position.positionVectorLength(differenceVector) < 2 &&
                        Position.positionVectorLength(differenceVector) > 0 &&
                        ((Animal) entityMap[closestPosition.getY()][closestPosition.getX()]).metBreedingRequirements()) {
                    return breeding = true;
                }
            }
        }
        return breeding = false;
    }

    public void move(Entity[][] entityMap, Position nextPosition) { // method used to reposition entity
        entityMap[nextPosition.getY()][nextPosition.getX()] = entityMap[position.getY()][position.getX()];
        entityMap[position.getY()][position.getX()] = null;
        position = new Position(nextPosition.getX(), nextPosition.getY());
    }

    public void updatePosition(Entity[][] entityMap, Terrain[][] terrainMap) { // poruszenie animala na następne miejsce
        for (int i = 0; i < speed; i++) {

            Position nextPosition = findNextPosition(entityMap, terrainMap);

            if (entityMap[nextPosition.getY()][nextPosition.getX()] == null) move(entityMap, nextPosition);
        }
    }

    @Override
    protected String getTextColor() {
        if (health <= 10)
            return "\033[38;2;255;0;0m";

        else if (health <= 30)
            return "\033[38;2;255;255;0m";

        else return "\033[38;2;188;190;196m";
    }

    public boolean getBreeding() {
        return breeding;
    }

    public int getHealth() {
        return health;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean metBreedingRequirements() {
        return metBreedingRequirements;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
