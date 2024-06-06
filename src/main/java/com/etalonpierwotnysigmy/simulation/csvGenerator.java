package com.etalonpierwotnysigmy.simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class csvGenerator {
    private File file;

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
}
