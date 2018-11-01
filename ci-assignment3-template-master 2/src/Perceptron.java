import java.util.Random;

public class Perceptron {

	public static void main(String[] args) {
		// initialize variables for training set
		double[][] input = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		int[] desired = {0, 1, 1, 1};
		int epochs = 10;
		double alpha = 0.1;
		double threshold = 0.2;
		train(input, desired, epochs, alpha, threshold);
	}

	public static void train(double[][] input, int[] output, int epochs, double alpha, double threshold) {
		long seed = 0; //seed for possible use in Random
		Random random = new Random();
		int rows = input.length;
		int cols = input[0].length;
		int[] predicted = new int[rows];
		double[] MSE = new double[epochs];
		double[] weights = new double[cols];

		//randomly initialize weights [-0.5, 0.5]
		for (int i = 0; i < cols; i++) {
			weights[i] = random.nextDouble() - 0.5;
		}

		//loop over all epochs
		for (int n = 0; n < epochs; n++) {
			//loop over all rows of input
			for (int i = 0; i < rows; i++) {
				//initialize sum at 0
				double sum = 0;
				//loop over all columns of input
				for (int k = 0; k < cols; k++) {
					sum += input[i][k] * weights[k];
				}
				//check activation function
				if (sum - threshold >= 0) {
					predicted[i] = 1;
				} else {
					predicted[i] = 0;
				}

				//compute error and update weights accordingly
				double error = output[i] - predicted[i];
				MSE[n] += error * error;
				for (int j = 0; j < cols; j++) {
					weights[j] += alpha * input[i][j] * error;
				}
			}
			MSE[n] = MSE[n] / rows;
			System.out.println(MSE[n]);
		}
	}
}
