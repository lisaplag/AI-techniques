package assignment1;

import java.util.Random;
import java.util.Arrays;
import assignment1.ReadData;

public class MultilayerNetwork {
	
	public static void main(String[] args) {
		
		// setting up the neural network
		long seed = 0; //seed for possible use in Random
		Random random = new Random(seed);

		double sumSquaredErrors = Double.MAX_VALUE;
		int iterations = 0;
		int epochs = 0;
		double alpha = 0.1;
		double epsilon = 0.001;

		// specify the input values

		// double[][] input = ReadData.readInput();
		// double[] outputDesired = ReadData.readTargets();
		double[][] input = {{1.0, 1.0}, {0.0, 1.0},  {1.0, 0.0},  {0.0, 0.0}};
		double[][] outputDesired = {{0.0}, {1.0}, {1.0}, {0.0}};
		int nExamples = input.length;
		int nFeatures = input[0].length;

		//Specify the size of the three layers
		int inputNeurons = 2; //index i
		int hiddenNeurons = 2; //index j
		int outputNeurons = 1; //index k
		
		//double[][] weightHidden = new double[inputNeurons][hiddenNeurons];
		//double[][] weightOutput = new double[hiddenNeurons][outputNeurons];
		//double[] thetaHidden = new double[hiddenNeurons];
		//double[] thetaOutput = new double[outputNeurons];

		//Step 1) Initialisation

		//Set the weights and thresholds to random numbers.
		//In this case the numbers from page 181
		double[][] weightHidden = {{0.5, 0.9}, {0.4, 1.0}};
		double[][] weightOutput = {{-1.2}, {1.1}};
		double[] thetaHidden = {0.8, -0.1};
	    double[] thetaOutput = {0.3};


		while (sumSquaredErrors > epsilon) {
			for (int n = 0; n < nExamples; n++) {
				//Step 2) activation
				double[] outputHidden = new double[hiddenNeurons];
				double[] outputFinal = new double[outputNeurons];
				//2a) outputs of hidden layer

                //for each hidden neuron
                for (int j = 0; j < hiddenNeurons; j++) {
					double sum = 0; // initialize sum to 0
                    //for each input this neuron has (equals number of input neurons)
                     for (int i = 0; i < inputNeurons; i++) {
                         // multiply the input by the associated weight and add to the sum.
						sum += input[n][i] * weightHidden[i][j];
					}
					//Store the output of the hidden neutron
					outputHidden[j] = 1 / (1 + Math.exp(thetaHidden[j] - sum));
				}
				//2b) outputs of output layer

                //for each output neuron
                for (int k = 0; k < outputNeurons; k++) {
					double sum = 0;// initialize sum to 0
                    //for each input this neuron has (equals number of hidden neurons)
                    for (int j = 0; j < hiddenNeurons; j++) {
                        // multiply the input(=output of hidden neuron) by the associated weight and add to the sum.
                        sum += outputHidden[j] * weightOutput[j][k];
					}
                    //Store the output of the output neutron
                    outputFinal[k] = 1 / (1 + Math.exp(thetaOutput[k] - sum));
				}

				//Step 3) Weight training
				sumSquaredErrors = 0; //Initialize sum of the squared errors to 0
				double[] errorGradient = new double[outputNeurons]; //
				//3a) Calculate the error gradient for the output neurons

                //For each output neuron
				for (int k = 0; k < outputNeurons; k++) {
					//calculate the error
				    double error = outputDesired[n][k] - outputFinal[k];
					//calculate the error gradient
				    errorGradient[k] = outputFinal[k] * (1 - outputFinal[k]) * error;
					//Add the square of the error to the sumSquaredErrors
				    sumSquaredErrors += error * error;
					//Calculate and update the theta value of the output neuron
				    thetaOutput[k] += alpha * (-1) * errorGradient[k];

                    //Calculate the weight corrections and update the weights
                    for (int j = 0; j < hiddenNeurons; j++) {
                        weightOutput[j][k] += alpha * outputHidden[j] * errorGradient[k];
					}
				}
                //3b) Calculate the error gradient for the hidden neurons

                //For each hidden neuron
                for (int j = 0; j < hiddenNeurons; j++) {
					double gradientSum = 0; //Initialize gradientSum to 0

                    //Calculate the gradient sum
                    for (int k = 0; k < outputNeurons; k++) {
						gradientSum += errorGradient[k] * weightOutput[j][k];
					}
					//Calculate the error gradient of the hidden neuron
					double errorGradientHidden = outputHidden[j] * (1 - outputHidden[j]) * gradientSum;
                    //Calculate the theta correction and update this value
                    thetaHidden[j] += alpha * (-1) * errorGradientHidden;

                    //Calculate the weight corrections and update the weights
                    for (int i = 0; i < inputNeurons; i++) {
						weightHidden[i][j] += alpha * input[n][i] * errorGradientHidden;
					}
				}
				
				iterations++;
			}
			
			epochs++;
            //Step 4) Repeat this process until the sumSquaredErrors is smaller or equal than epsilon.
		}
			
		System.out.println("Weights of hidden layer: " + Arrays.deepToString(weightHidden));
		System.out.println("Weights of output layer: " + Arrays.deepToString(weightOutput));
		System.out.println("Thresholds of hidden layer: " + Arrays.toString(thetaHidden));
		System.out.println("Thresholds of output layer: " + Arrays.toString(thetaOutput));
		System.out.println("Iterations: " + iterations);
		System.out.println("Epochs: " + epochs);
		System.out.println("Error: " + sumSquaredErrors);
	}
}

