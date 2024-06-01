package com.etalonpierwotnysigmy.entity.animal;
import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.simulation.Map;
import com.etalonpierwotnysigmy.simulation.Position;
import com.etalonpierwotnysigmy.simulation.Terrain;


public abstract class Predator extends Animal {
    protected int damage;
    protected Position targetPosition;
    protected Entity targetEntity;
    protected boolean foundTarget;

    protected boolean findingLove;
    protected boolean findingWater;
    protected boolean findingAnimal;

    protected abstract void findTarget(Entity[][] entityMap, Terrain[][] terrainMap);

    public Predator() {
        super();
        sightRange = 20;
        thirst = 30;
        saturation = 30;
        maxSaturation = 50;
        maxThirst = 50;
    }
    protected void findTargetPredator(Entity[][] entityMap, Terrain[][] terrainMap) {
        // znalezienie celu w zależności od potrzeb
        targetEntity = null;
        targetPosition = null;
        findingLove = false;
        findingWater = false;
        findingAnimal = false;
        if (metBreedingRequirements){
            targetPosition = findLove(entityMap);
            if (targetPosition != null) findingLove = true;
        }
        if (thirst < saturation && !findingLove) {
            targetPosition = findWater(terrainMap);
            if (targetPosition != null) findingWater = true;
        }
        // pozostałe warunki znajdują się w klasach dla poszczególnych gatunków
    }
    public Position findNextPositionPredator(Entity[][] entityMap, Terrain[][] terrainMap) {
        // znajdowanie następnej pozycji drapieżnika (najlepsze pole spośród 9 możliwych)
        Position potentialNewPosition = new Position(position.getX(), position.getY());
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

    public void updateStatsPredator(Entity[][] entityMap, Terrain[][] terrainMap) {
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
        if (findingAnimal && foundTarget && entityMap[targetPosition.getY()][targetPosition.getX()] != null) {
            Animal animal = (Animal)entityMap[targetPosition.getY()][targetPosition.getX()];
            animal.setHealth(animal.getHealth() - damage);
            if (animal.getHealth() <= 0) {
                saturation = maxSaturation;
                    health = maxHealth;
            }
        }
        metBreedingRequirements = thirst > 40 && saturation > 40;
        if(metBreedingRequirements) {
            health += 5;
            if (health > maxHealth) maxHealth = health;
        }
        foundTarget = false;
    }

}
