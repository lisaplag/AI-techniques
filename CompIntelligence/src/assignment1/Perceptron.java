package assignment1;

import java.util.Arrays;
import java.util.Random;

public class Perceptron {
	
	public static void main(String[] args) {
		long seed = 0;
		Random random = new Random(seed);
		int[][] input = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		int[] desired = {0, 0, 0, 1};
		int[] predicted = new int[4];
		double alpha = 0.1;
		int epochs = 10;
		double[] MSE = new double[epochs];
		
		double[] weights = new double[2];
		double threshold = 0.2;
		weights[0] = random.nextDouble() - 0.5;
		weights[1] = random.nextDouble() - 0.5;
		
		for (int n = 0; n < epochs; n++) {
			
			for (int i = 0; i < input.length; i++) {
				
				if (input[i][0] * weights[0] + input[i][1] * weights[1] - threshold >= 0) {
					predicted[i] = 1;
				}
				else {
					predicted[i] = 0;
				}	
				
				double error = desired[i] - predicted[i];
				MSE[n] += error*error; 
				weights[0] += alpha * input[i][0] * error;
				weights[1] += alpha * input[i][1] * error;
			}
			MSE[n] = MSE[n] / input.length;
			System.out.println(MSE[n]);	
		}
		// System.out.println(Arrays.toString(MSE));
		
	}
}