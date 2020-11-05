package swe_mo.solver.de;

public class Test_Solver_DE {
	
	public static void main(String[] args) {
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
