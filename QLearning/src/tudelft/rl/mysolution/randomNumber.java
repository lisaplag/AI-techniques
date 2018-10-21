package tudelft.rl.mysolution;
import java.util.Random;

public class randomNumber {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random random = new Random();
		int choice = (int) Math.ceil(random.nextDouble()*3);
		int country = (int) Math.ceil(random.nextDouble()*2);
		System.out.println(choice); 
		System.out.println(country);
	}

}
