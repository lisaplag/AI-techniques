package tudelft.rl.mysolution;

import java.util.ArrayList;

import tudelft.rl.Action;
import tudelft.rl.QLearning;
import tudelft.rl.State;

public class MyQLearning extends QLearning {

	@Override
	public void updateQ(State s, Action a, double r, State s_next, ArrayList<Action> possibleActions, double alpha, double gamma) {
		// TODO Auto-generated method stub
		Action best = new Action("down");
		double max = Double.MIN_VALUE;
		for ( Action act : possibleActions) {
			if ( getQ(s_next, act) > max) {
				best = act;
			}
		}
		double result = getQ(s, a) + alpha * (r + gamma * getQ(s_next, best) - getQ(s, a));
		//System.out.println(result);
		this.setQ(s, a, result);
	}

}
