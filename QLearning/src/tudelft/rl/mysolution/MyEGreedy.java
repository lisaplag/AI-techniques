package tudelft.rl.mysolution;

import java.util.ArrayList;

import tudelft.rl.Action;
import tudelft.rl.Agent;
import tudelft.rl.EGreedy;
import tudelft.rl.Maze;
import tudelft.rl.QLearning;

public class MyEGreedy extends EGreedy {

	@Override
	public Action getRandomAction(Agent r, Maze m) {
		//TODO to select an action at random in State s
		return null;
	}

	@Override
	public Action getBestAction(Agent r, Maze m, QLearning q) {
		
		//get information
		State s = m.getState(r.x, r.y);
		ArrayList<javax.swing.Action> validActions = m.getValidActions(r);
		double[] actionValues = getActionValues(s, validActions);
		
		//initialize best action to first valid action
		Action bestAction = validActions.get(0);
		double bestActionValue = actionValues[0];
		
		//loop through possible actions in State s
		for(int i = 1; i < validActions.size(); i++) {
			
			if(actionValues[i] > bestActionValue) {
				//update best action
				bestAction = validActions.get(i);
				bestActionValue = actionValues[i];				
			}
		}
		
		return bestAction;
	}

	@Override
	public Action getEGreedyAction(Agent r, Maze m, QLearning q, double epsilon) {
		//TODO to select between random or best action selection based on epsilon.
		return null;
	}

}
