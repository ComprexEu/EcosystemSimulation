package com.etalonpierwotnysigmy;

import com.etalonpierwotnysigmy.simulation.EcosystemSimulation;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        int y = args.length > 0 ? Integer.parseInt(args[0]) : -1;
        int x = args.length > 1 ? Integer.parseInt(args[1]) : -1;
        int i = args.length > 2 ? Integer.parseInt(args[2]) : -1;
        double deerChance = args.length > 3 ? Double.parseDouble(args[3]) : -1;
        double sheepChance = args.length > 4 ? Double.parseDouble(args[4]) : -1;
        double lynxChance = args.length > 5 ? Double.parseDouble(args[5]) : -1;
        double wolfChance = args.length > 6 ? Double.parseDouble(args[6]) : -1;

        new EcosystemSimulation(x, y, i, deerChance, sheepChance, lynxChance, wolfChance);
    }

}