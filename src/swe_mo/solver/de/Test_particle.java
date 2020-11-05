package swe_mo.solver.de;


public class Test_particle {

	public static void main(String[] args) {
		
		int dimension = 3;
		int min = 0;
		int max = 10;
		
		System.out.println("Hello World!");
		System.out.println("Der Partikel hat das Attribut a mit dem Wert: "+ Particle_DE.a);
		
		//for(int i = 0; i<10; i++) {
		//	System.out.println("Random Number Methode: "+ CRN.rn(max, min));
		//}
		
		Particle_DE p = new Particle_DE(dimension, min, max);
		System.out.println("Die Position des Partikels ist: " + p.position);
	}

}
