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
		
		
		//Step 4: Iteration
		double sumSquaredErrors = Double.MAX_VALUE;
		int iterations = 0;
		int epochs = 0;
		double alpha = 0.1;
		double epsilon = 0.001;
		double[] predictions = new double[outputNeurons];
		
		while (sumSquaredErrors > epsilon) {
			for (int n = 0; n < nExamples; n++) {
				//Step 2: activation
				double[] outputHidden = new double[hiddenNeurons];
				double[] outputFinal = new double[outputNeurons];
				//a) outputs of hidden layer
				for (int j = 0; j < hiddenNeurons; j++) {
					double sum = 0;
					for (int i = 0; i < inputNeurons; i++) {
						sum += input[n][i] * weightHidden[i][j];
					}
					outputHidden[j] = 1 / (1 + Math.exp(thetaHidden[j] - sum));
				}
				//b) outputs of output layer
				for (int k = 0; k < outputNeurons; k++) {
					double sum = 0;
					for (int j = 0; j < hiddenNeurons; j++) {
						sum += outputHidden[j] * weightOutput[j][k];
					}
					outputFinal[k] = 1 / (1 + Math.exp(thetaOutput[k] - sum));
				}

				//Step 3: Weight training
				sumSquaredErrors = 0;
				double[] errorGradient = new double[outputNeurons];
				//output layer
				for (int k = 0; k < outputNeurons; k++) {
					double error = outputDesired[n][k] - outputFinal[k];
					errorGradient[k] = outputFinal[k] * (1 - outputFinal[k]) * error;
					sumSquaredErrors += error * error;
					thetaOutput[k] += alpha * (-1) * errorGradient[k];
					
					for (int j = 0; j < hiddenNeurons; j++) {
						weightOutput[j][k] += alpha * outputHidden[j] * errorGradient[k];
					}
				}
				//hidden layer
				for (int j = 0; j < hiddenNeurons; j++) {
					double gradientSum = 0;
					for (int k = 0; k < outputNeurons; k++) {
						gradientSum += errorGradient[k] * weightOutput[j][k];
					}
					double errorGradientHidden = outputHidden[j] * (1 - outputHidden[j]) * gradientSum;
					thetaHidden[j] += alpha * (-1) * errorGradientHidden;
					
					for (int i = 0; i < inputNeurons; i++) {
						weightHidden[i][j] += alpha * input[n][i] * errorGradientHidden;
					}
				}
				predictions = outputFinal;
				iterations++;
			}
			
			epochs++;
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

