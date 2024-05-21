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
        terrain = new Terrain[xSize][ySize];
        double yOff = 0;
        for (int i = 0; i < ySize; i++) {
            double xOff = 0;
            for (int j = 0; j < xSize; j++) {
                double x = generator.evaluateNoise(xOff,yOff);
                if(x>=-0.2) terrain[j][i] = Terrain.GRASS;
                else terrain[j][i] = Terrain.WATER;
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
