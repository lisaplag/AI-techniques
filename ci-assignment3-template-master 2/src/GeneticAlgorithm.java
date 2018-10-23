import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TSP problem solver using genetic algorithms.
 */
public class GeneticAlgorithm {

    private int generations;
    private int popSize;

    /**
     * Constructs a new 'genetic algorithm' object.
     * @param generations the amount of generations.
     * @param popSize the population size.
     */
    public GeneticAlgorithm(int generations, int popSize) {
        this.generations = generations;
        this.popSize = popSize;
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
     * This method should solve the TSP. 
     * @param pd the TSP data.
     * @return the optimized product sequence.
     */
    public int[] solveTSP(TSPData pd) {
        //Step 1) Randomly select starting population.
        List<Chromosome> population = generateStartPop(pd);

        //Perform evolution cycle
        for(int i = 0; i < generations; i++){
            population = evolution(population);
        }

        //select the chromosome with highest fitness.
        double bestFitness = 0;
        Chromosome bestChromosome = null;
        for (Chromosome chromosome : population) {
            if (chromosome.getFitness() > bestFitness) {
                bestChromosome = chromosome;
            }
        }
        return bestChromosome.getData();
    }

    /**
     * Perform the evolution cycle on input population.
     * @param population the starting population
     * @return the new population.
     */
    public List<Chromosome> evolution(List<Chromosome> population){
        List<Chromosome> newPopulation = new ArrayList<>();
        //Step 2) Compute the fitness-(ratio) of all chromosomes.
        double totalFitness = 0;
        for(Chromosome chromosome : population) {
            totalFitness += chromosome.getFitness();
        }

        //Step 3) Selection.
        //repeat until population is full again
        for (int i = 0; i < popSize; i++) {
            Chromosome parentOne = rouletteWheelSelection(population, totalFitness);
            Chromosome parentTwo = rouletteWheelSelection(population, totalFitness);

            //Step 4) Cross over
            Chromosome child = parentOne.crossOver(parentTwo);

            //Step 5) Mutation
            Chromosome mutatedChild = child.mutate();

            //Step 6) Add new chromosome to the new population
            newPopulation.add(mutatedChild);
        }

        return newPopulation;
    }

    /**
     * Perform RouletteWheelSelection.
     * @param population the population.
     * @return the selected chromosome.
     */
    public Chromosome rouletteWheelSelection(List<Chromosome> population, double totalFitness){
        double r = Math.random();
        double sum = 0;
        Chromosome chosen = null;
        // This block chooses a chromosome based on its fitnessratio.
        for (Chromosome chromosome : population) {
            sum += chromosome.getFitness() / totalFitness;
            if (r <= sum) {
                chosen = chromosome;
                break;
            }
        }
        return chosen;
    }

    /**
     * Generate the starting population.
     * @param pd TSPData to solve.
     * @return The starting population.
     */
    public List<Chromosome> generateStartPop(TSPData pd) {
        //Create the base chromosome
        int numberOfProducts = pd.getDistances().length;
        int[] chromosomeData = new int[numberOfProducts];
        for (int i = 0; i < numberOfProducts; i++){
            chromosomeData[i] = i;
        }

        //Create as many random mutations of the base chromosome to fill the population
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < popSize; i++){
            this.shuffle(chromosomeData);
            population.add(new Chromosome(chromosomeData));
        }

        return population;
    }

    /**
     * Assignment 2.b
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
    	//parameters
    	int populationSize = 20;
        int generations = 20;
        String persistFile = "./tmp/productMatrixDist";
        
        //setup optimization
        TSPData tspData = TSPData.readFromFile(persistFile);
        GeneticAlgorithm ga = new GeneticAlgorithm(generations, populationSize);
        
        //run optimzation and write to file
        int[] solution = ga.solveTSP(tspData);
        tspData.writeActionFile(solution, "./data/TSP solution.txt");
    }
}