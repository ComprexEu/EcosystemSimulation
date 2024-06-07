package com.etalonpierwotnysigmy.simulation;

import com.etalonpierwotnysigmy.entity.Entity;
import com.etalonpierwotnysigmy.entity.animal.*;
import com.etalonpierwotnysigmy.entity.plant.Mushroom;
import com.etalonpierwotnysigmy.entity.plant.Plant;
import com.etalonpierwotnysigmy.entity.plant.Turnip;

import java.io.File;
import java.io.IOException;

public class EcosystemSimulation {
    int xSize,ySize;
    Entity[][] entityMap;
    double deerChance,sheepChance,lynxChance,wolfChance;
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
            csv = new csvGenerator("Wyniki symulacji");
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
                    if (Math.random() < 0.05)
                        entityMap[y][x] = new Turnip(new Position(x, y));
                    else if (Math.random() < 0.05)
                        entityMap[y][x] = new Mushroom(new Position(x, y));
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
                if (entityMap[y][x] instanceof Animal &&
                        entityMap[y][x].didntMove() &&
                        ((Animal) entityMap[y][x]).isBreeding(entityMap)) {
                    entityMap[y][x].setMoved(true);
                    ((Animal) entityMap[y][x]).breed(entityMap, terrainMap);
                }
            }
        }
    }

    private void moveHerbivores(Terrain[][] terrainMap) {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] != null && entityMap[y][x] instanceof Herbivore &&
                        entityMap[y][x].didntMove() &&
                        !((Herbivore) entityMap[y][x]).getBreeding()) {
                    entityMap[y][x].setMoved(true);
                    ((Herbivore) entityMap[y][x]).updatePosition(entityMap, terrainMap);
                }
            }
        }
    }

    private void movePredators(Terrain[][] terrainMap) {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] != null && entityMap[y][x] instanceof Predator &&
                        entityMap[y][x].didntMove() &&
                        !((Predator) entityMap[y][x]).getBreeding()) {
                    entityMap[y][x].setMoved(true);
                    ((Predator) entityMap[y][x]).updatePosition(entityMap, terrainMap);
                }
            }
        }
    }

    private void updateAnimalStats(Terrain[][] terrainMap) throws IOException {
        int deerCount = 0;
        int sheepCount = 0;
        int lynxCount = 0;
        int wolfCount = 0;
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] != null && entityMap[y][x] instanceof Animal) {
                    ((Animal) entityMap[y][x]).updateStats(entityMap, terrainMap);
                    if (entityMap[y][x] != null && ((Animal) entityMap[y][x]).getHealth() <= 0) {
                        entityMap[y][x] = null; // usuwanie obiektu, którego 'health' spadnie poniżej 0
                    }
                    if(entityMap[y][x] instanceof Deer)deerCount++;
                    if(entityMap[y][x] instanceof Sheep)sheepCount++;
                    if(entityMap[y][x] instanceof Lynx)lynxCount++;
                    if(entityMap[y][x] instanceof Wolf)wolfCount++;
                }
            }
        }
        if(save){
            population.put("Deer",deerCount);
            population.put("Sheep",sheepCount);
            population.put("Lynx",lynxCount);
            population.put("Wolf",wolfCount);
            csv.writeData(iteration,population);
        }
    }

    private void updatePlantGrowthState() {
        for (int y = 0; y < ySize; y++) { // wyrastanie planta
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] instanceof Plant) {
                    ((Plant) entityMap[y][x]).changeGrowthStatus();
                }
            }
        }
    }

    private void updateEntities(Terrain[][] terrainMap) throws IOException {
        resetMovement();
        breedAnimals(terrainMap);
        moveHerbivores(terrainMap);
        movePredators(terrainMap);
        // wszystkie 3 powyższe metody liczą się jako ruch
        updateAnimalStats(terrainMap);
        updatePlantGrowthState();
    }
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public void printMap(Terrain[][] terrainMap) {
        clearScreen();
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (entityMap[y][x] == null) {
                    if (terrainMap[y][x] == Terrain.GRASS) {
                        System.out.print("\033[42m");
                        System.out.print(" G");
                    }
                    else if (terrainMap[y][x] == Terrain.WATER) {
                        System.out.print("\033[44m");
                        System.out.print(" W");
                    }
                }
                else {
                    if (entityMap[y][x] instanceof Sheep) {
                        // https://stackoverflow.com/questions/4842424/list-of-ansi-color-escape-sequences
                        // \033[38;2;<r>;<g>;<b>m     #Select RGB foreground color
                        // \033[48;2;<r>;<g>;<b>m     #Select RGB background color
                        System.out.print("\033[48;2;160;160;160m");
                        if(((Sheep) entityMap[y][x]).getHealth() >= 30)
                            System.out.print(" S");
                        else if(((Sheep) entityMap[y][x]).getHealth() > 10 && ((Sheep) entityMap[y][x]).getHealth() < 30)
                            System.out.print("\033[38;2;255;255;0m S");
                        else
                            System.out.print("\033[38;2;255;0;0m S");
                    }
                    else if (entityMap[y][x] instanceof Turnip) {
                        System.out.print("\033[45m");
                        System.out.print(" T");
                    }
                    else if (entityMap[y][x] instanceof Wolf) {
                        System.out.print("\033[48;2;85;85;85m");
                        if(((Wolf) entityMap[y][x]).getHealth() >= 30)
                            System.out.print(" W");
                        else if(((Wolf) entityMap[y][x]).getHealth() > 10 && ((Wolf) entityMap[y][x]).getHealth() < 30)
                            System.out.print("\033[38;2;255;255;0m W");
                        else
                            System.out.print("\033[38;2;255;0;0m W");
                    }
                    else if (entityMap[y][x] instanceof Lynx) {
                        System.out.print("\033[48;2;252;127;0m");
                        if(((Lynx) entityMap[y][x]).getHealth() >= 30)
                            System.out.print(" L");
                        else if(((Lynx) entityMap[y][x]).getHealth() > 10 && ((Lynx) entityMap[y][x]).getHealth() < 30)
                            System.out.print("\033[38;2;255;255;0m L");
                        else
                            System.out.print("\033[38;2;255;0;0m L");
                    }
                    else if (entityMap[y][x] instanceof Mushroom) {
                        System.out.print("\033[48;2;255;85;85m");
                        System.out.print(" M");
                    }
                    else if (entityMap[y][x] instanceof Deer) {
                        System.out.print("\033[48;2;170;85;0m");
                        if(((Deer) entityMap[y][x]).getHealth() >= 30)
                            System.out.print(" D");
                        else if(((Deer) entityMap[y][x]).getHealth() > 10 && ((Deer) entityMap[y][x]).getHealth() < 30)
                            System.out.print("\033[38;2;255;255;0m D");
                        else
                            System.out.print("\033[38;2;255;0;0m D");
                    }

                }
                System.out.print(" \033[0m");
            }
            System.out.print(" \033[0m");
            System.out.println();
        }
    }
    private void calculatePopulation(){

    }
}


