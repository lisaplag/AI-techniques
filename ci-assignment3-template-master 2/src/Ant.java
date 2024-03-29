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
        Direction chosen;
        
        //While the ant has not reached the end yet
        while (!currentPosition.equals(end)) {
            chosen = null;
            ArrayList<Direction> possibleDirections = getValidDirections(currentPosition);
            
            // If there is only one possible direction, always pick that one.
            if ( possibleDirections.size() == 1) {
                chosen = possibleDirections.get(0);
            }
            else if (lastTaken != null && maze.getSurroundingPheromone(currentPosition).getTotalSurroundingPheromone()
                    - maze.getPheromone(currentPosition.subtract(lastTaken))<= 0 ) {
                //This block makes the ant take a random choice if there is no pheromone at all.
                int randomChoice = (int) Math.floor(rand.nextDouble() * possibleDirections.size());
                chosen = possibleDirections.get(randomChoice);
            } 
            else {
            	// This block chooses a direction based on their probabilities.
                // The total takes into account that the ant can't go back to where it came from.
                double total = maze.getSurroundingPheromone(currentPosition).getTotalSurroundingPheromone();
                if (lastTaken != null) {
                	total -= maze.getPheromone(currentPosition.subtract(lastTaken));                	
                }
            	double sum = 0;
            	double r = rand.nextDouble();
                while (chosen == null) {
                    for (Direction possibleDirection : possibleDirections) {
                        sum += maze.getPheromone(currentPosition.add(possibleDirection)) / total;
                        if (r <= sum) {
                            chosen = possibleDirection;
                            break;
                        }
                    }
                                       
                    //This block makes the ant choose a random direction without any pheromone with a certain probability.
                    //The direction is random because the list of possible directions was shuffled.
                    if (rand.nextDouble() <= 0.7) {
	                    for (Direction possibleDirection : possibleDirections) {
	                        if (maze.getPheromone(currentPosition.add(possibleDirection)) == 0) {
	                            chosen = possibleDirection;
	                            break;
	                        }
	                    }
                    }
                    
                }
            }
            lastTaken = chosen;
            route.add(chosen);
            currentPosition = currentPosition.add(chosen);
        }
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
        // check for possible directions and add them
        // by default it is not possible to go back to exactly where the ant came from
        if (x < maze.getWidth() - 1 && maze.getWalls()[x + 1][y] == 1 && lastTaken != Direction.West) {
        	possibleDirections.add(Direction.East);        	
        }

        if (x > 0 && maze.getWalls()[x - 1][y] == 1 && lastTaken != Direction.East) {
        	possibleDirections.add(Direction.West);        	
        }

        if (y < maze.getLength() - 1 && maze.getWalls()[x][y + 1] == 1 && lastTaken != Direction.North) {
        	possibleDirections.add(Direction.South);        	
        }

        if (y > 0 && maze.getWalls()[x][y - 1] == 1 && lastTaken != Direction.South) {
        	 possibleDirections.add(Direction.North);        	
        }
          
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
        //shuffle the list of possible directions.
        Collections.shuffle(possibleDirections);
        return possibleDirections;
    }


    /**
     * THIS IS NOT USED!!!!
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

