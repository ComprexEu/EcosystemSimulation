package com.etalonpierwotnysigmy;

import com.etalonpierwotnysigmy.simulation.EcosystemSimulation;
import com.etalonpierwotnysigmy.simulation.csvGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        boolean print = args.length > 7 && args[7].equals("1");
        boolean save = args.length > 8 && args[8].equals("1");
        if(args.length == 0||y == -1||x == -1||i == -1||deerChance == -1||sheepChance == -1||lynxChance == -1||wolfChance == -1){
            System.out.println("Wprowadzono niepoprawne dane wejściowe, zapoznaj się z dokumentacją");
            return;
        }
        EcosystemSimulation simulation;
        List<File> listOfFiles = new ArrayList<>();
        boolean end = false;
        Scanner scanner = new Scanner(System.in);
        do{
            simulation = new EcosystemSimulation(x, y, i, deerChance, sheepChance, lynxChance, wolfChance, print, save);
            simulation.run();
            listOfFiles.add(simulation.file);
            System.out.println("Czy chcesz powtórzyć symulację [Y/N]?");
            while(true){
                if(scanner.hasNextLine()){
                    String input = scanner.nextLine();
                    if(input.equalsIgnoreCase("y")){
                        break;
                    }else if(input.equalsIgnoreCase("n")){
                        end = true;
                    }else System.out.println(input);
                }
                if(end)break;
            }
        } while(!end);
        scanner.close();
        System.out.println("Zakończono symulację");
        List<List<List<String>>> dataset = new ArrayList<>();
        csvGenerator generator = new csvGenerator("Średnie wyniki");
        for(File file : listOfFiles){
            dataset.add(generator.read_data(file));
        }
        System.exit(0);
    }
}