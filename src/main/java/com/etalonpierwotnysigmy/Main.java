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

        //check if enough arguments are provided
        if (args.length < 9) {
            System.out.println("Niewystarczająca ilość argumentów, zapoznaj się z dokumentacją");
            return;
        }

        // change type of argument from String to int
        int[] intArgs = new int[5];
        intArgs[0] = Integer.parseInt(args[0]);
        intArgs[1] = Integer.parseInt(args[1]);
        intArgs[2] = Integer.parseInt(args[2]);
        intArgs[3] = Integer.parseInt(args[3]);
        intArgs[4] = Integer.parseInt(args[4]);

        // change type of argument from String to double
        double[] doubleArgs = new double[4];
        doubleArgs[0] = Double.parseDouble(args[5]);
        doubleArgs[1] = Double.parseDouble(args[6]);
        doubleArgs[2] = Double.parseDouble(args[7]);
        doubleArgs[3] = Double.parseDouble(args[8]);

        // check if provided arguments are valid and initialise variables
        // intArgs
        int y = intArgs[0] >= 3 ? intArgs[0] : -1;
        int x = intArgs[1] >= 3 ? intArgs[1] : -1;
        int i = intArgs[2] > 0 ? intArgs[2] : -1;
        boolean print = intArgs[3] == 1 && x <= 70;
        boolean save = intArgs[4] == 1;

        // doubleArgs
        double deerChance = doubleArgs[0] >= 0 && doubleArgs[0] <= 1 ? doubleArgs[0] : -1;
        double sheepChance = doubleArgs[1] >= 0 && doubleArgs[1] <= 1 ? doubleArgs[1] : -1;
        double lynxChance = doubleArgs[2] >= 0 && doubleArgs[2] <= 1 ? doubleArgs[2] : -1;
        double wolfChance = doubleArgs[3] >= 0 && doubleArgs[3] <= 1 ? doubleArgs[3] : -1;

        // end program if invalid arguments are provided
        if(y == -1||x == -1||i == -1||deerChance == -1||sheepChance == -1||lynxChance == -1||wolfChance == -1){
            System.out.println("Wprowadzono niepoprawne dane wejściowe, zapoznaj się z dokumentacją");
            return;
        }


        EcosystemSimulation simulation = new EcosystemSimulation(x, y, i, deerChance, sheepChance, lynxChance, wolfChance, print, save);
        List<File> listOfFiles = new ArrayList<>();

        simulation.run();
        listOfFiles.add(simulation.file);

        // repeat simulation
        System.out.println("Czy chcesz powtórzyć symulację [Y/N]?");
        boolean end = false;
        Scanner scanner = new Scanner(System.in);
        do {
            // check user input
            // if 'y': repeat simulation
            // else if 'n': end
            // else: repeat process

            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("y")) {

                    System.out.println("Ile chcesz wykonać powtórzeń?");
                    int repetitions = scanner.nextInt();

                    //repeats simulation given number of times
                    for (int j = 0; j < repetitions; ++j) {
                        simulation = new EcosystemSimulation(x, y, i, deerChance, sheepChance, lynxChance, wolfChance, print, save);
                        simulation.run();
                        listOfFiles.add(simulation.file);
                    }

                    end = true;

                } else if (input.equalsIgnoreCase("n"))
                    end = true;
            }
        } while (!end);
        scanner.close();

        // create .csv file with average results
        if(save){
            List<List<List<String>>> dataset = new ArrayList<>();
            for (File file : listOfFiles) {
                dataset.add(csvGenerator.read_data(file));
            }
            List<List<Double>> averageData = new ArrayList<>(csvGenerator.calculateAverage(dataset));
            csvGenerator generator = new csvGenerator("wyniki_srednia");
            generator.writeData(averageData);
        }

        System.out.println("Zakończono symulację");
        System.exit(0);
    }
}