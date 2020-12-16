package swe_mo.fitnessfunction;

public class PpfHandler {
	private PreparsedFunction root_ppf = new PreparsedFunction();
	private PreparsedFunction cur_ppf = root_ppf;
	private String brackettype = "";

	public void levelUp() {
		cur_ppf = cur_ppf.parent;
		brackettype = cur_ppf.brackettype;
	}
	public void levelDown(String brackettype) throws Exception {
		this.brackettype = brackettype;
		cur_ppf.add(new PreparsedFunction());
		cur_ppf = cur_ppf.children.get(cur_ppf.children.size()-1);
	}
	
	public void add(String s) throws Exception {
		if(s.trim().isEmpty()) return;
		cur_ppf.add(new PreparsedFunction());
		cur_ppf.children.get(cur_ppf.children.size()-1).value = s.trim();
		cur_ppf.children.get(cur_ppf.children.size()-1).brackettype = brackettype;
	}
	
	
	public PreparsedFunction getRoot() {
		return root_ppf;
	}
	
	

	@Override 
	public String toString() {
		return root_ppf.toString();
	}
}
