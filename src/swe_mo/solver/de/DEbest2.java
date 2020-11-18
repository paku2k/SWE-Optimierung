package swe_mo.solver.de;

public class DEbest2 extends DEbest1{

	
	public DEbest2(int N, int NP, double F, double CR, int maxGenerations, double upperBound, double lowerBound,
			int ffIndex, int solverID) {
		super(N, NP, F, CR, maxGenerations, upperBound, lowerBound, ffIndex, solverID);
		// TODO Auto-generated constructor stub
	}
	
	
	public DEbest2(int N, int NP, double F, double CR, int maxGenerations, 
			int ffIndex, int solverID) {
		super(N, NP, F, CR, maxGenerations,  ffIndex, solverID);
		// TODO Auto-generated constructor stub
	}
			
		

	
	
	@Override
	public Particle_DE calculateV(int index) {
		//calculates the Vector V for current generation
		Particle_DE p = this.calculateRandomDoubleDifference(index);
		p.multiply(this.F);
		p.add(bestParticle);

	
		for (int i = 0; i < p.position.size(); i++) {
			if(p.position.get(i)>this.upperBound) {
				p.position.set(i, this.upperBound);
			}
			if(p.position.get(i)<this.lowerBound) {
				p.position.set(i, this.lowerBound);
			}
		}
		
		
		return p;
	}
		
	
	public Particle_DE calculateRandomDoubleDifference(int skip) {
		//Calculates a random difference between two vectors of the population and returns it as a new vector
		//skip defines, what index to skip (because it belongs to the original vector)
		int index1;
		int index2;
		int index3;
		int index4;
		Particle_DE newP = new Particle_DE(this.N);
		
		do {
			index1 = CRN.rInt(0, (int)NP-1);
		}
		while (index1 == skip);
		
		do {
			index2 = CRN.rInt(0, (int)NP-1);
		}
		while (index2 == skip || index2 == index1);
		
		do {
			index3 = CRN.rInt(0, (int)NP-1);
		}
		while (index3 == skip || index3 == index1 || index3 == index2);
		
		do {
			index4 = CRN.rInt(0, (int)NP-1);
		}
		while (index4 == skip || index4 == index1 || index4 == index2 || index4 == index3);
		
		newP = new Particle_DE(xPop.get(index1));
		newP.add(xPop.get(index2));
		newP.substract(xPop.get(index3));
		newP.substract(xPop.get(index4));
		
		return newP;
	}
	
	
	
}
