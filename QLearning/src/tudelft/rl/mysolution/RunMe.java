package tudelft.rl.mysolution;

import java.io.File;
import java.util.ArrayList;

import tudelft.rl.*;

public class RunMe {

	public static void main(String[] args) {
		
		//load the maze
		//TODO replace this with the location to your maze on your file system
		Maze maze = new Maze(new File(System.getProperty("user.dir")+"/data/easy_maze.txt"));

		
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
		int count = 0;
		while (!stop) {
			//TODO implement the action selection and learning cycle
			Action a = selection.getEGreedyAction(robot, maze, learn, 0.1);
			State s = robot.getState(maze);
			robot.doAction(a, maze);
			ArrayList<Action> choices = maze.getValidActions(robot);
			learn.updateQ(s, a, maze.getR(robot.getState(maze)), robot.getState(maze), choices, 0.7, 0.9);
			stepCount++;
			//TODO figure out a stopping criterion
			if(maze.getR(robot.getState(maze)) == 10) {
				robot.reset();
				count++;
			}
			if(count >= 15) {
				stop = true;
			}
		}


	}

}
