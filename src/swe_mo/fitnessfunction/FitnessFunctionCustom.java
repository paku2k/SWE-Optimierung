package swe_mo.fitnessfunction;

import java.util.ArrayList;
import java.util.Stack;

public class FitnessFunctionCustom {

	private String functionString;
	private String functionStringStyled;
	private MathfunctionTree functionTree;
	
	

	public FitnessFunctionCustom() {}
	public FitnessFunctionCustom(String s) throws Exception {
		parseFunctionString(s);
	}
	

	@Override
	public String toString() {
		return functionTree.toString();
	}

	public String getFunctionString() {
		return functionString;
	}
	public String getFunctionStringStyled() {
		return functionStringStyled;
	}
	public void parseFunctionString(String s) throws Exception {
		s = s.trim();
		String styled = s;
		s = removeLRBrackets(s);
		try {
			
		//find equal sign, string before has to be f... and after the expression
			if(s.indexOf("f")!=0) {
				throw new Exception("no function identifier f at beginning");
			}
			if(s.indexOf("=")<1) {
				if(s.indexOf("=")<0) {
					throw new Exception("no equal sign");
				}
				throw new Exception("no function identifier before equal sign");
			}
			String[] expr = s.split("=", 2);
			
		//parse f_{}(var1,var2,...,varn) and find defined variables
			ArrayList<String> detectedVars = new ArrayList<String>();
			String f = expr[0];
			
			PreparsedFunction ppf_f = traverseBrackets(f).getRoot();
			
			//detect variables
			try {
				String test = ppf_f.children.get(ppf_f.children.size()-1).children.get(0).brackettype;
			} catch(Exception e) {
				throw new Exception("variablenames for function call needed (e.g. f(x,n))");
			}
			if(ppf_f.children.get(ppf_f.children.size()-1).children.get(0).brackettype.equals("(")) {
				String varnames = "";
				for(PreparsedFunction v : ppf_f.children.get(ppf_f.children.size()-1).children) {
					varnames += v.value;
					if(!v.children.isEmpty()) {
							for(PreparsedFunction va : v.children) {
							varnames += va.value;

							if(!va.children.isEmpty())
								throw new Exception("no stacked sub-/superscripts as variablename allowed");
						}
					}
				}
				
				for(String var : varnames.split(",")) {
					var = var.trim();
					if(var.contains("^")) {
						throw new Exception("no superscripts in variablename allowed");									
					} else if(!var.isEmpty()) {
						detectedVars.add(var);
					}
				}
			}

			functionTree = new MathfunctionTree(new STC("F",expr[0]));

				
for(String u : detectedVars) {
	System.out.println(u);
}
System.out.println("-----");
		//parse expression		
			expr[1] = replaceAll(expr[1], "\\cdot ", "*");
			
			//FunctionMap fm = traverseIntoMap(traverseBrackets(expr[1]), detectedVars);
			FunctionMap fm = new FunctionMap();
			fm.rootkey = "["+fm.add(expr[1])+"]";
			fmTraverseBrackets(fm);
			
			
			
			
System.out.println(s);
//throw new Exception(fm.toString());
			//functionTree.getRoot().addR();

			functionTree.getRoot().addR(temp());
			this.functionString = s;	
			this.functionStringStyled = styled;
		} catch(Exception e) {
			throw new Exception("Error while parsing function string. ("+e.getMessage()+")");
		}
	}
	
	
	
	
	

