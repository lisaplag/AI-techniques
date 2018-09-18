package assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static ArrayList<String> feat = new ArrayList<>();
    public static ArrayList<Integer> classes = new ArrayList<>();
    public static void main(String[] args) {
        File features = new File(System.getProperty("user.dir")+"/Code/src/assignment1/features.txt");
        File targets = new File(System.getProperty("user.dir")+"/Code/src/assignment1/targets.txt");
        try {
            Scanner scf = new Scanner(features);
            Scanner sct = new Scanner(targets);
            while (scf.hasNext()) {
                    feat.add(scf.nextLine()+","+sct.nextLine());
            }
            scf.close();
            sct.close();
            System.out.println(feat.get(0));
            System.out.println(feat.get(1));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
