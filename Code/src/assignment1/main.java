package assignment1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class main {

    public static void main(String[] args){
        //Configure the network
        double alpha = 0.02;
        double epsilon = 0.017;
        int hiddenLayerSize = 11;

        double[][] input = ReadData.readInput();
        double[][] outputDesired = ReadData.readTargets();

        MultilayerNetwork network = new MultilayerNetwork(hiddenLayerSize, alpha, input, outputDesired);

        //Repeat this process until the sumSquaredErrors is smaller or equal than epsilon.
        //printing results
        int epochs = 0;
        double validateError = Double.MAX_VALUE;
        
        while ((validateError > epsilon) && (epochs < 1000)) {
            //Train the network
            double trainError = network.train();

            //Validate the network
            validateError = network.validate();

            //print info of the current epoch
            System.out.println("Epoch: " + epochs);
            System.out.println("TrainError: " + trainError);
            System.out.println("ValidateError: " + validateError);
            System.out.println("--------------------------------------------------------------------------");
            epochs++;
        }

        //Test the network using the test set
        double finalError = network.test();
        System.out.println("Final TestSet error: " + finalError);
        System.out.println("Total number of epochs: " + epochs);
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
        int class1 = 1;
        int class2 = 2;
        int class3 = 3;
        int[] predictions = network.predict(ReadData.readUnknown());
        intArrayToString(predictions);
        ArrayList<Integer> resultset1 = network.productsOfClass( class1, class2, class3, predictions);
        System.out.println(resultset1.toString());
        int[] results = alToArray(resultset1);
        ReadData.writeResults(results, predictions);
      //  ReadData.writeResults(results);
    }

    public static int[] alToArray(ArrayList<Integer> list) {
        int[] result = new int[list.size()];
        for (int i = 0; i < result.length ; i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public static void intArrayToString(int[] list) {
        System.out.print("[");
        for (int i = 0; i < list.length - 1; i++) {
            System.out.print(list[i] + ", ");
        }
        System.out.println(list[list.length - 1] + "]");
    }

}
