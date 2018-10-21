import java.io.FileNotFoundException;
import java.util.ArrayList;

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
        Q = maze.getWidth() + maze.getLength();
        Route found;
        int min =1999999999;
        Route result = null;
        for (int j = 0; j < generations; j++) {
            counter = 0;
            if (j >= generations * 0.75) {
                evaporation = 0.0;
            }
            ArrayList<Route> genRoutes = new ArrayList<>();
            for (int i = 0; i < antsPerGen; i++) {
                Ant ant = new Ant(maze, spec);
                found = ant.findRoute();
                genRoutes.add(found);
               // System.out.println((i+1) + ", gen " + (j+1));
            }
            maze.addPheromoneRoutes(genRoutes, Q);
            maze.evaporate(evaporation);
            for (Route p: genRoutes) {
                if (p.cPath.size() < min) {
                    min = p.cPath.size();
                    result = makeRoute(p.getStart(), p.cPath);

                }
            }
            System.out.println("Generation " + (j+1) + ": " +min);

        }
        //Ant greedyAnt = new Ant(maze, spec);
        return result;
    }

    /**
     * Driver function for Assignment 1
     */
    public static void main(String[] args) throws FileNotFoundException {
    	//parameters
    	int genSize = 20;
        int noGen = 10;
        double Q = 1350;
        double evap = 0.01;
        
        //construct the optimization objects
        Maze maze = Maze.createMaze("./data/hard maze.txt");
        PathSpecification spec = PathSpecification.readCoordinates("./data/hard coordinates.txt");
        AntColonyOptimization aco = new AntColonyOptimization(maze, genSize, noGen, Q, evap);
        
        //save starting time
        long startTime = System.currentTimeMillis();
        
        //run optimization
        Route shortestRoute = aco.findShortestRoute(spec);
        
        //print time taken
        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));
        
        //save solution
        shortestRoute.writeToFile("./data/hard_solution.txt");
        
        //print route size
        System.out.println("Route size: " + shortestRoute.size());
    }

    public Route makeRoute(Coordinate start, ArrayList<Coordinate> path) {
        Route result = new Route(start);
        Coordinate currentPos = start;
        for (int i = 0; i < path.size() - 1; i++) {
            if (currentPos.add(Direction.North).equals(path.get(i + 1))) {
                result.add(Direction.North);
            } else if (currentPos.add(Direction.South).equals(path.get(i + 1))) {
                result.add(Direction.South);
            } else if (currentPos.add(Direction.East).equals(path.get(i + 1))) {
                result.add(Direction.East);
            } else {
                result.add(Direction.West);
            }
            currentPos = path.get(i + 1);
        }
        return result;
    }
}
