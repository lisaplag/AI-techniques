package tudelft.rl.mysolution;

import tudelft.rl.*;

import java.util.ArrayList;
import java.util.Random;

public class MyEGreedy extends EGreedy {

    /**
     * A method that returns a random valid action.
     * @param r the agent that is taking the action
     * @param m the maze the agent is in
     * @return a random valid action
     */
	@Override
	public Action getRandomAction(Agent r, Maze m) {
		ArrayList<Action> actions = m.getValidActions(r);
		Random random = new Random();
		int choice = (int) Math.ceil(random.nextDouble()*actions.size());
		return actions.get(choice - 1);
	}

    /**
     * A method that returns the best action.
     * @param r the agent that is taking the action
     * @param m the maze the agent is in
     * @param q the learning object the agent uses
     * @return the best action
     */
	@Override
	public Action getBestAction(Agent r, Maze m, QLearning q) {
		
		//get information
		State s = r.getState(m);
		ArrayList<Action> validActions = m.getValidActions(r);
		double[] actionValues = q.getActionValues(s, validActions);
		
		//initialize best action to a random valid action
		Random random = new Random();
		int randomIndex = random.nextInt(validActions.size());
		Action bestAction = validActions.get(randomIndex);
		double bestActionValue = actionValues[randomIndex];
		
		//loop through all possible actions in State s
		for(int i = 0; i < validActions.size(); i++) {
			if(actionValues[i] > bestActionValue) {
				//update best action
				bestAction = validActions.get(i);
				bestActionValue = actionValues[i];				
			}
		}
		
		return bestAction;
	}

	/**
	 * A method that returns an action, based on the greedy algorithm.
     * It uses a random action with probability epsilon,
     * otherwise it uses the best action.
	 * @param r the agent that is taking the action
	 * @param m the maze the agent is in
	 * @param q the learning object the agent uses
	 * @param epsilon the chance a random action is picked
	 * @return the selected action
	 */
	@Override
	public Action getEGreedyAction(Agent r, Maze m, QLearning q, double epsilon) {
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
