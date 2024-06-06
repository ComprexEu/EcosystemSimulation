package com.etalonpierwotnysigmy;

import com.etalonpierwotnysigmy.simulation.EcosystemSimulation;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        int y = args.length > 0 && Integer.parseInt(args[0]) >= 3 ? Integer.parseInt(args[0]) : -1;
        int x = args.length > 1 && Integer.parseInt(args[1]) >= 3 ? Integer.parseInt(args[1]) : -1;
        int i = args.length > 2 && Integer.parseInt(args[2]) > 0 ? Integer.parseInt(args[2]) : -1;
        double deerChance = args.length > 3 && Double.parseDouble(args[3]) >= 0 && Double.parseDouble(args[3]) <= 1 ? Double.parseDouble(args[3]) : -1;
        double sheepChance = args.length > 4 && Double.parseDouble(args[4]) >= 0 && Double.parseDouble(args[4]) <= 1 ? Double.parseDouble(args[4]) : -1;
        double lynxChance = args.length > 5 && Double.parseDouble(args[5]) >= 0 && Double.parseDouble(args[5]) <= 1 ? Double.parseDouble(args[5]) : -1;
        double wolfChance = args.length > 6 && Double.parseDouble(args[6]) >= 0 && Double.parseDouble(args[6]) <= 1 ? Double.parseDouble(args[6]) : -1;

        if(args.length == 0||y == -1||x == -1||i == -1||deerChance == -1||sheepChance == -1||lynxChance == -1||wolfChance == -1){
            System.out.println("Wprowadzono niepoprawne dane wejściowe, zapoznaj się z dokumentacją");
            return;
        }
        EcosystemSimulation simulation = new EcosystemSimulation(x, y, i, deerChance, sheepChance, lynxChance, wolfChance);
        simulation.run();
    }

}