	private void fmTraverseBrackets(FunctionMap fm) throws Exception {
		//translate into function map
		for(int i=0; i<fm.size(); i++) {
			String s = fm.get("["+i+"]");

			while(true) {
				BracketIndex brackets = new BracketIndex();	
				brackets.findNextBrackets(s);				
				if(brackets.roundOpenFirst()) {
					int closingBracketIndex = brackets.roundOpen + findCorrespondingClosingBracket(s.substring(brackets.roundOpen+1), "(");
					String bracketContent = s.substring(brackets.roundOpen+1, closingBracketIndex);
					s = replaceInString(s, "("+bracketContent+")", "["+fm.add(bracketContent)+"]");
				} else if(brackets.curlyOpenFirst()) {
					int closingBracketIndex = brackets.curlyOpen + findCorrespondingClosingBracket(s.substring(brackets.curlyOpen+1), "{");
					String bracketContent = s.substring(brackets.curlyOpen+1, closingBracketIndex);
					s = replaceInString(s, "{"+bracketContent+"}", "["+fm.add(bracketContent)+"]");					
				} else if(brackets.roundCloseFirst()) {
					throw new Exception("Traverse error: Open round bracket missing");				
				} else if(brackets.curlyCloseFirst()) {
					throw new Exception("Traverse error: Open curly bracket missing");
				} else if(brackets.noBrackets()) {
					break;
				}
			}
			
			fm.edit("["+i+"]", s, true);
		}
		
		//clean up fields with a reference to another field only + delete empty fields		
		fm.setAllUnchecked();
		for(int i=0; i<fm.size(); i++) {
			if(!fm.checked("["+i+"]")) {
				String s = fm.get("["+i+"]");

				if(s.indexOf("[") == 0 && s.indexOf("]") == s.length()-1 && fm.findAnchor(s)!="") {
					//if only a reference to another map entry and no additional operation/constant/variable
					fm.edit(fm.findAnchor("["+i+"]"), replaceInString(fm.get(fm.findAnchor("["+i+"]")), "["+i+"]", s), false);
					if(("["+i+"]").equals(fm.rootkey)) fm.rootkey = s;
					s = "";
				}
				
				if(s.isEmpty()) {
					fm.remove("["+i+"]");
					fm.edit(fm.findAnchor("["+i+"]"), deleteFromString(fm.get(fm.findAnchor("["+i+"]")),"["+i+"]"), false);
					
				} else {
					fm.edit("["+i+"]", s, true);						
				}
			}
		}

		//remove spaces before and after [] brackets and operands	
		fm.setAllUnchecked();
		for(int i=0; i<fm.size(); i++) {
			if(!fm.checked("["+i+"]")) {
				String s = fm.get("["+i+"]");
				
				s = s.trim();
				while(s.indexOf("  ")>-1) {
					s = s.replaceAll("  ", " ");
				}
				s = replaceAll(s, " +", "+");
				s = replaceAll(s, "+ ", "+");
				s = replaceAll(s, " -", "-");
				s = replaceAll(s, "- ", "-");
				s = replaceAll(s, " *", "*");
				s = replaceAll(s, "* ", "*");
				s = replaceAll(s, " /", "/");
				s = replaceAll(s, "/ ", "/");
				s = replaceAll(s, " ^", "^");
				s = replaceAll(s, "^ ", "^");
				s = replaceAll(s, " _", "_");
				s = replaceAll(s, "_ ", "_");
				s = replaceAll(s, " [", "[");
				s = replaceAll(s, "] ", "]");
				
				fm.edit("["+i+"]", s, true);
			}
		}

		//put operands for +,-,/,%,* in own entries
		fm.setAllUnchecked();
		for(int i=0; i<fm.size(); i++) {
			if(!fm.checked("["+i+"]")) {
				String s = fm.get("["+i+"]");
				
				s = operandsToOwnEntry(s, "+", fm);
				s = operandsToOwnEntry(s, "-", fm);
				s = operandsToOwnEntry(s, "/", fm);
				s = operandsToOwnEntry(s, "%", fm);

				
				int indexOfBackslash = s.indexOf("\\");
				if(indexOfBackslash > 0) {
					String before = s.substring(indexOfBackslash-1, indexOfBackslash);
					if(!before.equals("+") || !before.equals("-") || !before.equals("/") || !before.equals("*") || !before.equals("^") || !before.equals("%"))
					s = s.substring(0, indexOfBackslash).concat("*").concat(s.substring(indexOfBackslash));
				}
				
				s = operandsToOwnEntry(s, "*", fm);				
				
				fm.edit("["+i+"]", s, true);
			}
		}

		//find products (number*variable) and replace
		fm.setAllUnchecked();
		for(int i=0; i<fm.size(); i++) {
			if(!fm.checked("["+i+"]")) {
				String s = fm.get("["+i+"]");
				
				if(!s.contains("\\") && !s.contains("_") && !s.contains("=") && !s.contains("[")) {
					//check if contains both letters and numbers
				    if(s.matches(".*[0-9].*") && (s.matches(".*[A-Z].*") || s.matches(".*[a-z].*"))) {
				    	s = resolveVariableNumberProd(s, fm);
						s = operandsToOwnEntry(s, "*", fm);		
				    }
				}
				
				fm.edit("["+i+"]", s, true);
			}
		}

System.out.println(fm);
System.out.println("Root: "+fm.rootkey);

	}
	
