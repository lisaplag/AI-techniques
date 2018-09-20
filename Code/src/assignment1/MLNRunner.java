package assignment1;

public class MLNRunner {

    public static void main(String[] args){
        //Configure the network
        double alpha = 0.1;
        double epsilon = 0.001;
        double[][] input = ReadData.readInput();
        double[][] outputDesired = ReadData.readTargets();

        MultilayerNetwork network = new MultilayerNetwork(alpha, epsilon, input, outputDesired);

        //Repeat this process until the sumSquaredErrors is smaller or equal than epsilon.
        //printing results
        double error= Integer.MAX_VALUE;
        while (error < epsilon) {
            //Train the network
            network.train();

            //Validate the network
            double error = network.validate();
        }

        //Test the network
        network.test()


        //Run the network on the testing set.
    }

}
