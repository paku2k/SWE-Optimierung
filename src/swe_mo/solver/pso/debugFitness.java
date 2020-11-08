package swe_mo.solver.pso;

import java.util.ArrayList;

public class debugFitness {

	public double calcSpehreFunction(int dimension, ArrayList<Double> position) {
		double val = 0;
		for(int i=0; i<dimension; i++) {
			val += position.get(i)*position.get(i);
		}
		return  val;
	}
	
}
