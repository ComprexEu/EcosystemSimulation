package com.etalonpierwotnysigmy.simulation;

import java.io.*;
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
    void write_data(int id, java.util.Map<String, Integer> population) throws IOException {
        FileWriter fw = new FileWriter(file,true);
        if(id==1)fw.write("iteration"+";"+"lynxes"+";"+"sheep"+";"+"wolves"+";"+"deer"+"\n"); //xd
        fw.write(id+";"+population.get("Lynx")+";"+population.get("Sheep")+";"+population.get("Wolf")+";"+population.get("Deer")+"\n");
        fw.close();
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
    List<List<List<Double>>> read_data_from_all_files(List<List<List<String>>> dataSet) {
        List<List<List<Double>>> averageDataSetValues = new ArrayList<>();
        List<List<Double>> averageDataValues = new ArrayList<>();
        List<Double> average = new ArrayList<>();
        for (List<List<String>> data : dataSet) {
            for (List<String> rowData : data) {
                if(!rowData.get(0).equals("iteration")){
                    for (String value : rowData) {
                        average.add(Double.parseDouble(value));
                    }
                }
                averageDataValues.add(average);
            }
            averageDataSetValues.add(averageDataValues);
        }
        return averageDataSetValues;
    }
    public List<List<String>> calculateAverage(List<List<List<String>>> dataSet){
        List<List<List<Double>>> DoubleDataSet = read_data_from_all_files(dataSet);
        List<List<String>> averageData = new ArrayList<>();
        List<String> average = new ArrayList<>();
        List<List<Double>> sumValues = new ArrayList<>();
        List<Double> sum = new ArrayList<>();
        for(List<List<Double>> data : DoubleDataSet){
            for(int i=0; i<data.size(); i++){
                for(int j=0; j<data.get(i).size(); j++){
                    if(data.equals(DoubleDataSet.get(0)))sum.add(data.get(i).get(j));
                    else {
                        sum.set(j, sum.get(j)+sumValues.get(i).get(j));
                        sumValues.set(i, sum);
                    }
                }
            }
            if(data.equals(DoubleDataSet.get(0)))sumValues.add(sum);
        }
        for(List<List<Double>> data : DoubleDataSet){
            for(List<Double> row : sumValues){
                for(Double value : row){
                    average.add(String.valueOf(value/DoubleDataSet.size()));
                }
                averageData.add(average);
            }
        }
        return averageData;
    }
}
