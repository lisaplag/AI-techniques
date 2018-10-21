import java.util.ArrayList;
import java.util.Collections;
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
    public ArrayList<Coordinate> path;
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
        path = new ArrayList<>();
    }

    /**
     * Method that performs a single run through the maze by the ant.
     *
     * @return The route the ant found through the maze.
     */
    public Route findRoute() {
        double Modifier = 1;
        Route route = new Route(start);
        Direction chosen;
        //While the and has not reached the end yet
        while (!currentPosition.equals(end)) {
            chosen = null;
            double r = rand.nextDouble();
            ArrayList<Direction> possibleDirections = getValidDirections(currentPosition);
            //shuffle the list of possible directions.
            Collections.shuffle(possibleDirections);
            if (lastTaken != null && maze.getSurroundingPheromone(currentPosition).getTotalSurroundingPheromone()
                    - maze.getPheromone(currentPosition.add(lastTaken.opposite()))<= 0 ) {
                //This block makes the ant take a random route if there is no pheromone at all.
                int randomChoice = (int) Math.floor(rand.nextDouble() * possibleDirections.size());
                chosen = possibleDirections.get(randomChoice);

            } else {
                // This total takes into account that the ant can't go back to where it came from.
                double total = maze.getSurroundingPheromone(currentPosition).getTotalSurroundingPheromone();
                if (lastTaken != null)
                    total -= maze.getPheromone(currentPosition.add(lastTaken.opposite()));
                double sum = 0;
                // If there is only one possible direction, always pick that one.
                if ( possibleDirections.size() == 1) {
                    sum = 1.0;
                } else if (possibleDirections.get(0) != lastTaken) {
                    Collections.shuffle(possibleDirections);
                }
                // Go straight if possible in an open area
                if (possibleDirections.contains(lastTaken) && openArea(possibleDirections)){
                    chosen = lastTaken;
                }
                while (chosen == null) {
                    // This block chooses a direction based on their probabilities.
                    for (Direction possibleDirection : possibleDirections) {
                        sum += maze.getPheromone(currentPosition.add(possibleDirection)) / total;
                        // This if statement is redundant for now
                        if (path.contains(currentPosition.add(possibleDirection)) && possibleDirections.size() > 1) {
                            sum -= Modifier;
                            if (r <= sum) {
                                chosen = possibleDirection;
                                break;
                            }
                            sum += Modifier;
                        } else {
                            if (r <= sum) {
                                chosen = possibleDirection;
                                break;
                            }
                        }
                    }

                    //This block makes the ant prefer to go in a random direction iff there is no pheromone in that direction.
                    for (Direction possibleDirection : possibleDirections) {
                        if (maze.getPheromone(currentPosition.add(possibleDirection)) == 0) {
                            chosen = possibleDirection;
                            break;
                        }
                    }
                }
            }
            lastTaken = chosen;
            route.add(chosen);
            currentPosition = currentPosition.add(chosen);
            path.add(currentPosition);
            //System.out.println(maze.getPheromone(currentPosition));
            //System.out.println(currentPosition.toString());
            //System.out.println("Found the exit and added pheromone " + AntColonyOptimization.counter + " times.");
        }
        AntColonyOptimization.counter++;
        return route;
    }

    /**
     * Method that returns all valid directions the ant can take,
     * based on its current position.
     *
     * @return the valid directions
     */
    public ArrayList<Direction> getValidDirections(Coordinate position) {
        int x = position.getX();
        int y = position.getY();
        ArrayList<Direction> possibleDirections = new ArrayList<>();
        if (x < maze.getWidth() - 1 && maze.getWalls()[x + 1][y] == 1 && lastTaken != Direction.West)
               // && !deadEnd(currentPosition.add(Direction.East)))
            possibleDirections.add(Direction.East);
        if (x > 0 && maze.getWalls()[x - 1][y] == 1 && lastTaken != Direction.East)
               // && !deadEnd(currentPosition.add(Direction.West)))
            possibleDirections.add(Direction.West);
        if (y < maze.getLength() - 1 && maze.getWalls()[x][y + 1] == 1 && lastTaken != Direction.North)
               // && !deadEnd(currentPosition.add(Direction.South)))
            possibleDirections.add(Direction.South);
        if (y > 0 && maze.getWalls()[x][y - 1] == 1 && lastTaken != Direction.South)
               // && !deadEnd(currentPosition.add(Direction.North)))
            possibleDirections.add(Direction.North);

        // This block handles dead ends.
        // If there are no possible routes, add the position we came from
        // as a possible direction.
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
        Collections.shuffle(possibleDirections);
        return possibleDirections;
    }

    public boolean openArea(ArrayList<Direction> dirs) {
        Coordinate test = currentPosition;
        for (Direction d: dirs) {
            test = test.add(d);
            if (maze.getWalls()[test.getX()][test.getY()] == 0){
                return false;
            }
        }
        return maze.getWalls()[test.getX()][test.getY()] == 1;
    }

    /**
     * Method that finds directions with the most pheromone
     *
     * @return the route with the most pheromone
     */
    public Route findGreedyRoute() {
        Route route = new Route(start);
        Direction dir = null;
        while (!currentPosition.equals(end)) {
            double r = -99999999999.9;
            ArrayList<Direction> possibleDirections = getValidDirections(currentPosition);
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

