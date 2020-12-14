package swe_mo.fitnessfunction;

import java.util.ArrayList;

public class PreparsedFunction {
	ArrayList<PreparsedFunction> children = new ArrayList<PreparsedFunction>();
	PreparsedFunction parent;
	String value = "";
	String brackettype = "";
	int position = 0;

	public PreparsedFunction() {}
	public PreparsedFunction(PreparsedFunction parent) {
		this.parent = parent;
	}
	
	
	public void add(PreparsedFunction c) throws Exception {
		c.parent = this;
		c.position = children.size();
		children.add(c);
	}
	
	public PreparsedFunction next() {
		int index = parent.children.indexOf(this)+1;
		if(index < parent.children.size())
			return parent.children.get(index);
		else
			return null;
	}
	
	
	@Override 
	public String toString() {
		String s = "";
		for(PreparsedFunction c : children) {
			s+=c+" | ";
		}
		String t = brackettype+" "+value;
		if(s!="") t += " <"+s+">";
		return t;
	}
}
