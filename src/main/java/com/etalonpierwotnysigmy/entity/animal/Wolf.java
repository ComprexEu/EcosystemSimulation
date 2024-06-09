package com.etalonpierwotnysigmy.entity.animal;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.entity.plant.Turnip;
import com.etalonpierwotnysigmy.simulation.Map;
import com.etalonpierwotnysigmy.simulation.Position;
import com.etalonpierwotnysigmy.simulation.Terrain;

import java.util.Random;

public class Wolf extends Predator {
    private int rabies;

    public Wolf(Position position) {
        super();
        damage = 20;
        health = 50;
        maxHealth = 50;
        speed = 3;
        this.position = position;
        rabies = 0;
    }
    public void findTarget(Entity[][] entityMap, Terrain[][] terrainMap) {
        super.findTargetPredator(entityMap, terrainMap);
        if(thirst >= saturation && !findingLove && !findingWater && rabies == 3){
            targetPosition = findEntity(entityMap, Animal.class);
            if (targetPosition != null) findingAnimal = true;
        }
        if (saturation >= thirst && !findingLove && !findingWater && !findingAnimal){
            targetPosition = findEntity(entityMap, Herbivore.class);
            if (targetPosition != null) findingAnimal = true;
        }
        if (targetPosition == null){
            Random x = new Random();
            targetPosition = new Position(x.nextInt(terrainMap[0].length),x.nextInt(terrainMap.length));
        }
    }

    @Override
    public Position findNextPosition(Entity[][] entityMap, Terrain[][] terrainMap) {
        findTarget(entityMap, terrainMap);
        return super.findNextPositionPredator(entityMap, terrainMap);
    }

    @Override
    public void updateStats(Entity[][] entityMap, Terrain[][] terrainMap) {
        super.updateStatsPredator(entityMap);
        if (rabies < 3 && Math.random() < 0.1) rabies++;
    }

    public void breed(Entity[][] entityMap, Terrain[][] terrainMap) {
        for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                if (Map.isInBounds(x, y, entityMap[0].length, entityMap.length)) {
                    if (entityMap[y][x] == null && terrainMap[y][x] == Terrain.GRASS) {
                        entityMap[y][x] = new Wolf(new Position(x, y));
                        entityMap[y][x].setMoved(true);
                        return;
                    }
                }
            }
        }
    }

    public boolean hasRabies() {
        return rabies == 3;
    }
}
