package com.etalonpierwotnysigmy;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator;

import java.util.Random;

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
        PerlinNoiseGenerator generator=  PerlinNoiseGenerator.newBuilder().setSeed(1231).setInterpolation(Interpolation.COSINE).setFadeFunction(FadeFunction.QUINTIC_POLY).build();
        terrain = new Terrain[ySize][xSize];
        Random random = new Random();
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                double randomValue = random.nextDouble(-5,15);
                double x = generator.evaluateNoise(i,(double)j/randomValue);
                if(x>=-0.15) terrain[i][j] = Terrain.GRASS;
                else terrain[i][j] = Terrain.WATER;
            }
        }
    }

    public Terrain[][] getTerrain() {
        return terrain;
    }
    public void printMap() {
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                if(terrain[i][j]==Terrain.GRASS)System.out.print("\u001B[32m");
                else if(terrain[i][j]==Terrain.WATER)System.out.print("\u001B[34m");
                System.out.print(terrain[i][j] + " \u001B[0m");
            }
            System.out.println();
        }
    }
}
