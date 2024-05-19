package com.etalonpierwotnysigmy;

public class EcosystemSimulation {
    int xSize;
    int ySize;
    Entity[][] entityMap = new Entity[xSize][ySize];
    public EcosystemSimulation(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        Map terrain = new Map(xSize, ySize);
        Terrain[][] terrainMap = terrain.getTerrain();
        terrain.printMap();
    }

//    private void spawnEntities(Terrain[][] terrainMap) {
//        for (int i = 0; i < ySize; i++) {
//            for (int j = 0; j < xSize; j++) {
//                if (terrainMap[i][j] == Terrain.GRASS) {
//
//                }
//            }
//        }
//    }
}
