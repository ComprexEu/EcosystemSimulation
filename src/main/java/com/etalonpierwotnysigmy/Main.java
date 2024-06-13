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

        // change type of argument from String to int / double and initialize variables
        int y = Integer.parseInt(args[0]);
        int x = Integer.parseInt(args[1]);
        int i = Integer.parseInt(args[2]);
        boolean print = Integer.parseInt(args[3]) == 1;
        boolean save = Integer.parseInt(args[4]) == 1;

        double deerChance = Double.parseDouble(args[5]);
        double sheepChance = Double.parseDouble(args[6]);
        double lynxChance = Double.parseDouble(args[7]);
        double wolfChance = Double.parseDouble(args[8]);

        // check if provided arguments are valid
        boolean validArgs = true;
        if(
                y < 3 ||                                //map can't be smaller than 3x3 tiles
                x < 3 ||                                //^
                i < 1 ||                                //simulation must have at least one iteration
                (print && x > 70) ||                    //printing is only available if width of map is smaller or equal 70
                deerChance < 0 ||deerChance > 1 ||      //spawn rates must be between 0 and 1
                sheepChance < 0 || sheepChance > 1 ||   //^
                lynxChance < 0 || lynxChance > 1 ||     //^
                wolfChance < 0 || wolfChance > 1        //^
        ){
            validArgs = false;
        }

        // end program if any of the arguments is invalid
        if(!validArgs){
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
            //creates a List and adds data from created files to it
            List<List<List<String>>> dataset = new ArrayList<>();
            for (File file : listOfFiles) {
                dataset.add(csvGenerator.read_data(file));
            }

            //creates List of data and fills it with average values
            List<List<Double>> averageData = new ArrayList<>(csvGenerator.calculateAverage(dataset));

            //creates .csv and writes the data
            csvGenerator generator = new csvGenerator("wyniki_srednia");
            generator.writeData(averageData);
        }

        System.out.println("Zakończono symulację");
        System.exit(0);
    }
}