package swe_mo.solver.de;
import java.util.ArrayList;

public class Particle_DE {

	ArrayList<Double> position = new ArrayList<Double>(); 
	
	
	public Particle_DE(int dimension, double max, double min) {
		//This constructor creates a particle with the given dimension 
		//and initializes all dimensions with a random number within the given bounds

	    for(int i = dimension; i>0; i--) {
	    	position.add(CRN.rn(max, min));
	    }
	  }
	
	public Particle_DE(int dimension) {
		//This constructor creates a particle with the given dimension and initializes all dimensions with zero
	    for(int i = dimension; i>0; i--) {
	    	position.add(0.0);
	    }
	  }
	
}