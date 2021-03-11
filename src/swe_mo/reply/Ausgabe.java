package swe_mo.reply;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Ausgabe {
    public Ausgabe(ArrayList<Double> eingang){
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write(eingang.size()/2 + "\n");
            for(int i = 0; i < eingang.size(); i+=2){
                myWriter.write((i/2) + " " + Math.round(eingang.get(i)) + " " + Math.round(eingang.get(i+1)) + "\n");
            }
            myWriter.close();
            System.out.println("Output erstellt!");
        } catch (IOException e){
            System.out.println("Error");
            e.printStackTrace();
            System.out.println(eingang.size()/2);
            for(int i = 0; i < eingang.size(); i+=2){
                System.out.println((i/2) + " " + Math.round(eingang.get(i)) + " " + Math.round(eingang.get(i+1)));
            }
        } 
    }
}

