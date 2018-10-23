import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class that holds all the maze data. This means the pheromones, the open and blocked tiles in the system as
 * well as the starting and end coordinates.
 */
public class Maze {
    private int width;
    private int length;
    private int[][] walls;
    private double[][] pheromones;
    private int[][] frequency;
    private Coordinate start;
    private Coordinate end;

    /**
     * Constructor of a maze
     * @param walls int array of tiles accessible (1) and non-accessible (0)
     * @param width width of Maze (horizontal)
     * @param length length of Maze (vertical)
     */
    public Maze(int[][] walls, int width, int length) {
        this.walls = walls;
        this.length = length;
        this.width = width;
        initializePheromones();
    }

    /**
     * Initialize pheromones to a start value.
     */
    private void initializePheromones() {
        pheromones = new double[width][length];
        frequency = new int[width][length];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                pheromones[i][j] = 0;
                frequency[i][j] = 0;
            }
        }
    }

    /**
     * Reset the maze for a new shortest path problem.
     */
    public void reset() {
        initializePheromones();
    }

    /**
     * Update the pheromones along a certain route according to a certain Q
     * @param r The route of the ants
     * @param Q Normalization factor for amount of dropped pheromone
     */
    public void addPheromoneRoute(Route r, double Q) {
        Coordinate currentPos = r.getStart();
        ArrayList<Direction> route = r.getRoute();
        ArrayList<Coordinate> visitedCoordinates = new ArrayList<>();
        visitedCoordinates.add(currentPos);
        
        int distance = route.size();
        
        for (Direction d : route) {            
            currentPos = currentPos.add(d);
            visitedCoordinates.add(currentPos);
            pheromones[currentPos.getX()][currentPos.getY()] += 1.0 / Math.pow(10, 30); 
            // eliminate loops from the route
            if (visitedCoordinates.contains(currentPos) == true) {
	           	int previousVisit = visitedCoordinates.indexOf(currentPos) + 1;            	
	           	visitedCoordinates.subList(previousVisit, visitedCoordinates.size()).clear();
           }
        }
        // only add pheromones to the route without loops
        for (Coordinate c : visitedCoordinates) {
        	pheromones[c.getX()][c.getY()] += (Q / distance);     	
        }
    }
    

    /**
     * Update pheromones for a list of routes
     * @param routes A list of routes
     * @param Q Normalization factor for amount of dropped pheromone
     */
    public void addPheromoneRoutes(List<Route> routes, double Q) {
        for (Route r : routes) {
            addPheromoneRoute(r, Q);
        }
    }

    /**
     * Evaporate pheromone
     * @param rho evaporation factor
     */
    public void evaporate(double rho) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                pheromones[i][j] *=  (1 - rho);
            }
        }
    }

    /**
     * Width getter
     * @return width of the maze
     */
    public int getWidth() {
        return width;
    }

    /**
     * Length getter
     * @return length of the maze
     */
    public int getLength() {
        return length;
    }

    /**
     * Walls getter.
     * @return the walls of the maze
     */
    public int[][] getWalls() { return walls; }


    /**
     * Returns a the amount of pheromones on the neighbouring positions (N/S/E/W).
     * @param position The position to check the neighbours of.
     * @return the pheromones of the neighbouring positions.
     */
    public SurroundingPheromone getSurroundingPheromone(Coordinate position) {
        if (position.xBetween(1, width-1) && position.yBetween(1, length-1)) {
            return new SurroundingPheromone(pheromones[position.getX()][position.getY() - 1],
                    pheromones[position.getX() + 1][position.getY()],
                    pheromones[position.getX()][position.getY() + 1],
                    pheromones[position.getX() - 1][position.getY()]);
        } else if (position.getX() <= 0 && position.getY() <= 0) {
            return new SurroundingPheromone(0,
                    pheromones[position.getX() + 1][position.getY()],
                    pheromones[position.getX()][position.getY() + 1],
                    0);
        } else if (position.getX() <= 0 && position.getY() >= length - 1) {
            return new SurroundingPheromone(pheromones[position.getX()][position.getY() - 1],
                    pheromones[position.getX() + 1][position.getY()],
                    0,
                    0);
        } else if (position.getX() >= width - 1 && position.getY() <= 0) {
            return new SurroundingPheromone(0,
                    0,
                    pheromones[position.getX()][position.getY() + 1],
                    pheromones[position.getX() - 1][position.getY()]);
        } else if (position.getX() >= width - 1 && position.getY() >= length -1){
            return new SurroundingPheromone(pheromones[position.getX()][position.getY() - 1],
                    0,
                    0,
                    pheromones[position.getX() - 1][position.getY()]);
        } else if (position.getY() <= 0) {
            return new SurroundingPheromone(0,
                    pheromones[position.getX() + 1][position.getY()],
                    pheromones[position.getX()][position.getY() + 1],
                    pheromones[position.getX() - 1][position.getY()]);
        } else if (position.getX() <= 0) {
            return new SurroundingPheromone(pheromones[position.getX()][position.getY() - 1],
                    pheromones[position.getX() + 1][position.getY()],
                    pheromones[position.getX()][position.getY() + 1],
                    0);
        } else if (position.getX() >= width - 1) {
            return new SurroundingPheromone(pheromones[position.getX()][position.getY() - 1],
                    0,
                    pheromones[position.getX()][position.getY() + 1],
                    pheromones[position.getX() - 1][position.getY()]);
        } else {
            return new SurroundingPheromone(pheromones[position.getX()][position.getY() - 1],
                    pheromones[position.getX() + 1][position.getY()],
                    0,
                    pheromones[position.getX() - 1][position.getY()]);
        }
    }

    /**
     * Pheromone getter for a specific position. If the position is not in bounds returns 0
     * @param pos Position coordinate
     * @return pheromone at point
     */
    public double getPheromone(Coordinate pos) {
        if ( inBounds(pos) == true) {
            return pheromones[pos.getX()][pos.getY()];
        }
        return 0;
    }
    
    /**
     * Frequency getter for a specific position. If the position is not in bounds returns 0
     * @param pos Position coordinate
     * @return frequency at point
     */
    public int getFrequency(Coordinate pos) {
        if ( inBounds(pos) == true) {
            return frequency[pos.getX()][pos.getY()];
        }
        return 0;
    }


    /**
     * Check whether a coordinate lies in the current maze.
     * @param position The position to be checked
     * @return Whether the position is in the current maze
     */
    private boolean inBounds(Coordinate position) {
        return position.xBetween(0, width) && position.yBetween(0, length);
    }

    /**
     * Representation of Maze as defined by the input file format.
     * @return String representation
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(width);
        sb.append(' ');
        sb.append(length);
        sb.append(" \n");
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++ ) {
                sb.append(walls[x][y]);
                sb.append(' ');
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Method that builds a mze from a file
     * @param filePath Path to the file
     * @return A maze object with pheromones initialized to 0's inaccessible and 1's accessible.
     */
    public static Maze createMaze(String filePath) throws FileNotFoundException {
        Scanner scan = new Scanner(new FileReader(filePath));
        int width = scan.nextInt();
        int length = scan.nextInt();
        int[][] mazeLayout = new int[width][length];
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                mazeLayout[x][y] = scan.nextInt();
            }
        }
        scan.close();
        return new Maze(mazeLayout, width, length);
    }
}
