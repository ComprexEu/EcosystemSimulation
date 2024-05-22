package com.etalonpierwotnysigmy;

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
            updateEntities(terrainMap);
            Thread.sleep(2000);
        }
    }

    private void spawnEntities(Terrain[][] terrainMap) {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (terrainMap[y][x] == Terrain.GRASS) {
                    if (Math.random() < 0.1) entityMap[y][x] = new Sheep(new Position(x, y)); // for testing only
                    else if (Math.random() < 0.1) entityMap[y][x] = new Turnip(new Position(x, y));
                }
            }
        }
    }


    private void updateEntities(Terrain[][] terrainMap) {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] != null) {
                    entityMap[y][x].setUpdated(false);
                }
            }
        }
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] instanceof Animal && !entityMap[y][x].isUpdated()) {
                    entityMap[y][x].setUpdated(true);
                    ((Animal) entityMap[y][x]).updatePosition(entityMap, terrainMap);
                }
            }
        }
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] instanceof Animal) {
                    ((Animal) entityMap[y][x]).updateStats(entityMap, terrainMap);
                }
            }
        }
        System.out.println();
    }

    public void printMap(Terrain[][] terrainMap) {
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
                }
            }
            System.out.println();
        }
    }

}