	private String resolveVariableNumberProd(String s, FunctionMap fm) {    	
    	String charAtStart = s.substring(0, 1);
    	String charAtEnd = s.substring(s.length()-1);
    	
    	if(charAtEnd.equals(".")) {
    		s = s.substring(0, s.length()-1);
        	charAtEnd = s.substring(s.length()-1);
    	}

		if((charAtStart.matches(".*[0-9].*") && (charAtEnd.matches(".*[A-Z].*") || charAtEnd.matches(".*[a-z].*")))
				|| (charAtStart.matches(".*[0-9].*") && charAtEnd.matches(".*[0-9].*"))) {
			//0..a
			String n = s;
			//find the number at start
			while(n.matches(".*[A-Z].*") || n.matches(".*[a-z].*")) {
				n = n.substring(0, n.length()-1);
			}	
			String c = s.replace(n,"");
			
			//if . at the end of the number -> remove
	    	if(n.substring(n.length()-1).equals(".")) {
	    		n = n.substring(0, n.length()-1);
	    	}
			
			s = "[" + fm.add(n) + "]*[" + fm.add(c) + "]";
			
		} else if((charAtEnd.matches(".*[0-9].*") && (charAtStart.matches(".*[A-Z].*") || charAtStart.matches(".*[a-z].*")))
				|| ((charAtStart.matches(".*[A-Z].*") || charAtStart.matches(".*[a-z].*")) && (charAtEnd.matches(".*[A-Z].*") || charAtEnd.matches(".*[a-z].*")))) {
			//a..0

			String c = s;
			//find the string at start
			while(c.matches(".*[0-9].*")) {
				c = c.substring(0, c.length()-1);
			}
			String n = s.replace(c,"");
			
			s = "[" + fm.add(c) + "]*[" + fm.add(n) + "]";
		}
    	
    	return s;		
	}
	
