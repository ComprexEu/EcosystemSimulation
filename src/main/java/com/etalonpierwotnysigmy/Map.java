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
        Random rand = new Random();
        PerlinNoiseGenerator generator=  PerlinNoiseGenerator.newBuilder().setSeed(rand.nextInt()).setInterpolation(Interpolation.COSINE).setFadeFunction(FadeFunction.QUINTIC_POLY).build();
        terrain = new Terrain[ySize][xSize];
        double yOff = 0;
        for (int y = 0; y < ySize; y++) {
            double xOff = 0;
            for (int x = 0; x < xSize; x++) {
                double v = generator.evaluateNoise(xOff,yOff);
                if(v >= -0.2) terrain[y][x] = Terrain.GRASS;
                else terrain[y][x] = Terrain.WATER;
                xOff+=0.2;
            }
            yOff+=0.2;
        }
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public Terrain[][] getTerrain() {
        return terrain;
    }

}
