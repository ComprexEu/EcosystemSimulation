package com.etalonpierwotnysigmy.simulation;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.entity.animal.*;
import com.etalonpierwotnysigmy.entity.plant.Mushroom;
import com.etalonpierwotnysigmy.entity.plant.Plant;
import com.etalonpierwotnysigmy.entity.plant.Turnip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EcosystemSimulation {
    int xSize,ySize;
    Entity[][] entityMap;
    double deerChance, sheepChance, lynxChance, wolfChance;
    int deerCount = 0;
    int sheepCount = 0;
    int lynxCount = 0;
    int wolfCount = 0;
    int mushroomCount = 0;
    int turnipCount = 0;
    protected int iteration,maxIteration;
    private final Terrain[][] terrainMap;
    private csvGenerator csv;
    private boolean print,save;
    public File file;
    public java.util.Map<String, Integer> population;
    public EcosystemSimulation(int xSize, int ySize, int maxIteration, double deerChance, double sheepChance, double lynxChance, double wolfChance, boolean print, boolean save) throws IOException {
        this.xSize = xSize;
        this.ySize = ySize;
        this.maxIteration = maxIteration;
        this.deerChance = deerChance;
        this.sheepChance = sheepChance;
        this.lynxChance = lynxChance;
        this.wolfChance = wolfChance;
        entityMap = new Entity[ySize][xSize];
        Map terrain = new Map(xSize, ySize);
        terrainMap = terrain.getTerrain();
        spawnEntities(terrainMap);
        this.print = print;
        this.save = save;
        if(save){
            population = new java.util.HashMap<>();
            csv = new csvGenerator("wyniki");
            file = csv.getFile();
        }
    }
    public void run() throws InterruptedException, IOException {
        iteration = 1;

        while (iteration <= maxIteration) {

            if(print){
                printMap(terrainMap);
                System.out.println();
            }
            updateEntities(terrainMap);
            iteration++;

            if(print)Thread.sleep(1000);
        }
    }
    private void spawnEntities(Terrain[][] terrainMap) {
        for (int y = 0; y < ySize; y++) {

            for (int x = 0; x < xSize; x++) {

                if (terrainMap[y][x] == Terrain.GRASS) {

                    if (Math.random() < 0.05) {
                        entityMap[y][x] = new Turnip(new Position(x, y));
                        turnipCount++;
                    }

                    else if (Math.random() < 0.05) {
                        entityMap[y][x] = new Mushroom(new Position(x, y));
                        mushroomCount++;
                    }

                    else if (y < ySize / 2 && x < xSize / 2 && Math.random() < sheepChance) entityMap[y][x] =
                            new Sheep(new Position(x, y));

                    else if (y < ySize / 2 && x > xSize / 2 && Math.random() < wolfChance)
                        entityMap[y][x] = new Wolf(new Position(x, y));

                    else if (y >= ySize / 2 && x >= xSize / 2 && Math.random() < deerChance)
                        entityMap[y][x] = new Deer(new Position(x, y));

                    else if (y >= ySize / 2 && x < xSize / 2 && Math.random() < lynxChance)
                        entityMap[y][x] = new Lynx(new Position(x, y));
                }
            }
        }
    }

    private void resetMovement() {
        for (int y = 0; y < ySize; y++) {

            for (int x = 0; x < xSize; x++) {

                if (entityMap[y][x] != null) {
                    entityMap[y][x].setMoved(false);
                }
            }
        }
    }

    private void breedAnimals(Terrain[][] terrainMap) {
        for (int y = 0; y < ySize; y++) {

            for (int x = 0; x < xSize; x++) {

                if(
                        entityMap[y][x] instanceof Animal &&
                        entityMap[y][x].didntMove() &&
                        ((Animal) entityMap[y][x]).isBreeding(entityMap)
                ){
                    entityMap[y][x].setMoved(true);
                    ((Animal) entityMap[y][x]).breed(entityMap, terrainMap);
                }
            }
        }
    }

    private void moveHerbivores(Terrain[][] terrainMap) {
        for (int y = 0; y < ySize; y++) {

            for (int x = 0; x < xSize; x++) {

                if(
                        entityMap[y][x] != null && entityMap[y][x] instanceof Herbivore &&
                        entityMap[y][x].didntMove() &&
                        !((Herbivore) entityMap[y][x]).getBreeding()
                ){
                    entityMap[y][x].setMoved(true);
                    ((Herbivore) entityMap[y][x]).updatePosition(entityMap, terrainMap);
                }
            }
        }
    }

    private void movePredators(Terrain[][] terrainMap) {
        for (int y = 0; y < ySize; y++) {

            for (int x = 0; x < xSize; x++) {

                if(
                        entityMap[y][x] != null && entityMap[y][x] instanceof Predator &&
                        entityMap[y][x].didntMove() &&
                        !((Predator) entityMap[y][x]).getBreeding()
                ){
                    entityMap[y][x].setMoved(true);
                    ((Predator) entityMap[y][x]).updatePosition(entityMap, terrainMap);
                }
            }
        }
    }

    private void updateAnimalStats(Terrain[][] terrainMap) throws IOException {

        for (int y = 0; y < ySize; y++) {

            for (int x = 0; x < xSize; x++) {

                if (entityMap[y][x] != null && entityMap[y][x] instanceof Animal) {
                    ((Animal) entityMap[y][x]).updateStats(entityMap, terrainMap);

                    if (entityMap[y][x] != null && ((Animal) entityMap[y][x]).getHealth() <= 0) {
                        entityMap[y][x] = null; // kill animal
                    }

                    // count animals
                    if (entityMap[y][x] instanceof Deer) deerCount++;
                    if (entityMap[y][x] instanceof Sheep) sheepCount++;
                    if (entityMap[y][x] instanceof Lynx) lynxCount++;
                    if (entityMap[y][x] instanceof Wolf) wolfCount++;
                }
            }
        }

        //create .csv with data
        if(save){
            population.put("Deer", deerCount);
            population.put("Sheep", sheepCount);
            population.put("Lynx", lynxCount);
            population.put("Wolf", wolfCount);
            csv.writeData(iteration, population);
        }
    }

    private void updatePlantGrowthState() {
        for (int y = 0; y < ySize; y++) {

            for (int x = 0; x < xSize; x++) {

                if (entityMap[y][x] instanceof Plant) {
                    ((Plant) entityMap[y][x]).changeGrowthStatus();
                }
            }
        }
    }

    private void updateEntities(Terrain[][] terrainMap) throws IOException {
        resetMovement();

        // all 3 methods below count as a move
        breedAnimals(terrainMap);
        moveHerbivores(terrainMap);
        movePredators(terrainMap);

        updateAnimalStats(terrainMap);

        updatePlantGrowthState();
    }
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void printLegend(int currentRow) {

        ArrayList<Entity> legendClasses = new ArrayList<>();
        legendClasses.add(new Sheep(new Position(0, 0)));
        legendClasses.add(new Deer(new Position(0, 0)));
        legendClasses.add(new Wolf(new Position(0, 0)));
        legendClasses.add(new Lynx(new Position(0, 0)));
        legendClasses.add(new Turnip(new Position(0, 0)));
        legendClasses.add(new Mushroom(new Position(0, 0)));

        if (currentRow < 6) {
            String legendRow = legendClasses.get(currentRow).getLegendRow();
            System.out.print(legendRow);
        }

        if (currentRow == 6)
            System.out.print("\033[42m G \033[0m - Grass");
        if (currentRow == 7)
            System.out.print("\033[44m W \033[0m - Water");
        if (currentRow == 8)
            System.out.print("\033[38;2;255;255;0m medium hp \033[0m");
        if (currentRow == 9)
            System.out.print("\033[38;2;255;0;0m low hp \033[0m");
    }
    public void printMap(Terrain[][] terrainMap) {
        clearScreen();
        int currentRow = 0;
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {

                if (entityMap[y][x] == null) {
                    if (terrainMap[y][x] == Terrain.GRASS)
                        System.out.print("\033[42m G");
                    else if (terrainMap[y][x] == Terrain.WATER)
                        System.out.print("\033[44m W");
                }
                else {
                    entityMap[y][x].printEntity();
                }
                System.out.print(" \033[0m");
            }
            System.out.print(" \033[0m");

            // map legend
            printLegend(currentRow);
            currentRow++;

            System.out.println();
        }
    }

}


