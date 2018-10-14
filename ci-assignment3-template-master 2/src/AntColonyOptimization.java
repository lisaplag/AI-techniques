import java.io.FileNotFoundException;

/**
 * Class representing the first assignment. Finds shortest path between two points in a maze according to a specific
 * path specification.
 */
public class AntColonyOptimization {
	
	private int antsPerGen;
    private int generations;
    private double Q;
    private double evaporation;
    private Maze maze;
    public static int counter;

    /**
     * Constructs a new optimization object using ants.
     * @param maze the maze .
     * @param antsPerGen the amount of ants per generation.
     * @param generations the amount of generations.
     * @param Q normalization factor for the amount of dropped pheromone
     * @param evaporation the evaporation factor.
     */
    public AntColonyOptimization(Maze maze, int antsPerGen, int generations, double Q, double evaporation) {
        this.maze = maze;
        this.antsPerGen = antsPerGen;
        this.generations = generations;
        this.Q = Q;
        this.evaporation = evaporation;
    }

    /**
     * Loop that starts the shortest path process
     * @param spec Spefication of the route we wish to optimize
     * @return ACO optimized route
     */
    public Route findShortestRoute(PathSpecification spec) {
        maze.reset();
        Route found = null;
        for (int i = 0; i < 1000; i++) {
            Ant ant = new Ant(maze, spec);
            found = ant.findRoute();
            maze.addPheromoneRoute(found, Q);
            maze.evaporate(evaporation);
            System.out.println(counter);
        }
        //Ant greedyAnt = new Ant(maze, spec);
        return found;
    }

    /**
     * Driver function for Assignment 1
     */
    public static void main(String[] args) throws FileNotFoundException {
    	//parameters
    	int gen = 1;
        int noGen = 20;
        double Q = 1600;
        double evap = 0.1;
        
        //construct the optimization objects
        Maze maze = Maze.createMaze("./data/easy maze.txt");
        PathSpecification spec = PathSpecification.readCoordinates("./data/easy coordinates.txt");
        AntColonyOptimization aco = new AntColonyOptimization(maze, gen, noGen, Q, evap);
        
        //save starting time
        long startTime = System.currentTimeMillis();
        
        //run optimization
        Route shortestRoute = aco.findShortestRoute(spec);
        
        //print time taken
        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));
        
        //save solution
        shortestRoute.writeToFile("./data/easy_solution.txt");
        
        //print route size
        System.out.println("Route size: " + shortestRoute.size());
    }
}
