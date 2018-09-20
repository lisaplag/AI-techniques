package assignment1;

import java.util.Random;
import java.util.Arrays;
import assignment1.ReadData;

public class MultilayerNetworkTest {

	public static void main(String[] args) {

		// setting up neural network
		//long seed = 0; //seed for possible use in Random
		//Random random = new Random(seed);

		// double[][] input = ReadData.readInput();
		// double[] outputDesired = ReadData.readTargets();

		double[][] input = {{1.0, 1.0}, {0.0, 1.0},  {1.0, 0.0},  {0.0, 0.0}};
		double[][] outputDesired = {{0.0}, {1.0}, {1.0}, {0.0}};
		int nExamples = input.length;
		//int nFeatures = input[0].length;

		int inputNeurons = 2; //index i
		int hiddenNeurons = 2; //index j
		int outputNeurons = 1; //index k

		//double[][] weightHidden = new double[inputNeurons][hiddenNeurons];
		//double[][] weightOutput = new double[hiddenNeurons][outputNeurons];
		//double[] thetaHidden = new double[hiddenNeurons];
		//double[] thetaOutput = new double[outputNeurons];

		//test book example on p.182
		double[][] weightHidden = {{0.5, 0.9}, {0.4, 1.0}};
		double[][] weightOutput = {{-1.2}, {1.1}};
		double[] thetaHidden = {0.8, -0.1};
	    double[] thetaOutput = {0.3};


		//Step 1: randomly initialize weights and thresholds [-0.5, 0.5]


		//Step 4: Iteration
		double sumSquaredErrors = Double.MAX_VALUE;
		int iterations = 0;
		int epochs = 0;
		double alpha = 0.1;
		double epsilon = 0.001;

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

				iterations++;
			}

			epochs++;
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

