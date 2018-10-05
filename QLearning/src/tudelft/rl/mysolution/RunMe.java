package tudelft.rl.mysolution;

import java.io.File;
import java.util.ArrayList;

import tudelft.rl.*;

public class RunMe {

	public static void main(String[] args) {
		
		//load the maze
		//TODO replace this with the location to your maze on your file system
		Maze maze = new Maze(new File(System.getProperty("user.dir")+"/data/toy_maze.txt"));

		
		//Set the reward at the bottom right to 10
		maze.setR(maze.getState(9, 9), 10);
		maze.setR(maze.getState(9, 0), 5);
				
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
		int total = 0;
		int topCount = 0;
		int botCount = 0;
		double epsilon = 0.1;
		while (!stop) {
			//TODO implement the action selection and learning cycle
			Action a = selection.getEGreedyAction(robot, maze, learn, epsilon);
			State s = robot.getState(maze);
			robot.doAction(a, maze);
			ArrayList<Action> choices = maze.getValidActions(robot);
			learn.updateQ(s, a, maze.getR(robot.getState(maze)), robot.getState(maze), choices, 0.7, 0.9);
			stepCount++;
			// When a goal is found, reset the agent and update epsilon
			if(robot.x == 9 && (robot.y == 9 || robot.y == 0)) {
				if(robot.y == 0) {
					System.out.println("Top");
				} else {
					System.out.println("Bottom");
				}
				if (robot.y != 9) {
					topCount++;
				} else {
					botCount++;
				}
				if (epsilon != 0.7) {
					if (topCount > 5 && botCount < 5) {
						epsilon = 0.7;
					}
				} else if (topCount > 5 && botCount > 5){
					epsilon = 0.1;
				} else {
					epsilon = 0.5;
				}


				robot.reset();
				count++;
				System.out.println(count);
				total = stepCount;
			}
			if(count >= 50) {
				stop = true;
			}
		}
		System.out.println("Found the exit " + count + " times with an average of " + (total / count)
				+ " steps." + topCount + ", " + botCount + "\nepsilon: " + epsilon);
	}

}
