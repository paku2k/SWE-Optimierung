
package swe_mo.solver.pso;

import java.util.ArrayList;
import java.util.Random;

import swe_mo.solver.FitnessFunction;
import swe_mo.solver.de.CRN;
import swe_mo.solver.de.Particle_DE;

public class PSOparticle extends Particle_DE {
	
	
	ArrayList<Double> velocity = new ArrayList<Double>();
	ArrayList<Double> personalBestPosition = new ArrayList<Double>();
	ArrayList<Double> nc = new ArrayList<Double>();
	ArrayList<Integer> neighborhood = new ArrayList<Integer>();
	double personalMinimum= Double.MAX_VALUE /10;
	double w, cc, cs, dt,  min, max;
	double ncMinimum = Double.MAX_VALUE;
	int dimension, particleCount;
	
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
		for(int i = dimension; i>0; i--) {
	    	velocity.add(CRN.rn(max, min));
	    }
		personalBestPosition = new ArrayList<Double>(position);
		nc = new ArrayList<Double>(position);

	}
	

	//clone
	public PSOparticle(PSOparticle clone) {
		super(clone);
	}
	

	public PSOparticle(int dimension, double max, double min, double w, double cc, double cs, double dt, int particleCount, int neighbors) {
		//This constructor creates a particle with the given dimension 
		//and initializes all dimensions with a random number within the given bounds
	this(dimension, max, min, w, cc, cs, dt);
	this.particleCount=particleCount;
	setNeighbors(neighbors);
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
	
	
	public void updateVelocityNSC() {
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
	
	
	public void updateVelocityGNSC(ArrayList<Double> socialComponent) {
		double rc=Math.random();
		double rs=Math.random();
		for(int i=0; i<velocity.size(); i++) {
			velocity.set(i, w*velocity.get(i)  
					+  cc*rc*(personalBestPosition.get(i)
							-position.get(i))  
					+  cs*rs*((nc.get(i)
							-position.get(i)) + (socialComponent.get(i) - position.get(i))));
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
	
	public void updatePersonalBestPosition(int ffID) throws Exception {
		double minimum = FitnessFunction.solve(ffID, this);
		if(minimum < personalMinimum) {
			personalMinimum = minimum;
			personalBestPosition = new ArrayList<Double>(position);
		}
	}
	
	
	public void updatePosition() {
		for(int i=0; i<position.size(); i++) {																	//This method only resets the position if the position remains within the bounds. 
			if((position.get(i) + velocity.get(i)*dt) <= min) {
				//System.out.println("out of bounds: " + (position.get(i) + velocity.get(i)*dt));				
			}else if((position.get(i) + velocity.get(i)*dt) >= max) {
				//System.out.println("out of bounds: " + (position.get(i) + velocity.get(i)*dt));
			}else {
			position.set(i, position.get(i) + velocity.get(i)*dt);
			}
		}
		
		/*for(int i=0; i<position.size(); i++) {																		//This method sets the position randomly if the position is outside the bounds.
			position.set(i, position.get(i) + (velocity.get(i)*dt));
			if(position.get(i) < min) {
				System.out.println("-out of bounds: " + (position.get(i)));
				position.set(i, (Math.random()*(max-min))+min);
			}else if(position.get(i) > max) {
				System.out.println("+out of bounds: " + (position.get(i)));
				position.set(i, (Math.random()*(max-min))+min);
			}
		}*/
	}
		
	
	public void setNC(ArrayList<Double> nc) {
		for(int i=0; i < nc.size(); i++) {
			this.nc.set(i, nc.get(i));
		}
	}
	
	
	public void setNeighbors(int neighbors){
		for(int i=0; i<(neighbors-1);i++) {
			Random r = new Random();
			neighborhood.add(r.nextInt(particleCount-1));
		}
	}
}