package swe_mo.fitnessfunction;

public class STC { //syntax tree content
	private String type;
	public String variable;
	public String function; 
	public Double number;
	public String constant; //pi, e
	public String operation; // +, -, *, /, ^, %, rand
	public String specialop; // sqrt, sin, cos, abs, floor, ceil, ln
	public String paramop; // logn, nth root
	public String loopop; // sum, prod
	public MathfunctionTree tree; 
	//for paramop
	public MathfunctionTree parameter;
	//for sum and prod
	public String counterName;
	public MathfunctionTree lowerBound;
	public MathfunctionTree upperBound;


	public STC() {
		
	}
	public STC(STC s) {
		this.type = s.getType();
		this.variable = s.variable;
		this.function = s.function;
		this.number = s.number;
		this.constant = s.constant;
		this.operation = s.operation;
		this.specialop = s.specialop;
		this.paramop = s.paramop;
		this.loopop = s.loopop;
		this.tree = s.tree;
		this.counterName = s.counterName;
		this.lowerBound = s.lowerBound;
		this.upperBound = s.upperBound;
	}	
	public STC(String type, String s) throws Exception {
		if(type.equals("V")) {
			this.type = "V";
			this.variable = s;	
		} else if(type.equals("C")) {
			this.type = "C";
			constant = s;			
		} else if(type.equals("O")) {
			this.type = "O";
			operation = s;			
		} else if(type.equals("F")) {
			this.type = "F";
			function = s;		
		} else {
			throw new Exception("Wrong constructor used. ("+type+")");
		}
	}
	public STC(double value) {
		type = "N";
		number = value;
	}
	public STC(String type, String s, MathfunctionTree tree) throws Exception {
		if(type.equals("V")) {
			this.type = "V";
			this.variable = s;	
			this.tree = tree;	
		} else if(type.equals("S")) {
			this.type = "S";
			this.specialop = s;	
			this.tree = tree;	
		} else {
			throw new Exception("Wrong constructor used. ("+type+")");
		}
	}
	public STC(String type, String s, MathfunctionTree parameter, MathfunctionTree tree) throws Exception {
		if(type.equals("P")) {
			this.type = "P";
			this.paramop = s;
			this.tree = tree;		
			this.parameter = parameter;
		} else {
			throw new Exception("Wrong constructor used. ("+type+")");
		}
	}
	public STC(String type, String s, String counterName, MathfunctionTree lowerBound, MathfunctionTree upperBound, MathfunctionTree tree) throws Exception {
		if(type.equals("L")) {
			this.type = "L";
			this.loopop = s;
			this.tree = tree;		
			this.counterName = counterName;
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;	
		} else {
			throw new Exception("Wrong constructor used. ("+type+")");
		}
	}

	
	
	public String getType() {
		return type;
	}
		
	@Override
	public String toString() {
		if(type.equals("V")) {
			if(tree != null)
				return "[V] "+variable+" { "+tree+"} ";
			else 
				return "[V] "+variable+" ";
		} else if(type.equals("C")) {
			return "[C] "+constant+" ";
		} else if(type.equals("N")) {
			return "[N] "+number+" ";
		} else if(type.equals("O")) {
			return "[O] "+operation+" ";
		} else if(type.equals("L")) {
			return "[L] "+loopop+" {cN:"+counterName+"}{lB:"+lowerBound+"}{uB:"+upperBound+"}{ "+tree+"} ";
		} else if(type.equals("P")) {
			return "[P] "+paramop+" {p:"+parameter+"}{ "+tree+"} ";
		} else if(type.equals("S")) {
			return "[S] "+specialop+" { "+tree+"} ";
		} else if(type.equals("F")) {
			return "[F] "+function+" ";
		} else {
			return "";
		}
	}
	


	
	//resolve constants
	public double resolveC() throws Exception {
		if(type.equals("C")) {
			if(constant.equals("pi"))
				return Math.PI;
			if(constant.equals("e"))
				return Math.E;
			throw new Exception("Cannot resolve constant from syntaxtree node. (unknown constant)");
		} 
		throw new Exception("Cannot resolve constant from syntaxtree node. (not a constant)");
	}
}
