package swe_mo.reply;

import java.io.FileNotFoundException;

import swe_mo.solver.SolverConfig;
import swe_mo.solver.de.Particle_DE;
import swe_mo.ui.clogger;

import java.util.ArrayList;




public class score {
	static Einlesen input = new Einlesen();
	
	public static void init(){
		try {
			input.Einlesen("C:\\Users\\david\\Desktop\\data_scenarios_a_example.in");
		} catch(Exception e) {
			clogger.err("RPL", "init", e);
		}
	}

	public static double calculate_score(Particle_DE vector) {	
		double totalScore = 0.0;
		double scoreMax = 0.0;
		boolean allConnected = true;
		
		for (Haus h : input.haeuser) {
			boolean connected = false;
			for(int i=0; i < input.antennen.length; i++) {
				double d = dist(h.x, h.y, vector.position.get(2*i), vector.position.get((2*i)+1));
				if (d<((double)input.antennen[i].range)) {
					connected = true;
					double sc = h.connectionSpeedWeight*input.antennen[i].speed-h.latencyWeight*d;
					if (sc>scoreMax) {
						scoreMax=sc;
					}	
				}
			}
			if(!connected) {
				allConnected=false;
			}
			totalScore+=scoreMax;

		}
		
		if(allConnected) {
			totalScore+=(double)input.reward;
		}
		
		
		 return totalScore;		 
	 }
	
	

	public static double dist(double x1, double y1, double x2, double y2) {
		return Math.abs(x1-x2)+Math.abs(y1-y2); //Manhattan distance


	public static SolverConfig defaultConfig() {		
		return new SolverConfig(27,input.numberOfAntenna*2,100,0.3,0.3,1000,input.height,input.width, 1.0, false, false);

	}
}
