package swe_mo.optimizer.algorithms;

import java.util.ArrayList;

import swe_mo.Settings;
import swe_mo.optimizer.OptimizerConfig;
import swe_mo.optimizer.OptimizerManager;
import swe_mo.optimizer.OptimizerResult;
import swe_mo.solver.FileGenerator;
import swe_mo.solver.FitnessFunction;
import swe_mo.solver.SolverManager;
import swe_mo.solver.SolverResult;
import swe_mo.solver.de.CRN;

public class RandomSearch extends BaseOptimizer{
	
	ArrayList<Double> parametersMin = new ArrayList<Double>();
	ArrayList<Double> parametersMax = new ArrayList<Double>();
	ArrayList<String> parametersName = new ArrayList<String>();
	int guesses;
	int dimensions;
	int optimizerID;
	double lowerBound;
	double upperBound;
	String solverType;
	boolean printfile;
	
	public static OptimizerConfig defaultConfig() throws Exception {
		return new OptimizerConfig(1, 200, (String)Settings.get("defaultAlgorithm"), 30, 10000, FitnessFunction.getBoundary("lower", 1), FitnessFunction.getBoundary("upper", 1), false);
	}
	
	public RandomSearch(int ffID, String solverType, int dimensions, int numberIterations, double lowerBound, double upperBound, ArrayList<Double> parametersMin, ArrayList<Double> parametersMax, ArrayList<String> parametersName, int guesses, boolean printfile, int optimizerID) throws Exception {
		super(ffID, numberIterations);
		this.parametersMin = parametersMin;
		this.parametersMax = parametersMax;
		this.parametersName = parametersName;
		this.guesses = guesses;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.dimensions = dimensions;
		this.solverType = solverType;
		this.optimizerID = optimizerID;
		this.printfile = printfile;
		
		if(parametersName.size()<1) {
			throw new Exception("No solver hyperparameter given.");
		}
		
	}

	public OptimizerResult optimize() throws Exception {
		
		ArrayList<Double> tempParametersMin = new ArrayList<Double>(parametersMin);
		ArrayList<Double> tempParametersMax = new ArrayList<Double>(parametersMax);
		double [] p = new double[parametersName.size()];
		double [] bestSolution = new double[parametersName.size()+1];

		int solverID = SolverManager.create("O-"+optimizerID, solverType);
		SolverManager.configure(solverID, "ffid="+ffID+", N="+dimensions+" maxGenerations="+numberIterations+", lowerBound="+lowerBound+", upperBound="+upperBound+", convergence=0");

		FileGenerator file = null;
		if(printfile) {
			String fileheader = "iteration; ffCalls; minimum";
			for(int a = 0; a < parametersName.size(); a++) {
				fileheader += "; "+parametersName.get(a);
			}
			file = new FileGenerator("RandomSearch_"+solverType, fileheader);				
		}
		
		for(int j = 0; j < guesses && !OptimizerManager.checkTerminated(optimizerID); j++) {
			System.gc();

			// define parameters for search
			for(int k = 0; k < parametersName.size(); k++) {	
				if(tempParametersMax.get(k) == tempParametersMin.get(k)) {
					p[k] = tempParametersMax.get(k);
					continue;
				}
				p[k] = CRN.rn(tempParametersMax.get(k), tempParametersMin.get(k));
			}
			
			//calculate solution
			String s = "";
			for(int a=0; a<parametersName.size(); a++) {						
				s += parametersName.get(a)+"="+p[a]+" ";
			}
			if(SolverManager.status(solverID) >= 0)
				SolverManager.clear(solverID);	
			SolverManager.configure(solverID, s);
			SolverManager.start(solverID);

			while(!OptimizerManager.checkTerminated(optimizerID)) {
				double status = SolverManager.status(solverID);
				s = ""+status;
				if (status >= 100) {break;}
			}

			if(!OptimizerManager.checkTerminated(optimizerID)) {
				SolverResult sr = SolverManager.result(solverID);
				
				
				String filecontent = sr.iterations+"; "+sr.ffCounter+"; "+sr.value;
				for(int a = 0; a < parametersName.size(); a++) {
					filecontent += "; "+p[a];
				}
				if(printfile)
					file.write(filecontent);
				
				// track best solution
				if(j==0) {
					bestSolution[0] = sr.value; 
					for(int l = 0; l < parametersName.size(); l++) {
						bestSolution[l+1] = p[l];
					}
				}
				else {
					if(sr.value<bestSolution[0]) {
						bestSolution[0] = sr.value; 
						for(int m = 0; m < parametersName.size(); m++) {
							bestSolution[m+1] = p[m];
						}
					}
				}
				
				OptimizerManager.updateStatus(optimizerID, (((double)j/guesses)*100));
			}
		}

		if(OptimizerManager.checkTerminated(optimizerID)) SolverManager.terminate(solverID);
		SolverManager.delete(solverID);

		if(printfile)
			file.close();
		String s = "";
		for(int i = 0; i < parametersName.size(); i++) {
			if(!s.equals("")) s+= ", ";
			s += parametersName.get(i)+"="+bestSolution[i+1];
		}
		System.gc();
		return new OptimizerResult(s, bestSolution[0]);
	}
	
}
