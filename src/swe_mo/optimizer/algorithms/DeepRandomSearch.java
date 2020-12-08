package swe_mo.optimizer.algorithms;

import swe_mo.solver.SolverResult;
import swe_mo.solver.de.CRN;
import swe_mo.solver.pso.PSOgsc;
import java.util.ArrayList;
import swe_mo.solver.*;

public class DeepRandomSearch extends BaseOptimizer{
	
	ArrayList<Double> parametersMin = new ArrayList<Double>();
	ArrayList<Double> parametersMax = new ArrayList<Double>();
	ArrayList<String> parametersName = new ArrayList<String>();
	int levels;
	int levelGuesses;

	public DeepRandomSearch(int ffID, int numberIterations, ArrayList<Double> parametersMin, ArrayList<Double> parametersMax, ArrayList<String> parametersName, int levels, int levelGuesses) {
		
		super(ffID, numberIterations);
		this.parametersMin = parametersMin;
		this.parametersMax = parametersMax;
		this.parametersName = parametersName;
		this.levels = levels;
		this.levelGuesses = levelGuesses;
		
	}
	
		public void optimize() throws Exception {
			
			FileGenerator file = new FileGenerator("Test_PSOgsc_DeepRandomSearch", "iteration; ffCalls; minimum; p1; p2; p3");
			ArrayList<Double> tempParametersMin = new ArrayList<Double>(parametersMin);
			ArrayList<Double> tempParametersMax = new ArrayList<Double>(parametersMax);
			ArrayList<String> tempParametersName = new ArrayList<String>(parametersName);
			double [] p = new double[tempParametersName.size()];
			double [] bestSolution = new double[tempParametersName.size()+1];
			
			for(int i = 0; i < levels; i++) {
				for(int j = 0; j < levelGuesses; j++) {
					
					// define parameters for search
					for(int k = 0; k < tempParametersName.size(); k++) {
						p[k] = CRN.rn(tempParametersMax.get(k), tempParametersMin.get(k));
					}
					
					// calculate solution for given parameters and write to file (in this hardcoded example the standard constructor of PSOgsc is used)
					SolverResult sr = new PSOgsc(30, -5.12, 5.12, 20, p[0], p[1], p[2], 1, numberIterations, ffID, 0, 1).solve();
					file.write(sr.iterations+";"+sr.ffCounter+";"+sr.value+";"+p[0]+";"+p[1]+";"+p[2]);
					
					// track best solution
					if(j==0 && i==0) {
						bestSolution[0] = sr.value; 
						for(int l = 0; l < tempParametersName.size(); l++) {
							bestSolution[l+1] = p[l];
						}
					}
					else {
						if(sr.value<bestSolution[0]) {
							bestSolution[0] = sr.value; 
							for(int m = 0; m < tempParametersName.size(); m++) {
								bestSolution[m+1] = p[m];
							}
						}
					}
					
				}
				
				// update boundaries of search space
				for(int n = 0; n < tempParametersName.size(); n++) {
					if(bestSolution[n+1]<((tempParametersMax.get(n)-tempParametersMin.get(n))/2)) {
						tempParametersMax.set(n, ((tempParametersMax.get(n)-tempParametersMin.get(n))/2));
					}
					else {
						tempParametersMin.set(n, ((tempParametersMax.get(n)-tempParametersMin.get(n))/2));
					}
				}
				
			}
			
			file.close();
			String s = "";
			for(int o = 0; o < bestSolution.length; o++) {
				s += bestSolution[o]+" ";
			}
			System.out.println(s);
			
		}
	
	
}
