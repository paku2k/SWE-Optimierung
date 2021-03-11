package swe_mo.reply;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Einlesen {
    Haus[] haeuser;
    Antenne[] antennen;
    int width;
    int height;
    int numberOfBuildings;
    int numberOfAntenna;
    int reward;
    

    public void Einlesen(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner sc = new Scanner(file);

        String wUH = sc.nextLine();
        System.out.println(wUH);
        String[] WUHs = wUH.split(" ");
        width = Integer.parseInt(WUHs[0]);
        height = Integer.parseInt(WUHs[1]);

        String[] zeile2 = sc.nextLine().split(" ");
        numberOfBuildings = Integer.parseInt(zeile2[0]);
        numberOfAntenna = Integer.parseInt(zeile2[1]);
        reward = Integer.parseInt(zeile2[2]);

        haeuser = new Haus[numberOfBuildings];
        for(int i = 0; i < haeuser.length; i++){
            String[] data = sc.nextLine().split(" ");
            haeuser[i] = new Haus(Short.parseShort(data[0]), Short.parseShort(data[1]), Byte.parseByte(data[2]), Byte.parseByte(data[3]));
        }

        antennen = new Antenne[numberOfAntenna];
        for(int i = 0; i < antennen.length; i++) {
            String[] data = sc.nextLine().split(" ");
            antennen[i] = new Antenne(Short.parseShort(data[0]), Short.parseShort(data[1]));
        }
    }
}
