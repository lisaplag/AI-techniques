import java.util.Random;

/**
 * Class that represents the ants functionality.
 */
public class Ant {
	
    private Maze maze;
    private Coordinate start;
    private Coordinate end;
    private Coordinate currentPosition;
    private static Random rand;

    /**
     * Constructor for ant taking a Maze and PathSpecification.
     * @param maze Maze the ant will be running in.
     * @param spec The path specification consisting of a start coordinate and an end coordinate.
     */
    public Ant(Maze maze, PathSpecification spec) {
        this.maze = maze;
        this.start = spec.getStart();
        this.end = spec.getEnd();
        this.currentPosition = start;
        if (rand == null) {
            rand = new Random();
        }
    }

    /**
     * Method that performs a single run through the maze by the ant.
     * @return The route the ant found through the maze.
     */
    public Route findRoute() {
        Route route = new Route(start);
        Direction lastDir = null;
        while(!currentPosition.equals(end)) {
            double r = rand.nextDouble();
            int x = currentPosition.getX();
            int y = currentPosition.getY();
            if (r <= 0.25 && lastDir != Direction.West && x < maze.getWidth() - 1) {
                route.add(Direction.East);
                currentPosition = currentPosition.add(Direction.East);
                lastDir = Direction.East;
            } else if (r <= 0.5 && lastDir != Direction.South && y > 0) {
                route.add(Direction.North);
                currentPosition = currentPosition.add(Direction.North);
                lastDir = Direction.North;
            } else if (r <= 0.75 && lastDir != Direction.North && y < maze.getLength() - 1) {
                route.add(Direction.South);
                currentPosition = currentPosition.add(Direction.South);
                lastDir = Direction.South;
            } else if(lastDir != Direction.East && x > 0){
                route.add(Direction.West);
                currentPosition = currentPosition.add(Direction.West);
                lastDir = Direction.West;
            }
            System.out.println(currentPosition.toString());
        }
        return route;
    }

    public Route findGreedyRoute() {
        return null;
    }
}

