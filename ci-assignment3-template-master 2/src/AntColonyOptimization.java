import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    	int min = Integer.MAX_VALUE;
    	Route result = null;
    	// run ACO 3 times to obtain best possible results
    	for (int n = 0; n < 3; n++) {
            maze.reset();
            Route globalRoute = null;
            Route localRoute = null;
            int globalMin = Integer.MAX_VALUE;
            int localMin = Integer.MAX_VALUE;
            double percentageSameRoute = 0.0;
            int gen = 0;
            // let another generation run through the maze if not converged yet
            while (percentageSameRoute < 1.0) {
            	ArrayList<Route> genRoutes = new ArrayList<>();
                localRoute = null;
                localMin = Integer.MAX_VALUE;
                gen++;
                // dynamically increase evaporation rate
                evaporation += 0.01;
            	if (evaporation > 0.9) {
                    evaporation = 0.9;
                }                
                // let all ants run through maze
                for (int i = 0; i < antsPerGen; i++) {
                    Ant ant = new Ant(maze, spec);
                    Route found = ant.findRoute();
                    genRoutes.add(found);
                    // check if route is shortest of this generation
                    if (found.size() < localMin) {
                        localMin = found.size();
                        localRoute = found;
                    }
                    // check if route is shortest of all generations
                    if (found.size() < globalMin) {
                        globalMin = found.size();
                        globalRoute = found;
                    }
                }
                // update convergence criterion
                percentageSameRoute = Collections.frequency(genRoutes, localRoute) / (double) antsPerGen;
                maze.addPheromoneRoutes(genRoutes, Q);
                maze.evaporate(evaporation);
            }
            // update best route of all runs of ACO if a shorter one was found in this run
            if (globalMin < min) {
            	min = globalMin;
            	result = globalRoute;
            }
            System.out.println("Run: " + (n+1) + ", Number of generations: " + (gen) + ", Local min: " + localMin + ", Global min: " + globalMin);
    	}
        return result;
    }

    /**
     * Driver function for Assignment 1
     */
    public static void main(String[] args) throws FileNotFoundException {
    	// good parameters hard maze
    	int genSize = 35;
        int noGen = 50;
        double Q = 800;
        double evap = 0.3;
    	
//    	//good parameters medium maze
//    	int genSize = 35;
//        int noGen = 50;
//        double Q = 200;
//        double evap = 0.5;
        
//    	//good parameters easy maze
//    	int genSize = 25;
//        int noGen = 50;
//        double Q = 50;
//        double evap = 0.8;

        //construct the optimization objects
        Maze maze = Maze.createMaze("./data/hard maze.txt");
        PathSpecification spec = PathSpecification.readCoordinates("./data/hard coordinates.txt");
        AntColonyOptimization aco = new AntColonyOptimization(maze, genSize, noGen, Q, evap);
        
        //save starting time
        //long startTime = System.currentTimeMillis();
        
        //run optimization
        Route shortestRoute = aco.findShortestRoute(spec);
        
        //print time taken
//        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));
        
        //save solution
        shortestRoute.writeToFile("./data/hard_solution.txt");
        
        //print route size
//        System.out.println("Route size: " + shortestRoute.size());
        
    }

}
