package tudelft.rl.mysolution;

import java.util.ArrayList;

import tudelft.rl.Action;
import tudelft.rl.Agent;
import tudelft.rl.EGreedy;
import tudelft.rl.Maze;
import tudelft.rl.QLearning;

import java.util.ArrayList;
import java.util.Random;

public class MyEGreedy extends EGreedy {

	@Override
	public Action getRandomAction(Agent r, Maze m) {
		ArrayList<Action> actions = m.getValidActions(r);
		Random random = new Random();
		int choice = (int) Math.ceil(random.nextDouble()*actions.size());
		return actions.get(choice - 1);
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
		double rand = Math.random();
        Action action;

        //Select the random action with probability epsilon.
        if (rand <= epsilon) {
            action = getRandomAction(r,m);
        } else {
		    action = getBestAction(r,m,q);
        }
        return action;
	}

}
