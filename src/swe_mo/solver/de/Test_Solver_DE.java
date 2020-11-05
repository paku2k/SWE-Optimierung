package swe_mo.solver.de;

public class Test_Solver_DE {
	
	public static void main(String[] args) {

		int N = 3;
		int NP = 10;
		double F = 0.7;
		double CR = 0.7;
		int maxGenerations = 10;
		int generation = 0;
		double upperBound = 5;
		double lowerBound = 5;
		FitnessFunction fF = new FitnessFunction();
		
		DErand1 rand1 = new DErand1(N, NP, F, CR, maxGenerations, upperBound, lowerBound, fF);
		
		Particle_DE p = rand1.calculateRandomDifference(2);

		Particle_DE part1=new Particle_DE(10, -1.0, 15);
		
		System.out.println(part1);
		
		Particle_DE part2=new Particle_DE(10, -1.0, 15);
		
		System.out.println(part2);
		
		part1.add(part2);
		
		System.out.println(part1);
		
		part1.substract(part2);
		
		System.out.println(part1);

	}

}
