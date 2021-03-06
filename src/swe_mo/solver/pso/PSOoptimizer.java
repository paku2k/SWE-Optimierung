package swe_mo.solver.pso;

import java.io.*;

import swe_mo.solver.SolverResult;

public class PSOoptimizer {
	
	int dimension;
	int numIter;
	int particleCount;
	int ffID;
	int solverID;
	double min;
	double max;
	double dt;
	String filename;
	
	public PSOoptimizer(String filename, int dimension, double min, double max, int particleCount, double dt, int numIter, int ffID, int solverID) {
		
		this.ffID = ffID;
		this.solverID = solverID;
		
		this.dimension = dimension;
		this.numIter = numIter;
		this.particleCount = particleCount;
		this.min = min;
		this.max = max;
		this.dt = dt;
		this.filename = new String(filename);
		
	}

	public void psoGlobalLinear3DimensionalGridSearch(int resolution, double w_upper, double w_lower, double cc_upper, double cc_lower, double cs_upper, double cs_lower) throws Exception {
		
		FileWriter author = new FileWriter(filename+".txt");
		
		double w_step = (w_upper - w_lower)/resolution;
		double cc_step = (cc_upper - cc_lower)/resolution;
		double cs_step = (cs_upper - cs_lower)/resolution;
		
		for(int i=0; i<resolution; i++) {
			for(int j=0; j<resolution; j++) {
				for(int k=0; k<resolution; k++) {
					
					PSOgsc psoGlobalSolver = new PSOgsc(dimension, min, max, particleCount, (w_lower+i*w_step), (cc_lower+j*cc_step), (cs_lower+k*cs_step), dt, numIter, ffID, 1.0, solverID, false, false);
					SolverResult sr = psoGlobalSolver.solve();
					author.write("Hyperparameter:	w="+(w_lower+i*w_step)+";cc="+(cc_lower+j*cc_step)+";cs="+(cs_lower+k*cs_step)+"	Minimum:	"+sr.value+"	Position:	"+sr.returnPosition.toString()+"\n");

				}
			}
		}

		author.close();
		
	}
	
}
