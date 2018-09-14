package assignment1;

import java.util.Arrays;
import java.util.Random;

public class Perceptron {
	
	public static void main(String[] args) {
		int[][] input = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		int[] desired = {0, 1, 1, 1};
		int epochs = 30;
		double alpha = 0.1;
		double threshold = 0.2;
	}

	public void train(int[][] input, int[] output, int epochs, double alpha, double threshold) {
		long seed = 0;
		Random random = new Random();
		int rows = input.length;
		int cols = input[0].length;
		int[] predicted = new int[rows];
		double[] MSE = new double[epochs];
		double[] weights = new double[cols];

		for (int i = 0; i < cols; i++) {
			double offset = random.nextDouble();
			weights[i] = offset =0.5;
		}

		for (int n = 0; n < epochs; n++) {

			for (int i = 0; i < rows; i++) {
				double sum = 0;
				for(int k = 0; k < cols; k++) {
					sum += input[i][k]*weights[k];
				}
				
				if (sum - threshold >= 0) {
					predicted[i] = 1;
				}
				else {
					predicted[i] = 0;
				}	
				
				double error = output[i] - predicted[i];
				MSE[n] += error*error;
				for (int j = 0; j < cols ; j++) {
					weights[j] += alpha * input[i][j] * error;
				}
			}
			MSE[n] = MSE[n] / rows;
			System.out.println(MSE[n]);	
		}
		// System.out.println(Arrays.toString(MSE));
		
	}
}