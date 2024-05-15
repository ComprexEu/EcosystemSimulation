package com.etalonpierwotnysigmy;

public class EcosystemSimulation {
    int xSize;
    int ySize;
    public EcosystemSimulation(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        Map terrain = new Map(xSize, ySize);
        Terrain[][] terrainMap = terrain.getTerrain();
        terrainMap[3][4] = Terrain.GRASS;
        terrain.printMap();

    }
}
