package assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadData {

    public static ArrayList<String> feat = new ArrayList<>();
    public static ArrayList<String> targets = new ArrayList<>();

    public static double[][] readInput() {
        File features = new File(System.getProperty("user.dir")+"/Code/src/assignment1/features.txt");
        read(features, feat);
        int size = feat.size();
        double[][] input = new double[size][10];
        for (int i = 0; i < size; i++) {
            String[] parts = feat.get(i).split(",");
            for (int j = 0; j < 10; j++) {
                input[i][j] = Double.valueOf(parts[j]);
            }
        }
        return(input);
    }

    public static double[] readTargets() {
        File targetFile = new File(System.getProperty("user.dir")+"/Code/src/assignment1/targets.txt");
        read(targetFile, targets);
        int size = targets.size();
        double[] res = new double[size];
        for (int i = 0; i < size; i++) {
             res[i] = Integer.valueOf(targets.get(i));
        }
        return res;
    }

    public static void read(File file, ArrayList<String> goal) {
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                goal.add(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
