package swe_mo.solver.de;
import  java.lang.Math;

public class CRN {
	//Create_ Random_Number-Class
	public static double rn(int max, int min)	{
		double random = (Math.random()*((max-min)+1))+min;
		return random;
	}
}
