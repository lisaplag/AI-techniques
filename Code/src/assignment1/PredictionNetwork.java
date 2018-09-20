package assignment1;

public class PredictionNetwork {

    double[][] weightHidden;
    double[][] weightOutput;
    double[] thetaHidden;
    double[] thetaOutput;

    public PredictionNetwork(double[][] wHidden, double[][]wOutput, double[] thHidden, double[] thOutput) {
        weightHidden = wHidden;
        weightOutput = wOutput;
        thetaHidden = thHidden;
        thetaOutput = thOutput;
    }

    public void predict() {
        //Loading data
        double[][] inputs = ReadData.readUnknown();
        int nExamples = inputs.length;
        int nFeatures = inputs[0].length;


        //Step 0: setting up neural network
        int inputNeurons = nFeatures;
        int hiddenNeurons = nFeatures + 1;
        int outputNeurons = 7;

        double[] predictions = new double[outputNeurons];

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
                    sum += inputs[n][i] * weightHidden[i][j];
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
                //Store the output of the output neutrons
                outputFinal[k] = 1 / (1 + Math.exp(thetaOutput[k] - sum));
                if (outputFinal[k] > 0.5) {
                    outputFinal[k] = 1;
                } else {
                    outputFinal[k] = 0;
                }
                predictions = outputFinal;
            }
            // Print the anser of class 1-7 for the sample
            for (int i = 0; i < predictions.length; i++) {
                if (predictions[i] == 1) {
                    System.out.println(i+1);
                    break;
                }
            }
        }

        System.out.println("Number of unknown samples: " + nExamples);

    }
}


