import java.io.IOException;
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
            shuffle(chromosomeData);
            population.add(chromosomeData);
        }
        return population;
    }


    /**
     * Knuth-Yates shuffle, reordering a array randomly
     * @param chromosome array to shuffle.
     */
    private void shuffle(int[] chromosome) {
        int n = chromosome.length;
        for (int i = 0; i < n; i++) {
            int r = i + (int) (Math.random() * (n - i));
            int swap = chromosome[r];
            chromosome[r] = chromosome[i];
            chromosome[i] = swap;
        }
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
        	length += distances[i][i+1];
        }
        // finally, add the length of the route between the last product and the end
        int lastProduct = chromosome[chromosome.length-1];
        length += endDistances[lastProduct];
        
        //TODO calculate the actual fitness: 1/length of route
        return 1.0 / length;
    }
    
    /**
     * Cross-over function.
     * @param chromosomeA array to cross-over.
     * @param chromosomeB array to cross-over.
     * @return The crossed-over chromosome array.
     */
    public int[] crossOver(int[] chromosomeA, int[] chromosomeB){
        //TODO implement cross-over function
    	// get random int between 0 and length (exclusive)
    	int start = (int) (Math.random() * chromosomeA.length);
    	// get random int between start+1 and length (exclusive)
    	int end = (start + 1) + (int) (Math.random() * (chromosomeA.length - start - 1));
    	// initialize new chromosome
    	int[] newChromosome = new int[chromosomeA.length];
    	// create list to keep track of products already in chromosome
    	ArrayList<Integer> products = new ArrayList<Integer>();
    	
    	// putting genes of chromosomeA in new chromosome
    	for (int i = 0; i < newChromosome.length; i++) {
    		if (start + i < end) {
    			newChromosome[i] = chromosomeA[start + i];
    			products.add(chromosomeA[start + i]);
    		}
    		else {
    			break;
    		}
    	}
    	// fill remaining genes with sequence from chromosomeB
    	int n = 0;
    	for (int j = 0; j < chromosomeB.length; j++) {
    		if ( !products.contains(chromosomeB[j]) ) {
    			newChromosome[end - start + n] = chromosomeB[j];
    			products.add(chromosomeB[j]);  
    			n++;
    		}
    		if (products.size() == chromosomeA.length) {
    			break;
    		}
    	}
    	
//    	System.out.println("A: " + Arrays.toString(chromosomeA) + " Start: " + start + " End: " + end);
//    	System.out.println("B: " + Arrays.toString(chromosomeB));
//    	System.out.println(Arrays.toString(newChromosome));
    	
        return newChromosome;
    }

    
    /**
     * Mutation function.
     * @param chromosome array to mutate.
     * @return The mutated chromosome array.
     */
    public int[] mutate(int[] chromosome) {
        //TODO implement mutation function
    	// randomly swap two genes of the chromosome
    	for (int i = 0; i < chromosome.length; i++) {
    		if (Math.random() < chanceMutation) {
    	        int index = (int) (Math.random() * chromosome.length);
    	        int swap = chromosome[index];
    	        chromosome[index] = chromosome[i];
    	        chromosome[i] = swap;    			
    		}
    	}
        
        return chromosome;
    }
    
    
    /**
     * Perform the evolution cycle on input population.
     * @param population the starting population
     * @return the new population.
     */
    public List<int[]> evolution(List<int[]> population, int[] bestChromosome, TSPData pd){
    	//initialize new population list
        List<int[]> newPopulation = new ArrayList<>();

        //Step 2) Compute the fitness-(ratio) of all chromosomes.
        double totalFitness = 0;
        for(int[] chromosome : population) {
            totalFitness += getFitness(chromosome, pd);
        }

        //repeat selection until population is full again
        for (int i = 0; i < popSize; i++) {
        	//keep best chromosome if there already is one (elitism)
        	if (bestChromosome != null) {
        		newPopulation.add(bestChromosome);
        		bestChromosome = null;
        	}
            //Step 3) Selection.
            int[] parentOne = rouletteWheelSelection(population, totalFitness, pd);
            int[] parentTwo = rouletteWheelSelection(population, totalFitness, pd);

            //Step 4) Cross-over
            if (Math.random() < chanceCrossOver) {
            	int[] child = crossOver(parentOne, parentTwo);       
                //Step 5) Add mutated child to the population
                newPopulation.add(mutate(child));
            } else {
            	newPopulation.add(parentOne);
            }
            
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

        //Perform evolution cycle
        for(int i = 0; i < generations; i++){
            population = evolution(population, bestChromosome, pd);
            
            //select the chromosome with highest fitness.
            for (int[] chromosome : population) {
            	double fitness = getFitness(chromosome, pd);
            	//update best chromosome if necessary
                if (fitness > bestFitness) {
                    bestChromosome = chromosome;
                    bestFitness = fitness;
                }
            }
            System.out.println("Length: " + (int) (1/bestFitness) + ", " + Arrays.toString(bestChromosome));
        }

        return bestChromosome;
    }

   
    /**
     * Assignment 2.b
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
    	//parameters
    	int populationSize = 1000;
        int generations = 100;
        String persistFile = "./data/productMatrixDist";
        
        //setup optimization
        TSPData tspData = TSPData.readFromFile(persistFile);
        GeneticAlgorithm ga = new GeneticAlgorithm(generations, populationSize);
        
        //run optimzation and write to file
        int[] solution = ga.solveTSP(tspData);
        tspData.writeActionFile(solution, "./data/TSP solution.txt");
    }
}