package com.etalonpierwotnysigmy.entity.animal;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.simulation.Map;
import com.etalonpierwotnysigmy.simulation.Position;
import com.etalonpierwotnysigmy.simulation.Terrain;

public class Lynx extends Predator{
    public Lynx(Position position) {
        super();
        damage = 5;
        health = 35;
        maxHealth = 35;
        speed = 3;
        this.position = position;
    }
    public void findTarget(Entity[][] entityMap, Terrain[][] terrainMap) {
        super.findTargetPredator(entityMap, terrainMap);
        if (thirst >= saturation && !findingLove && !findingWater){
            targetPosition = findEntity(entityMap, Herbivore.class);
            if (targetPosition != null) findingAnimal = true;
        }
        if (targetPosition == null) targetPosition = position;
    }

    @Override
    public Position findNextPosition(Entity[][] entityMap, Terrain[][] terrainMap) {
        findTarget(entityMap, terrainMap);
        return super.findNextPositionPredator(entityMap, terrainMap);
    }

    @Override
    public void updateStats(Entity[][] entityMap, Terrain[][] terrainMap) {
        super.updateStatsPredator(entityMap);
        // obrona lynxa przed wścieklizną
        for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                if (Map.isInBounds(x, y, terrainMap[0].length, terrainMap.length)) {
                    if (entityMap[y][x] instanceof Wolf && ((Wolf) entityMap[y][x]).hasRabies()) {
                        ((Wolf) entityMap[y][x]).setHealth(((Wolf) entityMap[y][x]).getHealth() - damage);
                    }
                }
            }
        }
    }

    public void breed(Entity[][] entityMap, Terrain[][] terrainMap) {
        for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                if (Map.isInBounds(x, y, entityMap[0].length, entityMap.length)) {
                    if (entityMap[y][x] == null && terrainMap[y][x] == Terrain.GRASS) {
                        entityMap[y][x] = new Lynx(new Position(x, y));
                        entityMap[y][x].setMoved(true);
                        return;
                    }
                }
            }
        }
    }
}
