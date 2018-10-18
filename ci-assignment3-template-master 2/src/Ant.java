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
    private Direction lastTaken;

    /**
     * Constructor for ant taking a Maze and PathSpecification.
     *
     * @param maze Maze the ant will be running in.
     * @param spec The path specification consisting of a start coordinate and an end coordinate.
     */
    public Ant(Maze maze, PathSpecification spec) {
        this.maze = maze;
        this.start = spec.getStart();
        this.end = spec.getEnd();
        this.currentPosition = start;
        lastTaken = null;
        if (rand == null) {
            rand = new Random();
        }
    }

    /**
     * Method that performs a single run through the maze by the ant.
     *
     * @return The route the ant found through the maze.
     */
    public Route findRoute() {
        Route route = new Route(start);
        Direction chosen = Direction.South;
        while (!currentPosition.equals(end)) {
            double r = rand.nextDouble();
            ArrayList<Direction> possibleDirections = getValidDirections();
            if (maze.getSurroundingPheromone(currentPosition).getTotalSurroundingPheromone() == 0) {
                int randomChoice = (int) Math.floor(rand.nextDouble() * possibleDirections.size());
                chosen = possibleDirections.get(randomChoice);
                lastTaken = chosen;
            } else {
                double total = maze.getSurroundingPheromone(currentPosition).getTotalSurroundingPheromone();
                double sum = 0;
                if ( possibleDirections.size() == 1)
                    sum = 1.0;
                if ( lastTaken == Direction.West)
                    total -= maze.getPheromone(currentPosition.add(Direction.East));
                if ( lastTaken == Direction.East)
                    total -= maze.getPheromone(currentPosition.add(Direction.West));
                if ( lastTaken == Direction.South)
                    total -= maze.getPheromone(currentPosition.add(Direction.North));
                if ( lastTaken == Direction.North)
                    total -= maze.getPheromone(currentPosition.add(Direction.South));
                for (Direction possibleDirection : possibleDirections) {
                    sum += maze.getPheromone(currentPosition.add(possibleDirection)) / total;
                    if (r <= sum) {
                        chosen = possibleDirection;
                        lastTaken = chosen;
                        break;
                    }
                }
                for (Direction possibleDirection : possibleDirections) {
                    if ( maze.getPheromone(currentPosition.add(possibleDirection)) == 0) {
                        chosen = possibleDirection;
                        lastTaken = chosen;
                    }
                }
            }
            route.add(chosen);
            currentPosition = currentPosition.add(chosen);
            //System.out.println(maze.getPheromone(currentPosition));
            //System.out.println(currentPosition.toString());
            //System.out.println("Found the exit and added pheromone " + AntColonyOptimization.counter + " times.");
        }
        AntColonyOptimization.counter++;

        return route;
    }

    public ArrayList<Direction> getValidDirections() {
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        ArrayList<Direction> possibleDirections = new ArrayList<>();
        if (x < maze.getWidth() - 1 && maze.getWalls()[x + 1][y] == 1 && lastTaken != Direction.West)
            possibleDirections.add(Direction.East);
        if (x > 0 && maze.getWalls()[x - 1][y] == 1 && lastTaken != Direction.East)
            possibleDirections.add(Direction.West);
        if (y < maze.getLength() - 1 && maze.getWalls()[x][y + 1] == 1 && lastTaken != Direction.North)
            possibleDirections.add(Direction.South);
        if (y > 0 && maze.getWalls()[x][y - 1] == 1 && lastTaken != Direction.South)
            possibleDirections.add(Direction.North);
        if (possibleDirections.size() == 0) {
            if (lastTaken == Direction.South)
                possibleDirections.add(Direction.North);
            if (lastTaken == Direction.West)
                possibleDirections.add(Direction.East);
            if (lastTaken == Direction.East)
                possibleDirections.add(Direction.West);
            if (lastTaken == Direction.North)
                possibleDirections.add(Direction.South);
        }
        return possibleDirections;
    }

    public Route findGreedyRoute() {
        Route route = new Route(start);
        Direction dir = null;
        while (!currentPosition.equals(end)) {
            double r = -99999999999.9;
            ArrayList<Direction> possibleDirections = getValidDirections();
            for (Direction d : possibleDirections) {
                if ( maze.getPheromone(currentPosition.add(d)) > r) {
                    r = maze.getPheromone(currentPosition.add(d));
                    dir = d;
                }
            }
            route.add(dir);
            currentPosition = currentPosition.add(dir);
        }
        return route;


    }
}