	private int findCorrespondingClosingBracket(String s, String brackettype) throws Exception {
		Stack<String> b = new Stack<String>();
		String old = s;
		
		b.add(brackettype);
		
		while(!b.isEmpty()) {
			BracketIndex brackets = new BracketIndex();	
			brackets.findNextBrackets(s);				
			if(brackets.roundOpenFirst()) {
				b.add("(");
				s = s.substring(brackets.roundOpen+1);
				
			} else if(brackets.curlyOpenFirst()) {
				b.add("{");
				s = s.substring(brackets.curlyOpen+1);
				
			} else if(brackets.roundCloseFirst()) {
				if(b.isEmpty() || !b.peek().equals("(")) {
					throw new Exception("Traverse error: Open round bracket missing");
				}
				b.pop();
				s= s.substring(brackets.roundClose+1);
		
			} else if(brackets.curlyCloseFirst()) {
				if(b.isEmpty() || !b.peek().equals("{")) {
					throw new Exception("Traverse error: Open curly bracket missing");
				}
				b.pop();
				s= s.substring(brackets.curlyClose+1);

			} else if(brackets.noBrackets()) {
				if(b.peek().equals("("))
					throw new Exception("Traverse error: Closing round bracket missing");						
				else if(b.peek().equals("{"))
					throw new Exception("Traverse error: Closing curly bracket missing");
			}
		}		
		return old.length() - s.length();
	}
	
	
	private String operandsToOwnEntry(String s, String op, FunctionMap fm) throws Exception {
		if(s.indexOf(op) > -1) {
			String l = s.substring(0, s.indexOf(op));
			String r = s.substring(s.indexOf(op)+op.length());
			
			if(l.isEmpty()) {
				switch(op) {
					case "/":
						throw new Exception("empty numerator in fraction");
					case "*":
						throw new Exception("missing multiplier in product");
					case "+":
					case "-":
						l = "0";
				}
			}
			if(r.isEmpty()) {
				switch(op) {
					case "/":
						throw new Exception("empty denominator in fraction");
					case "*":
						throw new Exception("missing multiplicand in product");
					case "+":
						throw new Exception("missing addend in sum");
					case "-":
						throw new Exception("missing subtrahend in difference");
			}
				
			}

			if(! (l.indexOf("[") == 0 && l.indexOf("]") == l.length()-1)) {
				l = "[" + fm.add(l) + "]";
			}
			if(! (r.indexOf("[") == 0 && r.indexOf("]") == r.length()-1)) {
				r = "[" + fm.add(r) + "]";
			}
			
			s = l+op+r;
		}
		
		return s;
	}
	
	
	
	
	
	
	
	

	
	
	private PpfHandler traverseBrackets(String s) throws Exception {
		return traverseBrackets(s, new Stack<String>(), new PpfHandler());
	}	
	private PpfHandler traverseBrackets(String s, Stack<String> b, PpfHandler ph) throws Exception {
		BracketIndex brackets = new BracketIndex();		
		brackets.findNextBrackets(s);	
	
		if(brackets.roundOpenFirst()) {
			ph.add(s.substring(0, brackets.roundOpen));
			
			b.push("(");

			ph.levelDown((!b.isEmpty())?"(":"");
			traverseBrackets(s.substring(brackets.roundOpen+1), b, ph);
			
			
		} else if(brackets.roundCloseFirst()) {
			if(b.isEmpty() || !b.peek().equals("(")) {
				throw new Exception("Traverse error: Open round bracket missing");
			}
			
			ph.add(s.substring(0, brackets.roundClose));
			
			b.pop();

			ph.levelUp();
			traverseBrackets(s.substring(brackets.roundClose+1), b, ph);
			
		} else if(brackets.curlyOpenFirst()) {
			ph.add(s.substring(0, brackets.curlyOpen));

			b.push("{");

			ph.levelDown((!b.isEmpty())?"{":"");
			traverseBrackets(s.substring(brackets.curlyOpen+1), b, ph);
			
		} else if(brackets.curlyCloseFirst()) {
			if(b.isEmpty() || !b.peek().equals("{")) {
				throw new Exception("Traverse error: Open curly bracket missing");
			}
			
			ph.add(s.substring(0, brackets.curlyClose));
			
			b.pop();

			ph.levelUp();
			traverseBrackets(s.substring(brackets.curlyClose+1), b, ph);
			
		} else if(brackets.noBrackets()) {
			ph.add(s);
		}
		
		if(s.isEmpty() && !b.isEmpty()) {
			if(b.peek().equals("("))
				throw new Exception("Traverse error: Close round bracket missing");		
			if(b.peek().equals("{"))
				throw new Exception("Traverse error: Close curly bracket missing");			
		}
		
		return ph;
	}

	
	
