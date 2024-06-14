package com.etalonpierwotnysigmy.entity.animal;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.simulation.Map;
import com.etalonpierwotnysigmy.simulation.Position;
import com.etalonpierwotnysigmy.simulation.Terrain;

import java.util.Random;

public class Lynx extends Predator{
    public Lynx(Position position) {
        super();
        damage = 15;
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

        // lynx's defense against rabies
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

    @Override
    protected Lynx spawnAnimal(Position position) {
        return new Lynx(position);
    }
}
