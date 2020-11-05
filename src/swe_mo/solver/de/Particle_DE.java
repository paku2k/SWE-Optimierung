package swe_mo.solver.de;
import java.util.ArrayList;

public class Particle_DE {

	static int a = 0;
	ArrayList<Double> position = new ArrayList<Double>(); 
	
	
	public Particle_DE(int dimension, int max, int min) {
	    for(int i = dimension; i>0; i--) {
	    	position.add(CRN.rn(max, min));
	    }
	  }
	
}