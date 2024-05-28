package com.etalonpierwotnysigmy.simulation;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.entity.animal.*;
import com.etalonpierwotnysigmy.entity.plant.Mushroom;
import com.etalonpierwotnysigmy.entity.plant.Plant;
import com.etalonpierwotnysigmy.entity.plant.Turnip;

import java.sql.SQLOutput;

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
            Thread.sleep(100);
        }
    }

    private void spawnEntities(Terrain[][] terrainMap) {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (terrainMap[y][x] == Terrain.GRASS) {
                    if (y < ySize / 2 && x < xSize / 2 && Math.random() < 0.2) entityMap[y][x] =
                            new Sheep(new Position(x, y)); // for testing only
                    else if (y < ySize / 2 && x > xSize / 2 && Math.random() < 0.2)
                        entityMap[y][x] = new Wolf(new Position(x, y));
                    else if (y >= ySize / 2 && x >= xSize / 2 && Math.random() < 0.15)
                        entityMap[y][x] = new Deer(new Position(x, y));
                    else if (y >= ySize / 2 && x < xSize / 2 && Math.random() < 0.1)
                        entityMap[y][x] = new Lynx(new Position(x, y));
                    else if (Math.random() < 0.01)
                        entityMap[y][x] = new Sheep(new Position(x, y));
                    else if (Math.random() < 0.01)
                        entityMap[y][x] = new Deer(new Position(x, y));
                    else if (Math.random() < 0.01)
                        entityMap[y][x] = new Wolf(new Position(x, y));
                    else if (Math.random() < 0.01)
                        entityMap[y][x] = new Lynx(new Position(x, y));
                    else if (Math.random() < 0.05)
                        entityMap[y][x] = new Mushroom(new Position(x, y));
                    else if (Math.random() < 0.05)
                        entityMap[y][x] = new Turnip(new Position(x, y));
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
                        ((Animal) entityMap[y][x]).isBreeding(entityMap)) {
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
                    if (terrainMap[y][x] == Terrain.GRASS) {
                        System.out.print("\033[42m");
                        System.out.print(" G");
                    }
                    else if (terrainMap[y][x] == Terrain.WATER) {
                        System.out.print("\033[44m");
                        System.out.print(" W");
                    }
                }
                else {
                    if (entityMap[y][x] instanceof Sheep) {
                        // https://stackoverflow.com/questions/4842424/list-of-ansi-color-escape-sequences
                        // \033[38;2;<r>;<g>;<b>m     #Select RGB foreground color
                        // \033[48;2;<r>;<g>;<b>m     #Select RGB background color
                        System.out.print("\033[48;2;160;160;160m");
                        System.out.print(" S");
                    }
                    else if (entityMap[y][x] instanceof Turnip) {
                        System.out.print("\033[45m");
                        System.out.print(" T");
                    }
                    else if (entityMap[y][x] instanceof Wolf) {
                        System.out.print("\033[48;2;85;85;85m");
                        System.out.print(" W");
                    }
                    else if (entityMap[y][x] instanceof Lynx) {
                        System.out.print("\033[48;2;252;127;0m");
                        System.out.print(" L");
                    }
                    else if (entityMap[y][x] instanceof Mushroom) {
                        System.out.print("\033[48;2;255;85;85m");
                        System.out.print(" M");
                    }
                    else if (entityMap[y][x] instanceof Deer) {
                        System.out.print("\033[48;2;170;85;0m");
                        System.out.print(" D");
                    }

                }
                System.out.print(" \u001B[0m");
            }
            System.out.println();
        }
    }

}


