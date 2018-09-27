package tudelft.rl.mysolution;

import java.io.File;

import tudelft.rl.*;

public class RunMe {

	public static void main(String[] args) {
		
		//load the maze
		//TODO replace this with the location to your maze on your file system
		Maze maze = new Maze(new File(System.getProperty("user.dir")+"/data/toy_maze.txt"));

		
		//Set the reward at the bottom right to 10
		maze.setR(maze.getState(9, 9), 10);
				
		//create a robot at starting and reset location (0,0) (top left)
		Agent robot=new Agent(0,0);
		
		//make a selection object (you need to implement the methods in this class)
		EGreedy selection=new MyEGreedy();
		
		//make a Qlearning object (you need to implement the methods in this class)
		QLearning learn=new MyQLearning();
		
		boolean stop=false;
		
		//keep learning until you decide to stop
		int stepCount = 0;
		while (!stop) {
			//TODO implement the action selection and learning cycle
			robot.doAction(selection.getRandomAction(robot, maze), maze);
			System.out.println(robot.x + "," + robot.y + "  " + maze.getState(robot.x, robot.y).type);
			if(maze.getR(robot.getState(maze)) == 10) {
				robot.x = 0;
				robot.y = 0;
				stop = true;
				System.out.println("Found the exit in " + stepCount + " steps!");
			}
			stepCount++;
			//TODO figure out a stopping criterion
			if(stepCount >= 30000) {
				stop = true;
			}
		}

	}

}
