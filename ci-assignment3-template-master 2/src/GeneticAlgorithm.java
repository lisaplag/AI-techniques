import assignment1.MultilayerNetwork;
import assignment1.ReadData;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * TSP problem solver using genetic algorithms.
 */
public class GeneticAlgorithm {

    private int generations;
    private int popSize;
    private double chanceCrossOver;
    private double chanceMutation;

    /**
     * Constructs a new 'genetic algorithm' object.
     * @param generations the amount of generations.
     * @param popSize the population size.
     */
    public GeneticAlgorithm(int generations, int popSize) {
        this.generations = generations;
        this.popSize = popSize;
        this.chanceCrossOver = 0.7;
        this.chanceMutation = 0.01;
    }
    
 
    /**
     * Generate the starting population.
     * @param pd TSPData to solve.
     * @return The starting population.
     */
    public List<int[]> generateStartPop(TSPData pd) {
        //Create the base chromosome
        int numberOfProducts = pd.getDistances().length;
        int[] chromosomeData = new int[numberOfProducts];
        for (int i = 0; i < numberOfProducts; i++){
            chromosomeData[i] = i;
        }

        //Create as many random mutations of the base chromosome to fill the population
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < popSize; i++){
            chromosomeData = shuffle(chromosomeData);
            population.add(chromosomeData);
        }
        return population;
    }


    /**
     * Knuth-Yates shuffle, reordering a array randomly
     * @param chromosome array to shuffle.
     */
    private int[] shuffle(int[] chromosome) {
        int n = chromosome.length;
        int[] shuffledChromosome = Arrays.copyOf(chromosome, n);
        for (int i = 0; i < n; i++) {
            int r = i + (int) (Math.random() * (n - i));
            int swap = shuffledChromosome[r];
            shuffledChromosome[r] = shuffledChromosome[i];
            shuffledChromosome[i] = swap;
        }
        
        return shuffledChromosome;
    }
    
    
    /**
     * Get the fitness value of a chromosome.
     * @param chromosome The chromosome.
     * @param pd TSPData to solve.
     * @return The fitness value.
     */
    public double getFitness(int[] chromosome, TSPData pd) {
        // get distance information    
        int[][] distances = pd.getDistances();
        int[] startDistances = pd.getStartDistances();
        int[] endDistances = pd.getEndDistances();
        
        // initialize length of the route as distance between start and first product
        int firstProduct = chromosome[0];
        int length = startDistances[firstProduct];
        // add the lengths of all paths between products to the total length
        for (int i = 0; i < chromosome.length - 1; i++) {
        	length += distances[chromosome[i]][chromosome[i+1]];
        }
        // finally, add the length of the route between the last product and the end
        int lastProduct = chromosome[chromosome.length-1];
        length += endDistances[lastProduct] + chromosome.length; // add product steps
        
        //Calculate the actual fitness: 1/length of route
        return (1.0 / length);
    }
    
    /**
     * Cross-over function.
     * @param chromosomeA array to cross-over.
     * @param chromosomeB array to cross-over.
     * @return The crossed-over chromosome array.
     */
    public int[][] crossOver(int[] chromosomeA, int[] chromosomeB){
    	// get random int between 1 and length (exclusive)
    	int start = 1 + (int) (Math.random() * (chromosomeA.length - 1));

    	// initialize new chromosomes
    	int[] newChromosomeA = new int[chromosomeA.length];
    	int[] newChromosomeB = new int[chromosomeA.length];
    	// create list to keep track of products already in new chromosomes
    	ArrayList<Integer> productsA = new ArrayList<Integer>();
    	ArrayList<Integer> productsB = new ArrayList<Integer>();
    	
    	// putting genes of chromosome A and B in new chromosomes
    	for (int i = 0; i < newChromosomeA.length; i++) {
    		if (i < start) {
    			newChromosomeB[i] = chromosomeB[i];
    			productsB.add(chromosomeB[i]);
    		}
    		else {
    			newChromosomeA[i] = chromosomeA[i];
    			productsA.add(chromosomeA[i]);    			
    		}	
    	}    	
    	// fill remaining genes with sequence from other chromosome
    	int a = 0;
    	int b = 0;
    	for (int j = 0; j < chromosomeB.length; j++) {
    		// check if gene of B is not already present in newChromosomeA
    		if ( !productsA.contains(chromosomeB[j]) ) {
    			newChromosomeA[a] = chromosomeB[j];
    			productsA.add(chromosomeB[j]);  
    			a++;
    		}
    		// check if gene of A is not already present in newChromosomeB
    		if ( !productsB.contains(chromosomeA[j]) ) {
    			newChromosomeB[start + b] = chromosomeA[j];
    			productsB.add(chromosomeA[j]);  
    			b++;
    		}
    	}
    	int[][] crossOvers = new int[2][newChromosomeA.length];
    	crossOvers[0] = newChromosomeA;
    	crossOvers[1] = newChromosomeB;
        return crossOvers;
    }

    
    /**
     * Mutation function.
     * @param chromosome array to mutate.
     * @return The mutated chromosome array.
     */
    public int[] mutate(int[] chromosome) {
    	// initialize mutation to copy of chromosome
    	int[] mutation = Arrays.copyOf(chromosome, chromosome.length);
    	// randomly swap two genes of the chromosome
    	for (int i = 0; i < mutation.length; i++) {
    		if (Math.random() < chanceMutation) {
    	        int index = (int) (Math.random() * mutation.length);
    	        int swap = mutation[index];
    	        mutation[index] = mutation[i];
    	        mutation[i] = swap;    			
    		}
    	}
        return mutation;
    }
    
    
    /**
     * Perform the evolution cycle on input population.
     * @param population the starting population
     * @return the new population.
     */
    public List<int[]> evolution(List<int[]> population, int[] bestChromosome, TSPData pd){
    	//initialize new population list
        List<int[]> newPopulation = new ArrayList<>();
        newPopulation.add(bestChromosome); // applying elitism

        //Step 2) Compute the fitness-(ratio) of all chromosomes.
        double totalFitness = 0;
        for(int[] chromosome : population) {
            totalFitness += getFitness(chromosome, pd);
        }

        //repeat selection until population is full again
        while(newPopulation.size() < popSize) {
            //Step 3) Selection.
            int[] parentOne = rouletteWheelSelection(population, totalFitness, pd);
            int[] parentTwo = rouletteWheelSelection(population, totalFitness, pd);
            //Step 4) Cross-over with certain chance
            if (Math.random() < chanceCrossOver) {
            	//create children by cross-over
            	int[][] children = crossOver(parentOne, parentTwo);
            	//mutate the children
            	int[] childOne = mutate(children[0]);
            	int[] childTwo = mutate(children[1]);
                //Step 5) Add mutated child to the new population
                newPopulation.add(childOne);
                newPopulation.add(childTwo);
            } else {
            	// if no cross-over, add potentially mutated parents to new population
            	newPopulation.add(mutate(parentOne));
            	newPopulation.add(mutate(parentTwo)); 
            }      		 	           
        }
        // due to elitism there will be 1 chromosome too much
        if(newPopulation.size() > popSize) {
        	newPopulation.remove(newPopulation.size() - 1);
        }

        return newPopulation;
    }

    /**
     * Perform Roulette Wheel Selection.
     * @param population the population.
     * @param totalFitness the sum of all fitness values of the population.
     * @param pd TSPData to solve.
     * @return the selected chromosome.
     */
    public int[] rouletteWheelSelection(List<int[]> population, double totalFitness, TSPData pd){
        double r = Math.random();
        double sum = 0;
        int[] chosen = null;
        Collections.shuffle(population);
        
        // This block chooses a chromosome based on its fitnessratio.
        for (int[] chromosome : population) {
            sum += getFitness(chromosome, pd) / totalFitness;
            if (r <= sum) {
                chosen = chromosome;
                break;
            }
        }
        return chosen;
    }
    
    

    /**
     * This method should solve the TSP. 
     * @param pd the TSP data.
     * @return the optimized product sequence.
     */
    public int[] solveTSP(TSPData pd) {
        //Step 1) Randomly select starting population.
        List<int[]> population = generateStartPop(pd);
        
        //initialize best values ever found
        double bestFitness = Double.MIN_VALUE;
        int[] bestChromosome = null;

        //Perform evaluation and evolution cycle
        for(int i = 0; i < generations; i++){
            //find the chromosome with highest fitness.
            for (int[] chromosome : population) {
            	double fitness = getFitness(chromosome, pd);
            	//update best chromosome if necessary
                if (fitness > bestFitness) {
                    bestChromosome = chromosome;
                    bestFitness = fitness;
                }
            }            
            System.out.println("Cycle: " + i + " | Length: " + (int) (1/bestFitness) + " | " + Arrays.toString(bestChromosome));
            population = evolution(population, bestChromosome, pd);
        }

        return bestChromosome;
    }

   
    /**
     * Assignment 2.b
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        //Configure the network
        double alpha = 0.02;
        double epsilon = 0.017;
        int hiddenLayerSize = 11;

        double[][] input = ReadData.readInput();
        double[][] outputDesired = ReadData.readTargets();

        MultilayerNetwork network = new MultilayerNetwork(hiddenLayerSize, alpha, input, outputDesired);

        //Repeat this process until the sumSquaredErrors is smaller or equal than epsilon.
        //printing results
        int epochs = 0;
        double validateError = Double.MAX_VALUE;

        while ((validateError > epsilon) && (epochs < 1000)) {
            //Train the network
            double trainError = network.train();

            //Validate the network
            validateError = network.validate();
            epochs++;
        }

        //Final prediction (for the unknown file)
        int class1 = 1;
        int class2 = 2;
        int class3 = 3;
        int[] predictions = network.predict(ReadData.readUnknown());
        ArrayList<Integer> resultset1 = network.productsOfClass( class1, class2, class3, predictions);
        int[] results = alToArray(resultset1);
        ReadData.writeResults(results, predictions);
        //parameters
    	int populationSize = 1000;
        int generations = 100;
        String persistFile = "./ci-assignment3-template-master 2/data/productMatrixDist";

        File inputFile = new File("./ci-assignment3-template-master 2/data/tsp products.txt");
        // Use GC_ProductCoordinates.txt for grand challenge ^^

        File productsFile = new File("./GC_Products.txt"); // Temp file, that will get overwritten every time the "Grand challenge" runs.

        // Make a reader and a writer
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(productsFile));

        int p = 0;
        String outputString = ""; // Initialize the output string empty
        String tempString;
        reader.readLine(); // skip the first line

        // Keep updating the output string with only the correct products
        while ((tempString = reader.readLine()) != null) {
            if (tempString.startsWith(Integer.toString(results[p] + 1))) {
                outputString +=  "\n" + tempString;
                p++;
            }

        }
        // Add the amount of products at the first line
        tempString = Integer.toString(p) + ";" + outputString;

        writer.write(tempString);
        writer.close();
        reader.close();

        //setup optimization
        //parameters
        int gen = 20;
        int noGen = 50;
        double Q = 400;
        double evap = 0.1;

        String TSPpath = "./GC_Products.txt";
        String coordinates = "./ci-assignment3-template-master 2/data/hard coordinates.txt"; // Use the GC maze here for grand challenge!

        //construct optimization
        Maze maze = Maze.createMaze("./ci-assignment3-template-master 2/data/hard maze.txt");// Use the GC maze here for grand challenge!
        TSPData pd = TSPData.readSpecification(coordinates, TSPpath);
        AntColonyOptimization aco = new AntColonyOptimization(maze, gen, noGen, Q, evap);

        //run optimization
        pd.calculateRoutes(aco);
        GeneticAlgorithm ga = new GeneticAlgorithm(generations, populationSize);
        
        //run optimzation and write to file
        int[] solution = ga.solveTSP(pd);
        pd.writeActionFile(solution, "./Action.txt");
        System.out.println(tempString);
        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    public static int[] alToArray(ArrayList<Integer> list) {
        int[] result = new int[list.size()];
        for (int i = 0; i < result.length ; i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}