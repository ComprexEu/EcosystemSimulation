package com.etalonpierwotnysigmy;

public class EcosystemSimulation {
    int xSize;
    int ySize;
    Entity[][] entites = new Entity[xSize][ySize];
    public EcosystemSimulation(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        Map terrain = new Map(xSize, ySize);
        Terrain[][] terrainMap = terrain.getTerrain();
        terrain.printMap();

    }
}
