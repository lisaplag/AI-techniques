package assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
            int k = 0;
            while (scf.hasNext()) {
                    String tempString = scf.nextLine();
                    String[] parts = tempString.split(",");
                feat.addAll(Arrays.asList(parts));

            }
            scf.close();
            System.out.println(feat.get(2));
            while (sct.hasNext()) {
                classes.add(Integer.valueOf(sct.nextLine()));
            }
            sct.close();
            System.out.println(classes.get(2));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
