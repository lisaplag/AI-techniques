import java.util.ArrayList;
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
        double r = rand.nextDouble();
        while(!currentPosition.equals(end)) {
            int x = currentPosition.getX();
            int y = currentPosition.getY();
            ArrayList<Direction> possibleDirections = new ArrayList<>();
            if (x < maze.getWidth() - 1 && maze.getWalls()[x + 1][y] == 1)
                    possibleDirections.add(Direction.East);
            if (x > 0 && maze.getWalls()[x - 1][y] == 1)
                    possibleDirections.add(Direction.West);
            if (y < maze.getLength() - 1 && maze.getWalls()[x][y + 1] == 1)
                    possibleDirections.add(Direction.South);
            if (y > 0 && maze.getWalls()[x][y - 1] == 1)
                    possibleDirections.add(Direction.North);
            int randomChoice = (int) Math.floor(rand.nextDouble() * possibleDirections.size());
            Direction chosen = possibleDirections.get(randomChoice);
            double total = maze.getSurroundingPheromone(currentPosition).getTotalSurroundingPheromone();
            for (Direction possibleDirection : possibleDirections) {
                if (r <= maze.getPheromone(currentPosition.add(possibleDirection)) / total
                        || maze.getPheromone(currentPosition.add(possibleDirection)) / total == 0) {
                    chosen = possibleDirection;
                    break;
                } else {
                    r -= maze.getPheromone(currentPosition.add(possibleDirection)) / total;
                }
            }
           // chosen = possibleDirections.get(randomChoice);
            route.add(chosen);
            currentPosition = currentPosition.add(chosen);
            System.out.println(currentPosition.toString());
        }
        return route;
    }

    public Route findGreedyRoute() {
        Route route = new Route(start);
        Direction dir = null;
        Direction lastDir = Direction.North;
        while(!currentPosition.equals(end)) {
            double r = -99999999999.9;
            int x = currentPosition.getX();
            int y = currentPosition.getY();
            if (maze.getPheromone(currentPosition.add(Direction.East))> r && x < maze.getWidth() -1 && !lastDir.equals(Direction.West)) {
                r = maze.getPheromone(currentPosition.add(Direction.East));
                dir = Direction.East;
            }
            if (maze.getPheromone(currentPosition.add(Direction.North))> r && y > 0 && !lastDir.equals(Direction.South)) {
                r = maze.getPheromone(currentPosition.add(Direction.North));
                dir = Direction.North;
            }
            if (maze.getPheromone(currentPosition.add(Direction.South))> r && y < maze.getLength() -1 && !lastDir.equals(Direction.North)) {
                r = maze.getPheromone(currentPosition.add(Direction.South));
                dir = Direction.South;
            }
            if(maze.getPheromone(currentPosition.add(Direction.West)) > r && x > 0 && !lastDir.equals(Direction.East)){
                dir = Direction.West;
            }
            route.add(dir);
            currentPosition = currentPosition.add(dir);
            lastDir = dir;
            System.out.println(currentPosition.toString() + "(Greedy)");
        }
        return route;


    }
}

