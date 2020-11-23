
package swe_mo.solver.pso;

import java.util.ArrayList;

import swe_mo.solver.FitnessFunction;
import swe_mo.solver.de.CRN;
import swe_mo.solver.de.Particle_DE;

public class PSOparticle extends Particle_DE {
	
	
	ArrayList<Double> velocity = new ArrayList<Double>();
	ArrayList<Double> personalBestPosition = new ArrayList<Double>();
	ArrayList<Double> nc = new ArrayList<Double>();
	double personalMinimum;
	double w, cc, cs, dt,  min, max;
	int dimension;
	
	public PSOparticle(int dimension, double max, double min, double w, double cc, double cs, double dt) {
			//This constructor creates a particle with the given dimension 
			//and initializes all dimensions with a random number within the given bounds
		super(dimension, max, min);
		this.dimension=dimension;
		this.w=w;
		this.cc=cc; 
		this.cs=cs;
		this.dt=dt;
		this.min=min;
		this.max=max;
		velocity = new ArrayList<Double>(position);
		personalBestPosition = new ArrayList<Double>(position);

	}
	
	
	
	public PSOparticle(int dimension) {
		//This constructor creates a particle with the given dimension and initializes all dimensions with zero
		super(dimension);
		velocity = new ArrayList<Double>(position);
		personalBestPosition = new ArrayList<Double>(position);
	}
	
	
	public void updateVelocity(ArrayList<Double> socialComponent) {
		double rc=Math.random();
		double rs=Math.random();
		for(int i=0; i<velocity.size(); i++) {
			velocity.set(i, w*velocity.get(i)  
					+  cc*rc*(personalBestPosition.get(i)
							-position.get(i))  
					+  cs*rs*(socialComponent.get(i)
							-position.get(i)));
		}
		
	}
	
	
	public void updateVelocityNeighborhood() {
		double rc=Math.random();
		double rs=Math.random();
		for(int i=0; i<velocity.size(); i++) {
			velocity.set(i, w*velocity.get(i)  
					+  cc*rc*(personalBestPosition.get(i)
							-position.get(i))  
					+  cs*rs*(nc.get(i)
							-position.get(i)));
		}
		
	}
	
	
	public void updateVelocityDecay(ArrayList<Double> socialComponent, int numIter , int Iter, double decayStart, double decayEnd) {
		double rc=Math.random();
		double rs=Math.random();
		w=((decayEnd-decayStart)/numIter)*Iter+decayStart;
		for(int i=0; i<velocity.size(); i++) {
			velocity.set(i, w*velocity.get(i)  
					+  cc*rc*(personalBestPosition.get(i)
							-position.get(i))  
					+  cs*rs*(socialComponent.get(i)
							-position.get(i)));
		}
	}
	
	public void updatePersonalBestPosition(int ffID) {
		double minimum = FitnessFunction.solve(ffID, this);
		if(minimum<personalMinimum) {
			personalMinimum = minimum;
			personalBestPosition = new ArrayList<Double>(position);
		}
	}
	
	
	public void updatePosition() {
		for(int i=0; i<position.size(); i++) {
			//position.set(i, position.get(i) + velocity.get(i)*dt);
			if(position.get(i) + velocity.get(i)*dt < min) {
				//position.set(i, (Math.random()*(max-min))+min);
			}else if(position.get(i) + velocity.get(i)*dt > max) {
				//position.set(i, (Math.random()*(max-min))+min);
			}else {
			position.set(i, position.get(i) + velocity.get(i)*dt);
			}
		}
	}
	
	public void setNC(ArrayList<Double> nc) {
		for(int i=0; i < nc.size(); i++) {
			this.nc.set(i, nc.get(i));
		}
	}
	
}