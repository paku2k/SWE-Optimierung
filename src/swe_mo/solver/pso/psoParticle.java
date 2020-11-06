
package swe_mo.solver.pso;

import java.util.ArrayList;

import swe_mo.solver.de.CRN;
import swe_mo.solver.de.Particle_DE;

public class psoParticle extends Particle_DE {
	
	
	ArrayList<Double> velocity = new ArrayList<Double>();
	ArrayList<Double> personalBestPosition = new ArrayList<Double>();
	double personalMinimum;
	double w, cc, cs;	
	
	
	public psoParticle(int dimension, double max, double min) {
			//This constructor creates a particle with the given dimension 
			//and initializes all dimensions with a random number within the given bounds
		super(dimension, max, min);
		UpdateVelocity();
	}
		
	
	public psoParticle(int dimension) {
		//This constructor creates a particle with the given dimension and initializes all dimensions with zero
		super(dimension);
		UpdateVelocity();
	}
	
	
	public void UpdateVelocity() {
		if(velocity.isEmpty()) {		//Initializes first velocity.
			this.velocity=this.position;
		}else {							//Velocity update
			
		}
	}
		
	
	public void updatePersonalBestPosition() {
		this.personalBestPosition=this.position;
	}
}
