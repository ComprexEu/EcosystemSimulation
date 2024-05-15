package com.etalonpierwotnysigmy;

public class Map {

    Terrain[][] terrain;
    int xSize;
    int ySize;
    public Map(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        initializeTerrain();
    }
    private void initializeTerrain(){
        terrain = new Terrain[ySize][xSize];
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                terrain[i][j] = Terrain.WATER; // miejsce na logikę szumu perlina
            }
        }
    }

    public Terrain[][] getTerrain() {
        return terrain;
    }
    public void printMap() {
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                System.out.print(terrain[i][j] + " ");
            }
            System.out.println();
        }
    }
}
