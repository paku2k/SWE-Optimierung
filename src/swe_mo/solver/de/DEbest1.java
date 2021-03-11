package swe_mo.solver.de;

import java.io.IOException;

import swe_mo.solver.Convergence;
import swe_mo.solver.FileGenerator;


public class DEbest1 extends DErand1{
	
	
	public DEbest1(int N, int NP, double F, double CR, int maxGenerations, double upperBound, double lowerBound,
			int ffIndex, int solverID, double convergence, boolean printConvergenceFile, boolean printPositionFile) throws IOException {
		super(N, NP, F, CR, maxGenerations, upperBound, lowerBound, ffIndex, solverID, convergence, false, false);
		c= new Convergence("DEbest1Convergence", printConvergenceFile, convergence);
		if(printPositionFile) {
			String header = "generation;";
			for (int i=0; i<NP; i++) {
				header=header+"P"+i+"solution;";
				for (int j=0; j<N; j++) {
					header=header+"P"+i+"axis"+j+";";
				}
			}
			g = new FileGenerator("DEbest1_Positions_FFID"+ffIndex, header);
		}
	}
	
	
	public DEbest1(int N, int NP, double F, double CR, int maxGenerations, 
			int ffIndex, int solverID, double convergence, boolean printConvergenceFile, boolean printPositionFile) throws IOException {
		super(N, NP, F, CR, maxGenerations,  ffIndex, solverID, convergence, false, false);
		c= new Convergence("DEbest1Convergence", printConvergenceFile, convergence);
		if(printPositionFile) {
			String header = "generation;";
			for (int i=0; i<NP; i++) {
				header=header+"P"+i+"solution;";
				for (int j=0; j<N; j++) {
					header=header+"P"+i+"axis"+j+";";
				}
			}
			g = new FileGenerator("DEbest1_Positions_FFID"+ffIndex, header);
		}

	}
	
	@Override
	public Particle_DE calculateV(int index) {
		//calculates the Vector V for current generation
		Particle_DE p = this.calculateRandomDifference(index);
		
		p.multiply(this.F);
		p.add(bestParticle);
		for (int i = 0; i < p.position.size(); i++) {
			if((i%2)!=0) {
				if(p.position.get(i)>this.upperBound) {
					p.position.set(i, this.upperBound);
				}
				if(p.position.get(i)<0) {
					p.position.set(i, (double) 0);
				}
				System.out.println(p.position.get(i)+"  "+this.upperBound);
			}else{
				if(p.position.get(i)>this.lowerBound) {
					p.position.set(i, this.lowerBound);
				}
				if(p.position.get(i)<0) {
					p.position.set(i, (double) 0);
				}
				System.out.println(p.position.get(i)+"  "+this.lowerBound);
			}
		}
		System.out.println("---");
		
		return p;
	}
	
	
	

}
