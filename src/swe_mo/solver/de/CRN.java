package swe_mo.solver.de;
import  java.lang.Math;

public class CRN {
	//Create_ Random_Number-Class
	public static double rn(double max, double min)	{
		double random = (Math.random()*(max-min))+min;
		return random;
	}

	public static int rInt(int max, int min) {
		//both bounds are included in the number generation
		if (max==min) return max;
		return (int)rn((double)max+1.0, (double)min);

	}
}
