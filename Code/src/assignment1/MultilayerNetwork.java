package assignment1;

import java.util.Random;
import java.util.Arrays;
import assignment1.ReadData;

public class MultilayerNetwork {
	
	public static void main(String[] args) {
		
		long seed = 0; //seed for possible use in Random
		Random random = new Random(seed);
		
		//Loading data and splitting it into training, validation and test set		
		double[][] inputs = ReadData.readInput();
		double[][] targets = ReadData.readTargets();
		int nExamples = inputs.length;
		int nFeatures = inputs[0].length;
		
    	int[] indices = ReadData.randomIndices(nExamples);
    	double[][][] newInputs = ReadData.splitSample(indices, inputs);
    	double[][][] newTargets = ReadData.splitSample(indices, targets);
    	
    	double[][] input = newInputs[0];
    	double[][] outputDesired = newTargets[0];    	
    	
		
    	
    	//Step 0: setting up neural network
		int inputNeurons = nFeatures; //index i
		int hiddenNeurons = nFeatures + 1; //index j
		int outputNeurons = outputDesired[0].length; //index k
		
		double[][] weightHidden = new double[inputNeurons][hiddenNeurons];
		double[][] weightOutput = new double[hiddenNeurons][outputNeurons];
		double[] thetaHidden = new double[hiddenNeurons];
		double[] thetaOutput = new double[outputNeurons];
		
		
		//Step 1: randomly initialize weights and thresholds [-0.5, 0.5]
		//hidden layer
		for (int i = 0; i < inputNeurons; i++) {
			for (int j = 0; j < hiddenNeurons; j++) {
				weightHidden[i][j] = random.nextDouble() - 0.5;	
				thetaHidden[j] = random.nextDouble() - 0.5;
			}
		}
		//output layer
		for (int j = 0; j < hiddenNeurons; j++) {
			for (int k = 0; k < outputNeurons; k++) {
				weightOutput[j][k] = random.nextDouble() - 0.5;	
				thetaOutput[k] = random.nextDouble() - 0.5;
			}
		}
		
		
		double sumSquaredErrors = Double.MAX_VALUE;
		int iterations = 0;
		int epochs = 0;
		double alpha = 0.1;
		double epsilon = 0.001;
		double[] predictions = new double[outputNeurons];
		
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
				predictions = outputFinal;
				iterations++;
			}
			
			epochs++;
            //Step 4) Repeat this process until the sumSquaredErrors is smaller or equal than epsilon.
		}
		//printing results	
		System.out.println("Weights of hidden layer: " + Arrays.deepToString(weightHidden));
		System.out.println("Weights of output layer: " + Arrays.deepToString(weightOutput));
		System.out.println("Thresholds of hidden layer: " + Arrays.toString(thetaHidden));
		System.out.println("Thresholds of output layer: " + Arrays.toString(thetaOutput));
		System.out.println("Iterations: " + iterations);
		System.out.println("Epochs: " + epochs);
		System.out.println("Error: " + sumSquaredErrors);
		System.out.println("Number in training sample: " + nExamples);
	}
}

