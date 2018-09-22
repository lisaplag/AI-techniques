package assignment1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MLNRunner {

    public static void main(String[] args){
        //Configure the network
        double alpha = 0.1;
        double epsilon = 0.000001;
        double[][] input = ReadData.readInput();
        double[][] outputDesired = ReadData.readTargets();

        MultilayerNetwork network = new MultilayerNetwork(alpha, input, outputDesired);

        //Repeat this process until the sumSquaredErrors is smaller or equal than epsilon.
        //printing results
        int iterations = 0;
        double validateError = Double.MAX_VALUE;
        while (validateError > epsilon) {
            //Train the network
            double trainError = network.train();

            //Validate the network
            validateError = network.validate();

            //print info
            System.out.println("Iteration: " + iterations);
            System.out.println("TrainError: " + trainError);
            System.out.println("ValidateError: " + validateError);
            System.out.println("--------------------------------------------------------------------------");
            iterations++;
        }

        //Test the network
        double finalError = network.test();
        System.out.println("Final TestSet error: " + finalError);
        System.out.println("Used iterations: " + iterations);
        network.testPrediction("test");
        network.testPrediction("validation");

        //Show confusion matrix
        int[][] matrix = network.confusionMatrix();
        System.out.println("vertical axis: targets \nhorizontal axis: actual\n    1 2 3 4 5 6 7");
        for (int i = 0; i < 7; i++) {
            System.out.print((i+1) + "| ");
            for (int j = 0; j < 7; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("\n");
        }
        //Final prediction (for the unknown file)
        int[] results = network.predict(ReadData.readUnknown());
        try {
            PrintWriter pw = new PrintWriter(System.getProperty("user.dir")+"/Code/src/assignment1/classes.txt");
            for (int i = 0; i < results.length; i++) {
                pw.write((results[i] + 1) + ",");
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
