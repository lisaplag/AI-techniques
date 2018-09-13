package assignment1;

import java.util.Random;

public class Perceptron {
	
	public static void main(String[] args) {
		// initializing parameters of the perceptron
		long seed = 0; // using a seed for more comparability of random numbers
		Random random = new Random(seed); 
		double alpha = 0.1;
		double threshold = 0.2;
		int epochs = 10;
		
		// initializing input- and output-related arrays
		int[][] input = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		int[] desired = {0, 0, 0, 1}; // desired output for AND
		int[] predicted = new int[4];
		double[] MSE = new double[epochs];
		// to implement OR use {0, 1, 1, 1} as desired output 
		// to implement XOR use {0, 1, 1, 0} as desired output 
		
		// randomly initialize weights between [-0.5, 0.5]
		double[] weights = new double[2];
		weights[0] = random.nextDouble() - 0.5;
		weights[1] = random.nextDouble() - 0.5;
		
		// loop over all epochs
		for (int n = 0; n < epochs; n++) {
			// loop over training sample
			for (int i = 0; i < input.length; i++) {
				// determine prediction according to step function
				if (input[i][0] * weights[0] + input[i][1] * weights[1] - threshold >= 0) {
					predicted[i] = 1;
				}
				else {
					predicted[i] = 0;
				}	
				// compute error and update weights accordingly
				double error = desired[i] - predicted[i];
				MSE[n] += error * error; 
				weights[0] += alpha * input[i][0] * error;
				weights[1] += alpha * input[i][1] * error;
			}
			MSE[n] = MSE[n] / input.length;
			System.out.println(MSE[n]);	
		}
	}
}
