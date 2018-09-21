package assignment1;

public class MLNRunner {

    public static void main(String[] args){
        //Configure the network
        double alpha = 0.02;
        double epsilon = 0.017;
        double[][] input = ReadData.readInput();
        double[][] outputDesired = ReadData.readTargets();

        MultilayerNetwork network = new MultilayerNetwork(alpha, input, outputDesired);

        //Repeat this process until the sumSquaredErrors is smaller or equal than epsilon.
        //printing results
        int iterations = 0;
        double validateError = Double.MAX_VALUE;
        
        while ((validateError > epsilon) && (iterations < 1000)) {
            //Train the network
            double trainError = network.train();

            //Validate the network
            validateError = network.validate();

            //print info
            //System.out.println("Iteration: " + iterations);
            //System.out.println("TrainError: " + trainError);
            System.out.println(trainError + " " + validateError);
            //System.out.println("--------------------------------------------------------------------------");
            iterations++;
        }

        //Test the network
        double finalError = network.test();
        System.out.println("Final TestSet error: " + finalError);
        System.out.println("Used iterations: " + iterations);

        double succesRate = network.testSetPrediction();
        System.out.println("Prediction succes rate over test set: " + succesRate*100 + "%");

        //Final prediction (for the unknown file)
        int[] results = network.predict(ReadData.readUnknown());
        
        for (int i = 0; i <results.length ; i++) {
            System.out.println("Sample " + (i + 1) + ": " + (results[i]+1));
        }

    }

}
