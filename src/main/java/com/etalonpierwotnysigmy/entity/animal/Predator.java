package com.etalonpierwotnysigmy.entity.animal;
import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.simulation.Map;
import com.etalonpierwotnysigmy.simulation.Position;
import com.etalonpierwotnysigmy.simulation.Terrain;

public abstract class Predator extends Animal {
    protected int damage;
    private Position targetPosition;
    private boolean foundTarget;

    public Predator() {
        super();
        sightRange = 10;
        thirst = 30;
        saturation = 30;
        maxSaturation = 50;
        maxThirst = 50;
    }

    @Override
    public Position findNextPosition(Entity[][] entityMap, Terrain[][] terrainMap) {
        targetPosition = position;
        if (metBreedingRequirements){
            targetPosition = findLove(entityMap);
        }
        else if (thirst < saturation) { // wybieranie pozycji do której zmierza drapieżnik za pomocą hierarchii
            targetPosition = findWater(terrainMap);
        }
        else {
            targetPosition = findEntity(entityMap, Herbivore.class);
        }
        Position potentialNewPosition = new Position(position.getX(), position.getY()); // znajdowanie następnej pozycji drapieżnika
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

    @Override
    public void updateStats(Entity[][] entityMap, Terrain[][] terrainMap) {
        if (saturation <= 0 || thirst <= 0) {
            health -= 5;
            if (health <= 0){
                return;
            }
        }
        else {
            saturation -= 3;
            thirst -= 3;
        }
        if (targetPosition == null) {
            targetPosition = position; // wyeliminowanie przypadku kiedy findNextPosition zmieniło targetPosition na potencjalnego partnera, ale on przemieścił się
        }
        if (terrainMap[targetPosition.getY()][targetPosition.getX()].equals(Terrain.WATER) && foundTarget) {
            thirst += 10;
            if (thirst > maxThirst) thirst = maxThirst;
        }
        else if (entityMap[targetPosition.getY()][targetPosition.getX()] instanceof Herbivore && foundTarget) {
            Herbivore herbivore = ((Herbivore) entityMap[targetPosition.getY()][targetPosition.getX()]);
            herbivore.setHealth(herbivore.getHealth() - damage);
            if (herbivore.getHealth() <= 0) saturation += herbivore.getSaturation();
            if (saturation > maxSaturation) saturation = maxSaturation;

        }
        metBreedingRequirements = thirst > 40 && saturation > 40;
        foundTarget = false;
    }
}
