package com.etalonpierwotnysigmy.simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class csvGenerator {
    private File file;

    public csvGenerator(String fileName) throws IOException {
        String name = fileName+".csv";
        int count = 1;
        file = new File(name);
        while(!file.createNewFile()){
            name = fileName+"_"+count+".csv";
            file = new File(name);
            count++;
        }
    }
    void write_data(int id,int numberOfLines, java.util.Map<String, Integer> population) throws IOException {
        FileWriter fw = new FileWriter(file,true);
        if(id==1)fw.write("iteration"+";"+"lynxes"+";"+"sheep"+";"+"wolves"+";"+"deer"+"\n"); //xd
        fw.write(id+";"+population.get("Lynx")+";"+population.get("Sheep")+";"+population.get("Wolf")+";"+population.get("Deer")+"\n");
        fw.close();
    }
}
