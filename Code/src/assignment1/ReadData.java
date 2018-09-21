package assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class ReadData {

    public static ArrayList<String> feat = new ArrayList<>();
    public static ArrayList<String> targets = new ArrayList<>();
    public static ArrayList<String> unknown = new ArrayList<>();

    public static double[][] readInput() {
        File features = new File(System.getProperty("user.dir")+"/src/assignment1/features.txt");
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

    public static double[][] readTargets() {
        File targetFile = new File(System.getProperty("user.dir")+"/src/assignment1/targets.txt");
        read(targetFile, targets);
        int size = targets.size();
        double category;
        double[][] targ = new double[size][7];
        for (int i = 0; i < size; i++) {
             category = Integer.valueOf(targets.get(i));
             targ[i][(int)(category - 1.0)] = 1.0; //encoding the class of the product
        }
        
        return(targ);
    }

    public static double[][] readUnknown() {
        File features = new File(System.getProperty("user.dir")+"/Code/src/assignment1/unknown.txt");
        read(features, unknown);
        int size = unknown.size();
        double[][] input = new double[size][10];
        for (int i = 0; i < size; i++) {
            String[] parts = unknown.get(i).split(",");
            for (int j = 0; j < 10; j++) {
                input[i][j] = Double.valueOf(parts[j]);
            }
        }
        return(input);
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
    
    //returns array containing indices of examples that are classified as training, validation and test set
    public static int[] randomIndices(int nExamples) { //nExamples = 7854
		long seed = 0; //seed for possible use in Random
		Random random = new Random();
		
		int[] sample = new int[nExamples];
		
		for (int n = 0; n < nExamples; n++) {
			double choice = random.nextDouble();
			if (choice < 0.7) sample[n] = 0; //70% in training set
			else if (choice >= 0.7 && choice < 0.85) sample[n] = 1; //15% in validation set
			else sample[n] = 2; //15% in test set
		}
		return(sample);
    }
    
    //splits the sample into training, validation and test set based on random indices
    public static double[][][] splitSample(int[] indices, double[][] sample) {    	
    	ArrayList<double[]> trainingSample = new ArrayList<>();
    	ArrayList<double[]> validationSample = new ArrayList<>();
    	ArrayList<double[]> testingSample = new ArrayList<>();
    	
    	int nSamples = sample.length;
    	int nFeatures = sample[0].length;
    	
    	for (int n = 0; n < nSamples; n++) {
    		if (indices[n] == 0) {
    		    trainingSample.add(sample[n]);
            } else if (indices[n] == 1) {
    		    validationSample.add(sample[n]);
            } else {
                testingSample.add(sample[n]);
    		}
    	}
    	
    	double[][] training = trainingSample.toArray(new double[trainingSample.size()][nFeatures]);
    	double[][] validation = validationSample.toArray(new double[validationSample.size()][nFeatures]);
    	double[][] testing = testingSample.toArray(new double[testingSample.size()][nFeatures]);
    	
    	double[][][] newInput = new double[3][training.length][nFeatures];
    	newInput[0] = training;
    	newInput[1] = validation;
    	newInput[2] = testing;
    	
    	return(newInput);
    }
    
    
    
    //just for testing the split sample method
    public static void main(String args[]) {
    	int[] indices = randomIndices( 7854 );
    	double[][] inputs = readInput();
    	double[][] targets = readTargets();
    	double[][][] newInputs = splitSample(indices, inputs);
    	double[][][] newTargets = splitSample(indices, targets);
    	
    	System.out.println("Testing Sample Input: " + Arrays.deepToString(newInputs[2]));
    	System.out.println("Testing Sample Size Input: " + newInputs[2].length);
    	System.out.println("Testing Sample Size Targets: " + newTargets[2].length);
    }
}
