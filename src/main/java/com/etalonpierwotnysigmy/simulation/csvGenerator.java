package com.etalonpierwotnysigmy.simulation;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class csvGenerator {
    public File file;

    public csvGenerator(String fileName) throws IOException {
        int count = 1;
        String name = fileName+" "+count+".csv";
        file = new File(name);
        while(!file.createNewFile()){
            name = fileName+" "+count+".csv";
            file = new File(name);
            count++;
        }
    }
    void writeData(int id, java.util.Map<String, Integer> population) throws IOException {
        FileWriter fw = new FileWriter(file,true);
        if(id==1)fw.write("iteration"+";"+"lynxes"+";"+"sheep"+";"+"wolves"+";"+"deer"+"\n");
        fw.write(id+";"+population.get("Lynx")+";"+population.get("Sheep")+";"+population.get("Wolf")+";"+population.get("Deer")+"\n");
        fw.close();
    }
    void writeData(List<List<Double>> data) throws IOException {
        //todo zrobić csv
    }
    public File getFile() {
        return file;
    }
    public List<List<String>> read_data(File file) throws IOException {
        List<List<String>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                data.add(Arrays.asList(values));
            }
        }
        return data;
    }
    static List<List<List<Double>>> convertStringToDouble(List<List<List<String>>> dataSet){
        List<List<List<Double>>> doubleList = new ArrayList<>();
        for(List<List<String>> outerList : dataSet) {
            List<List<Double>> outerDoubleList = new ArrayList<>();
            for(List<String> innerList : outerList) {
                if(!innerList.get(0).equals("iteration")) {
                    List<Double> innerDoubleList = new ArrayList<>();
                    for (String value : innerList) {
                        innerDoubleList.add(Double.parseDouble(value));
                    }
                    outerDoubleList.add(innerDoubleList);
                }
            }
            doubleList.add(outerDoubleList);
        }
        return doubleList;
    }
    public static List<List<Double>> calculateAverage(List<List<List<String>>> dataSet) {
        List<List<List<Double>>> DoubleDataSet = new ArrayList<>(convertStringToDouble(dataSet));
        List<List<Double>> result = new ArrayList<>();

        int outerSize = DoubleDataSet.get(0).size();
        int innerSize = DoubleDataSet.get(0).get(0).size();

        for (int i = 0; i < outerSize; i++) {
            List<Double> innerResultList = new ArrayList<>();
            for (int j = 0; j < innerSize; j++) {
                innerResultList.add(0.0);
            }
            result.add(innerResultList);
        }
        for (List<List<Double>> dataset : DoubleDataSet) {
            for (int i = 0; i < outerSize; i++) {
                for (int j = 0; j < innerSize; j++) {
                    double currentValue = dataset.get(i).get(j);
                    double updatedValue = result.get(i).get(j) + currentValue;
                    result.get(i).set(j, updatedValue);
                }
            }
        }
        int numDatasets = DoubleDataSet.size();
        for (int i = 0; i < outerSize; i++) {
            for (int j = 0; j < innerSize; j++) {
                double sumValue = result.get(i).get(j);
                double averageValue = sumValue / numDatasets;
                result.get(i).set(j, averageValue);
            }
        }
        return result;
    }
}