	private static String removeLRBrackets(String s) throws Exception {
		s = replaceAll(s, "\\left", "");
		s = replaceAll(s, "\\right", "");

		s = replaceAll(s, "\\{", "(");
		s = replaceAll(s, "\\}", ")");
		s = replaceAll(s, "\\langle", "(");
		s = replaceAll(s, "\\rangle", ")");		
		s = replaceAll(s, "\\lfloor", "\\floor(");
		s = replaceAll(s, "\\rfloor", ")");		
		s = replaceAll(s, "\\lceil", "\\ceil(");
		s = replaceAll(s, "\\rceil", ")");	

		//find abs  (||) and replace by \abs()
		while(s.indexOf("|")>-1) {			
			int indexFirstPipe = s.indexOf("|");
			s = s.substring(0, indexFirstPipe).concat("\\abs(").concat(s.substring(indexFirstPipe+"|".length()));
			int closeBracketIndex = indexFirstPipe+"|".length() +s.substring(indexFirstPipe+"|".length()+1).indexOf("|");
			s = s.substring(0, closeBracketIndex+1).concat(")").concat(s.substring(closeBracketIndex + 2));
		}
		
		//change [] brackets to () except for where they belong to a \sqrt
		while(s.indexOf("\\sqrt[")>-1) {
			int closeBracketIndex = s.indexOf("\\sqrt[")+"\\sqrt[".length() +findCorrespondingBracket(s.substring(s.indexOf("\\sqrt[")+"\\sqrt[".length()+1),"[");
			s = s.substring(0, s.indexOf("\\sqrt[")).concat("\\sqrt<").concat(s.substring(s.indexOf("\\sqrt[")+"\\sqrt[".length()));
			s = s.substring(0, closeBracketIndex).concat(">").concat(s.substring(closeBracketIndex + 1));
		}
		s = replaceAll(s, "[", "(");
		s = replaceAll(s, "]", ")");	
		
		return s;
	}	
	
	private static int findCorrespondingBracket(String s, String brackettype) throws Exception {
		Stack<String> b = new Stack<String>();
		String old = s;
		
		b.add(brackettype);
		
		while(!b.isEmpty()) {
			int openBracketIndex = s.indexOf(brackettype);
			int closeBracketIndex = -1;
			switch(brackettype) {
				case "[":
					closeBracketIndex = s.indexOf("]");
					break;
				case "(":
					closeBracketIndex = s.indexOf(")");
					break;
				case "{":
					closeBracketIndex = s.indexOf("}");
					break;
				case "<":
					closeBracketIndex = s.indexOf(">");
					break;
				default:
					throw new Exception("unknown brackettype");
			}
			
			
			//find first
			boolean openisfirst = openBracketIndex > -1;
			if(closeBracketIndex > -1)
				openisfirst = openisfirst && (openBracketIndex < closeBracketIndex);
			
			boolean closeisfirst = closeBracketIndex > -1;
			if(openBracketIndex > -1)
				closeisfirst = closeisfirst && (closeBracketIndex < openBracketIndex);
			
			
			if(openisfirst) {
				b.add(brackettype);
				s = s.substring(openBracketIndex+1);
				
			} else if(closeisfirst) {
				if(b.isEmpty() || !b.peek().equals(brackettype)) {
					throw new Exception("Traverse error: Open "+brackettype+" bracket missing");
				}
				b.pop();
				s= s.substring(closeBracketIndex+1);
			}
			
			if(s.isEmpty()) {
				throw new Exception("Traverse error: Closing "+brackettype+" bracket missing");
			}
		}		
		return old.length() - s.length();
	}
	
	
	
	
	
	
	
	private static String replaceAll(String s, String sequence, String replacement) {
		while(s.indexOf(sequence) > -1) {
			s = s.substring(0, s.indexOf(sequence)).concat(replacement).concat(s.substring(s.indexOf(sequence)+sequence.length()));
		}		
		return s;
	}
	
	private String insertBefore(String s, String insert, String before) throws Exception {
		if(s.indexOf(before) < 0) {
			throw new Exception("insert before string not found");
		}
		return s.substring(0, s.indexOf(before)).concat(insert).concat(s.substring(s.indexOf(before)));		
	}
	
