package assignment1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MLNRunner {

    public static void main(String[] args){
        //Configure the network
        double alpha = 0.02;
        double epsilon = 0.017;
        int hiddenLayerSize = 1;

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
