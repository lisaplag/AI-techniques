package assignment1;

import java.util.Random;


public class MultilayerNetwork {

    //configure data
    private double alpha;

    private double[][] trainingInput;
    private double[][] trainingTargets;
    private double[][] validationInput;
    private double[][] validationTargets;
    private double[][] testInput;
    private double[][] testTargets;

    private static double[][] weightHidden;
    private static double[] thetaHidden;
    private static double[][] weightOutput;
    private static double[] thetaOutput;

    //static data that is directly derived from above data
    private int inputNeurons;
    private int hiddenNeurons;
    private int outputNeurons;
    private int nFeatures;

    public MultilayerNetwork(int hiddenLayerSize, double alpha, double[][] input, double[][] outputDesired) {
        this.alpha = alpha;
        long seed = 0; //seed for possible use in Random
        Random random = new Random();

        //Randomly splitting data into training, validation and test set
        int nExamples = input.length;
        nFeatures = input[0].length;

        int[] indices = ReadData.randomIndices(nExamples);
        double[][][] splitInputs = ReadData.splitSample(indices, input);
        double[][][] splitTargets = ReadData.splitSample(indices, outputDesired);
        trainingInput = splitInputs[0];
        trainingTargets = splitTargets[0];
        validationInput = splitInputs[1];
        validationTargets = splitTargets[1];
        testInput = splitInputs[2];
        testTargets = splitTargets[2];

        //Step 0: setting up neural network
        inputNeurons = nFeatures; //index i
        hiddenNeurons = hiddenLayerSize; //index j
        outputNeurons = outputDesired[0].length; //index k

        weightHidden = new double[inputNeurons][hiddenNeurons];
        weightOutput = new double[hiddenNeurons][outputNeurons];
        thetaHidden = new double[hiddenNeurons];
        thetaOutput = new double[outputNeurons];

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
    }
    
    // method for training the network using the training set
	public double train() {
		int nTrainingExamples = trainingInput.length;
        double MSE = 0;

        for (int n = 0; n < nTrainingExamples; n++) {
        	double sumSquaredErrors = 0; // initialize sum of squared errors
        	
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
                    sum += trainingInput[n][i] * weightHidden[i][j];
                }
                //Store the output of the hidden neuron
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
                //Store the output of the output neuron
                outputFinal[k] = 1 / (1 + Math.exp(thetaOutput[k] - sum));
            }

            //Step 3) Weight training
            double[] errorGradient = new double[outputNeurons]; //
            //3a) Calculate the error gradient for the output neurons

            //For each output neuron
            for (int k = 0; k < outputNeurons; k++) {
                //calculate the error
                double error = trainingTargets[n][k] - outputFinal[k];

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
                    weightHidden[i][j] += alpha * trainingInput[n][i] * errorGradientHidden;
                }
            }
            
            MSE += sumSquaredErrors / outputNeurons;
        }
        
        MSE = MSE / nTrainingExamples;
        return MSE;
	}
	
	// method for evaluating the network given the weights from training
	public double runWithoutChangingWeights(double[][] input, double[][] inputTargets){
        int nTrainingExamples = input.length;
        double MSE = 0;
        
        for (int n = 0; n < nTrainingExamples; n++) {
        	double sumSquaredErrors = 0; // initializing sum of squared errors
        	
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
                //Store the output of the hidden neuron
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
                //Store the output of the output neuron
                outputFinal[k] = 1 / (1 + Math.exp(thetaOutput[k] - sum));
            }

            //Step 3) Weight training
            double[] errorGradient = new double[outputNeurons]; //
            //3a) Calculate the error gradient for the output neurons

            //For each output neuron
            for (int k = 0; k < outputNeurons; k++) {
                //calculate the error
                double error = inputTargets[n][k] - outputFinal[k];
                //calculate the error gradient
                errorGradient[k] = outputFinal[k] * (1 - outputFinal[k]) * error;
                //Add the square of the error to the sumSquaredErrors
                sumSquaredErrors += error * error;
            }
            
            MSE += sumSquaredErrors / outputNeurons;
        }
        
        MSE = MSE / nTrainingExamples;
        return MSE;
    }

    public double validate() {
        return runWithoutChangingWeights(validationInput, validationTargets);
    }

	public double test() {
		return runWithoutChangingWeights(testInput, testTargets);
	}

	public int[] predict(double[][] input) {
		PredictionNetwork p = new PredictionNetwork(hiddenNeurons, input, weightHidden, weightOutput, thetaHidden, thetaOutput);
		return p.predict();
	}

	// Count the amount of correct and incorrect predictions over test or validation set, and calculate success rate.
	public void testPrediction(String in) {
        int[] p;
        int correct = 0;
        int incorrect = 0;
        if ( in.equals("test")) {
            p = predict(testInput);
            for (int i = 0; i < testTargets.length; i++) {
                if ( testTargets[i][p[i]] == 1.0) {
                    correct++;
                } else {
                    incorrect ++;
                }
            }
        } else {
            p = predict(validationInput);
            for (int i = 0; i < validationTargets.length; i++) {
                if ( validationTargets[i][p[i]] == 1.0) {
                    correct++;
                } else {
                    incorrect ++;
                }
            }
        }
        double denom = incorrect + correct;
        double rate = correct / denom;
        System.out.println("Prediction succes rate over " + in + " set: " + rate);
    }

    public int[][] confusionMatrix() {
        // Make empty matrix as 2d array.
        int[][] res = new int[7][7];
        for (int k = 0; k < 7; k++) {
            for (int j = 0; j < 7; j++) {
                res[k][j] = 0;
            }
        }
        // Increment the correct place in the matrix for every sample in the test set.
        int[] p;
        p = predict(testInput);
        for (int i = 0; i < testTargets.length; i++) {
            for (int j = 0; j < 7; j++) {
                if (testTargets[i][j] == 1.0) {
                        res[j][p[i]]++;
                }
            }
        }
        return res;
    }

}