	private String insertAfter(String s, String insert, String after) throws Exception {
		if(s.indexOf(after) < 0) {
			throw new Exception("insert after string not found");
		}
		return s.substring(0, s.indexOf(after)+after.length()).concat(insert).concat(s.substring(s.indexOf(after)+after.length()));		
	}
	
	private String deleteFromString(String s, String delete) throws Exception {
		if(s.indexOf(delete) > -1) {
			return s.substring(0, s.indexOf(delete)).concat(s.substring(s.indexOf(delete)+delete.length()));					
		} else {
			return s;
		}
	}
	
	private String replaceInString(String s, String old, String replacement) throws Exception {
		if(s.indexOf(old) > -1) {
			return s.substring(0, s.indexOf(old)).concat(replacement).concat(s.substring(s.indexOf(old)+old.length()));					
		} else {
			return s;
		}
	}
	
	
	
	
	private BinaryTreeNode temp() throws Exception {
		// temp	
		// "f_{test}(x,n)=\sum_{a=0}^{n-1}((5 x_a-3)+\sqrt{2n})+x_3"
		
			//5x_1-3
	 		BinaryTreeNode<STC> n1 = new BinaryTreeNode<STC>(new STC("O","-"));
			BinaryTreeNode<STC> n2 = new BinaryTreeNode<STC>(new STC("O","*"));
			n2.addL(new BinaryTreeNode<STC>(new STC(5)));
			MathfunctionTree mathfunctionTree2 = new MathfunctionTree(new STC("V","a"));
			n2.addR(new BinaryTreeNode<STC>(new STC("V", "x", mathfunctionTree2)));
			BinaryTreeNode<STC> n3 = new BinaryTreeNode<STC>(new STC(3));
			n1.addL(n2);
			n1.addR(n3);
			
			//sqrt(2n)
			MathfunctionTree mathfunctionTree3 = new MathfunctionTree(new STC("F","f(n)"));
			BinaryTreeNode<STC> n6 = new BinaryTreeNode<STC>(new STC("O","*"));
			n6.addL(new BinaryTreeNode<STC>(new STC(2)));
			n6.addR(new BinaryTreeNode<STC>(new STC("V","n")));
			mathfunctionTree3.getRoot().addR(n6);
			BinaryTreeNode<STC> n5 = new BinaryTreeNode<STC>(new STC("S","sqrt",mathfunctionTree3));

			BinaryTreeNode<STC> n4 = new BinaryTreeNode<STC>(new STC("O","+"));
			n4.addL(n1);
			n4.addR(n5);	

			//sum
			MathfunctionTree mathfunctionTree4 = new MathfunctionTree(new STC("F", "f(x,n,a)"));
			mathfunctionTree4.getRoot().addR(n4);
			MathfunctionTree mathfunctionTree4a = new MathfunctionTree(new STC("F", "f(n)"));
			BinaryTreeNode<STC> a = new BinaryTreeNode<STC>(new STC("O", "-"));
			a.addL(new BinaryTreeNode<STC>(new STC("V","n")));
			a.addR(new BinaryTreeNode<STC>(new STC(1)));
			BinaryTreeNode<STC> b = new BinaryTreeNode<STC>(new STC("F","f(n)"));
			b.addR(a);
			mathfunctionTree4a.getRoot().addR(b);
			BinaryTreeNode<STC> nA = new BinaryTreeNode<STC>(new STC("L", "sum", "a", new MathfunctionTree(new STC(0)), mathfunctionTree4a, mathfunctionTree4));
			
			//+x_3
			MathfunctionTree mathfunctionTree5 = new MathfunctionTree(new STC(3));
			BinaryTreeNode<STC> nB = new BinaryTreeNode<STC>(new STC("V", "x", mathfunctionTree5));
			
			BinaryTreeNode<STC> n = new BinaryTreeNode<STC>(new STC("O","+"));
			n.addL(nA);
			n.addR(nB);
			
			return n;
		
	}
	
	
	public Double calculate(VarSpace v) throws Exception {
		functionTree.setVarSpace(v);	
		return functionTree.calculate();
	}
	
	
}