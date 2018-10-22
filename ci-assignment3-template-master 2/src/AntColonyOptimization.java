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
    private double epsilon;
    public static int counter;

    /**
     * Constructs a new optimization object using ants.
     * @param maze the maze .
     * @param antsPerGen the amount of ants per generation.
     * @param generations the amount of generations.
     * @param Q normalization factor for the amount of dropped pheromone
     * @param evaporation the evaporation factor.
     */
    public AntColonyOptimization(Maze maze, int antsPerGen, int generations, double Q, double evaporation, double epsilon) {
        this.maze = maze;
        this.antsPerGen = antsPerGen;
        this.generations = generations;
        this.Q = Q;
        this.evaporation = evaporation;
        this.epsilon = epsilon;
    }

    /**
     * Loop that starts the shortest path process
     * @param spec Spefication of the route we wish to optimize
     * @return ACO optimized route
     */
    public Route findShortestRoute(PathSpecification spec) {
        maze.reset();
        Q = maze.getWidth() + maze.getLength();
        Route globalRoute = null;
        int globalMin = Integer.MAX_VALUE;
        double percentageSameRoute = 0.0;

        for(int j = 0; j < generations; j++) {
        	ArrayList<Route> genRoutes = new ArrayList<>();
            Route localRoute = null;
            int localMin = Integer.MAX_VALUE;
            
        	if (j >= generations * 0.7) {
                evaporation = 0.5;
            }
            
            // let all ants run through maze
            for (int i = 0; i < antsPerGen; i++) {
                Ant ant = new Ant(maze, spec);
                Route found = ant.findRoute();
                genRoutes.add(found);
                if (found.size() < localMin) {
                    localMin = found.size();
                    localRoute = found;
                }

                if (found.size() < globalMin) {
                    globalMin = found.size();
                    globalRoute = found;
                }
            }
            percentageSameRoute = 100 * Collections.frequency(genRoutes, localRoute) / (double) antsPerGen;
            maze.addPheromoneRoutes(genRoutes, Q);
            maze.evaporate(evaporation);
            System.out.println("Generation " + (j+1) + ": " + globalMin + ", " + percentageSameRoute + "% converged");

        }
        return globalRoute;
    }

    /**
     * Driver function for Assignment 1
     */
    public static void main(String[] args) throws FileNotFoundException {
    	//set parameters
    	int genSize = 100;
        int noGen = 100;
        double Q = 1000;
        double evap = 0.1;
        double eps = 0.1;

        //construct the optimization objects
        Maze maze = Maze.createMaze("./data/medium maze.txt");
        PathSpecification spec = PathSpecification.readCoordinates("./data/medium coordinates.txt");
        AntColonyOptimization aco = new AntColonyOptimization(maze, genSize, noGen, Q, evap, eps);
        
        //save starting time
        long startTime = System.currentTimeMillis();
        
        //run optimization
        Route shortestRoute = aco.findShortestRoute(spec);
        
        //print time taken
        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));
        
        //save solution
        shortestRoute.writeToFile("./data/x_solution.txt");
        
        //print route size
        System.out.println("Route size: " + shortestRoute.size());
        
    }

}
