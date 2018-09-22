package assignment1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MLNRunner {

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

            //print info
            System.out.println("Epoch: " + epochs);
            System.out.println("TrainError: " + trainError);
            System.out.println("ValidateError: " + validateError);
            //System.out.println(trainError + " " + validateError);
            System.out.println("--------------------------------------------------------------------------");
            epochs++;
        }

        //Test the network
        double finalError = network.test();
        System.out.println("Final TestSet error: " + finalError);
        System.out.println("Used iterations: " + epochs);
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
        ReadData.writeResults(results);
    }

}
