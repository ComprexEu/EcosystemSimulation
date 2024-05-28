package com.etalonpierwotnysigmy.simulation;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.entity.animal.*;
import com.etalonpierwotnysigmy.entity.plant.Plant;
import com.etalonpierwotnysigmy.entity.plant.Turnip;

public class EcosystemSimulation {
    int xSize;
    int ySize;
    Entity[][] entityMap;
    public EcosystemSimulation(int xSize, int ySize) throws InterruptedException {
        this.xSize = xSize;
        this.ySize = ySize;
        entityMap = new Entity[ySize][xSize];
        Map terrain = new Map(xSize, ySize);
        Terrain[][] terrainMap = terrain.getTerrain();
        spawnEntities(terrainMap);
        Thread.sleep(2000);
        while (true) {
            printMap(terrainMap);
            System.out.println();
            updateEntities(terrainMap);
            Thread.sleep(500);
        }
    }

    private void spawnEntities(Terrain[][] terrainMap) {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (terrainMap[y][x] == Terrain.GRASS) {
                    if (Math.random() < 0.1) entityMap[y][x] = new Sheep(new Position(x, y)); // for testing only
                    else if (Math.random() < 0.1) entityMap[y][x] = new Turnip(new Position(x, y));
                    else if (Math.random() < 0.04) entityMap[y][x] = new Wolf(new Position(x, y));
                }
            }
        }
    }

    private void updateEntities(Terrain[][] terrainMap) {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] != null) {
                    entityMap[y][x].setMoved(false);
                }
            }
        }
        for (int y = 0; y < ySize; y++) { // rozmnażanie się
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] instanceof Animal &&
                        entityMap[y][x].didntMove() &&
                        ((Animal) entityMap[y][x]).isBreeding(entityMap, terrainMap)) {
                    entityMap[y][x].setMoved(true);
                    ((Animal) entityMap[y][x]).breed(entityMap, terrainMap);
                }
            }
        }
        for (int y = 0; y < ySize; y++) { // poruszenie się roślinożerców
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] != null && entityMap[y][x] instanceof Herbivore &&
                        entityMap[y][x].didntMove() &&
                        !((Herbivore) entityMap[y][x]).getBreeding()) {
                    entityMap[y][x].setMoved(true);
                    ((Herbivore) entityMap[y][x]).updatePosition(entityMap, terrainMap);
                }
            }
        }
        for (int y = 0; y < ySize; y++) { // poruszenie się drapieżników
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] != null && entityMap[y][x] instanceof Predator &&
                        entityMap[y][x].didntMove() &&
                        !((Predator) entityMap[y][x]).getBreeding()) {
                    entityMap[y][x].setMoved(true);
                    ((Predator) entityMap[y][x]).updatePosition(entityMap, terrainMap);
                }
            }
        }
        for (int y = 0; y < ySize; y++) { // zmiana statystyk po wykonaniu ruchu
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] != null && entityMap[y][x] instanceof Animal) {
                    ((Animal) entityMap[y][x]).updateStats(entityMap, terrainMap);
                    if (entityMap[y][x] != null && ((Animal) entityMap[y][x]).getHealth() <= 0) {
                        entityMap[y][x] = null; // usuwanie obiektu, którego 'health' spadnie poniżej 0
                    }
                }
            }
        }
        for (int y = 0; y < ySize; y++) { // wyrastanie planta
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] instanceof Plant) {
                    ((Plant) entityMap[y][x]).changeGrowthStatus();
                }
            }
        }
    }
    public static void clearScreen() {      // działa chyba tylko w terminalu windowsowym, potem się to wywali jak będzie gui
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public void printMap(Terrain[][] terrainMap) {
        clearScreen();
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] == null) {
                    if (terrainMap[y][x] == Terrain.GRASS) System.out.print("\u001B[32m");
                    else if (terrainMap[y][x] == Terrain.WATER) System.out.print("\u001B[34m");
                    System.out.print(terrainMap[y][x] + " \u001B[0m");
                }
                else {
                    if (entityMap[y][x] instanceof Sheep) {
                        System.out.print("\u001B[0m");
                        System.out.print("SHEEP ");
                    }
                    else if (entityMap[y][x] instanceof Turnip) {
                        System.out.print("\u001B[35m");
                        System.out.print("TURNP ");
                    }
                    else if (entityMap[y][x] instanceof Wolf) {
                        System.out.print("\u001B[33m");
                        System.out.print("WOOLF ");
                    }
                }
            }
            System.out.println();
        }
    }

}


