package com.etalonpierwotnysigmy;


public class EcosystemSimulation {
    int xSize;
    int ySize;
    Entity[][] entityMap;
    public EcosystemSimulation(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        entityMap = new Entity[ySize][xSize];
        Map terrain = new Map(xSize, ySize);
        Terrain[][] terrainMap = terrain.getTerrain();
        spawnEntities(terrainMap);
        printMap(terrainMap);
    }

    private void spawnEntities(Terrain[][] terrainMap) {
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                if (terrainMap[i][j] == Terrain.GRASS) {
                    if (Math.random() < 0.1) entityMap[i][j] = new Sheep(new Position(i, j)); // for testing only
                    else if (Math.random() < 0.1) entityMap[i][j] = new Turnip(new Position(i, j));
                }
            }
        }
    }

    public void printMap(Terrain[][] terrainMap) {
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                if (entityMap[i][j] == null) {
                    if (terrainMap[i][j] == Terrain.GRASS) System.out.print("\u001B[32m");
                    else if (terrainMap[i][j] == Terrain.WATER) System.out.print("\u001B[34m");
                    System.out.print(terrainMap[i][j] + " \u001B[0m");
                }
                else {
                    if (entityMap[i][j] instanceof Sheep) {
                        System.out.print("\u001B[0m");
                        System.out.print("SHEEP ");
                    }
                    else if (entityMap[i][j] instanceof Turnip) {
                        System.out.print("\u001B[35m");
                        System.out.print("TURNIP ");
                    }
                }
            }
            System.out.println();
        }
    }

}


