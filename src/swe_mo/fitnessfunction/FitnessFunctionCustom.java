package swe_mo.fitnessfunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class FitnessFunctionCustom {

	private String functionString;
	private String functionStringStyled;
	private FunctionMap functionMap;
	private MathfunctionTree functionTree;
	private Double lowerBound;
	private Double upperBound;
	
	

	public FitnessFunctionCustom() {}
	public FitnessFunctionCustom(String s) throws Exception {
		parseFunctionString(s);
	}
	

	public void setLowerBound(Double b) {
		lowerBound = b;
	}
	public void setUpperBound(Double b) {
		upperBound = b;
	}
	public Double getLowerBound() {
		return lowerBound;
	}
	public Double getUpperBound() {
		return upperBound;
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
	public FunctionMap getFunctionMap() {
		return functionMap;
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
				@SuppressWarnings("unused")
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
				
		//parse expression		
			expr[1] = replaceAll(expr[1], "\\cdot ", "*");
			
			//FunctionMap fm = traverseIntoMap(traverseBrackets(expr[1]), detectedVars);
			FunctionMap fm = new FunctionMap();
			fm.rootkey = "["+fm.add(expr[1])+"]";
			fmTraverseBrackets(fm);
			
			this.functionMap = fm;

			BinaryTreeNode<STC> btn = new BinaryTreeNode<STC>();
			convertFmToBtn(fm, detectedVars, fm.get(fm.rootkey), btn);			
			
			functionTree.getRoot().addR(btn);
			
			this.functionString = s;	
			this.functionStringStyled = styled;
		} catch(Exception e) {
			throw new Exception("Error while parsing function string. ("+e.getMessage()+")");
		}
	}
	
	
	

	private void convertFmToBtn(FunctionMap fm, ArrayList<String> detectedVars, String s, BinaryTreeNode<STC> btn) throws Exception {
		//L	
			if(checkForPattern(s, "\\sum_[~]^[~][~][~]")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=1)");
				
			} else if(checkForPattern(s, "\\sum_[~]^[~][~]~")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=2)");
				
			} else if(checkForPattern(s, "\\sum_[~]^[~][~]")) {
				//L
				ArrayList<String> contents = getPatternContents(s, "\\sum_[~]^[~][~]");					
				String countername = "";
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();

				MathfunctionTree mft_lb = new MathfunctionTree(new STC("F",""));
				String[] lb = fm.get("["+contents.get(0)+"]").split("=");
				if(lb.length != 2) throw new Exception("error lower boundary in product");
				countername = lb[0];
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,lb[1],b);
				mft_lb.getRoot().addR(b);
				
				MathfunctionTree mft_ub = new MathfunctionTree(new STC("F",""));
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				mft_ub.getRoot().addR(b);
				
				detectedVars.add(countername);			
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));	
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(2)+"]"),b);
				mft.getRoot().addR(b);
				
				btn.setData(new STC("L","sum",countername,mft_lb,mft_ub,mft));
				
				
			} else if(checkForPattern(s, "\\sum_[~]^~[~][~]")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=3)");
				
			} else if(checkForPattern(s, "\\sum_[~]^~[~]~")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=4)");
				
			} else if(checkForPattern(s, "\\sum_[~]^~[~]")) {
				//L
				ArrayList<String> contents = getPatternContents(s, "\\sum_[~]^~[~]");					
				String countername = "";
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();

				MathfunctionTree mft_lb = new MathfunctionTree(new STC("F",""));
				String[] lb = fm.get("["+contents.get(0)+"]").split("=");
				if(lb.length != 2) throw new Exception("error lower boundary in product");
				countername = lb[0];
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,lb[1],b);
				mft_lb.getRoot().addR(b);
				
				MathfunctionTree mft_ub = new MathfunctionTree(new STC("F",""));
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);
				mft_ub.getRoot().addR(b);
				
				detectedVars.add(countername);			
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));	
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(2)+"]"),b);
				mft.getRoot().addR(b);
				
				btn.setData(new STC("L","sum",countername,mft_lb,mft_ub,mft));
		
			} else if(checkForPattern(s, "\\sum_[~]^[~]~[~]")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=5)");
				
			} else if(checkForPattern(s, "\\sum_[~]^[~]~ ~")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=6)");
				
			} else if(checkForPattern(s, "\\sum_[~]^[~]~")) {
				//L
				ArrayList<String> contents = getPatternContents(s, "\\sum_[~]^[~]~");					
				String countername = "";
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();

				MathfunctionTree mft_lb = new MathfunctionTree(new STC("F",""));
				String[] lb = fm.get("["+contents.get(0)+"]").split("=");
				if(lb.length != 2) throw new Exception("error lower boundary in product");
				countername = lb[0];
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,lb[1],b);
				mft_lb.getRoot().addR(b);
				
				MathfunctionTree mft_ub = new MathfunctionTree(new STC("F",""));
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				mft_ub.getRoot().addR(b);
				
				detectedVars.add(countername);			
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));	
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(2),b);
				mft.getRoot().addR(b);
				
				btn.setData(new STC("L","sum",countername,mft_lb,mft_ub,mft));
		
			} else if(checkForPattern(s, "\\sum_[~]^~ ~[~]")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=7)");
				
			} else if(checkForPattern(s, "\\sum_[~]^~ ~ ~")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=8)");
				
			} else if(checkForPattern(s, "\\sum_[~]^~ ~")) {
				//L
				ArrayList<String> contents = getPatternContents(s, "\\sum_[~]^~ ~");					
				String countername = "";
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();

				MathfunctionTree mft_lb = new MathfunctionTree(new STC("F",""));
				String[] lb = fm.get("["+contents.get(0)+"]").split("=");
				if(lb.length != 2) throw new Exception("error lower boundary in product");
				countername = lb[0];
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,lb[1],b);
				mft_lb.getRoot().addR(b);
				
				MathfunctionTree mft_ub = new MathfunctionTree(new STC("F",""));
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);
				mft_ub.getRoot().addR(b);
				
				detectedVars.add(countername);			
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));	
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(2),b);
				mft.getRoot().addR(b);
				
				btn.setData(new STC("L","sum",countername,mft_lb,mft_ub,mft));
				
		
			} else if(checkForPattern(s, "\\prod_[~]^[~][~][~]")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=9)");
				
			} else if(checkForPattern(s, "\\prod_[~]^[~][~]~")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=10)");
				
			} else if(checkForPattern(s, "\\prod_[~]^[~][~]")) {
				//L
				ArrayList<String> contents = getPatternContents(s, "\\prod_[~]^[~][~]");					
				String countername = "";
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();

				MathfunctionTree mft_lb = new MathfunctionTree(new STC("F",""));
				String[] lb = fm.get("["+contents.get(0)+"]").split("=");
				if(lb.length != 2) throw new Exception("error lower boundary in product");
				countername = lb[0];
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,lb[1],b);
				mft_lb.getRoot().addR(b);
				
				MathfunctionTree mft_ub = new MathfunctionTree(new STC("F",""));
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				mft_ub.getRoot().addR(b);
				
				detectedVars.add(countername);			
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));	
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(2)+"]"),b);
				mft.getRoot().addR(b);
				
				btn.setData(new STC("L","prod",countername,mft_lb,mft_ub,mft));

				
			} else if(checkForPattern(s, "\\prod_[~]^~[~][~]")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=11)");
				
			} else if(checkForPattern(s, "\\prod_[~]^~[~]~")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=12)");
				
			} else if(checkForPattern(s, "\\prod_[~]^~[~]")) {
				//L
				ArrayList<String> contents = getPatternContents(s, "\\prod_[~]^~[~]");					
				String countername = "";
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();

				MathfunctionTree mft_lb = new MathfunctionTree(new STC("F",""));
				String[] lb = fm.get("["+contents.get(0)+"]").split("=");
				if(lb.length != 2) throw new Exception("error lower boundary in product");
				countername = lb[0];
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,lb[1],b);
				mft_lb.getRoot().addR(b);
				
				MathfunctionTree mft_ub = new MathfunctionTree(new STC("F",""));
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);
				mft_ub.getRoot().addR(b);
				
				detectedVars.add(countername);			
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));	
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(2)+"]"),b);
				mft.getRoot().addR(b);
				
				btn.setData(new STC("L","prod",countername,mft_lb,mft_ub,mft));
		
			} else if(checkForPattern(s, "\\prod_[~]^[~]~[~]")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=13)");
				
			} else if(checkForPattern(s, "\\prod_[~]^[~]~ ~")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=14)");
				
			} else if(checkForPattern(s, "\\prod_[~]^[~]~")) {
				//L
				ArrayList<String> contents = getPatternContents(s, "\\prod_[~]^[~]~");					
				String countername = "";
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();

				MathfunctionTree mft_lb = new MathfunctionTree(new STC("F",""));
				String[] lb = fm.get("["+contents.get(0)+"]").split("=");
				if(lb.length != 2) throw new Exception("error lower boundary in product");
				countername = lb[0];
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,lb[1],b);
				mft_lb.getRoot().addR(b);
				
				MathfunctionTree mft_ub = new MathfunctionTree(new STC("F",""));
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				mft_ub.getRoot().addR(b);
				
				detectedVars.add(countername);			
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));	
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(2),b);
				mft.getRoot().addR(b);
				
				btn.setData(new STC("L","prod",countername,mft_lb,mft_ub,mft));
		
			} else if(checkForPattern(s, "\\prod_[~]^~ ~[~]")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=14)");
				
			} else if(checkForPattern(s, "\\prod_[~]^~ ~ ~")) {
				//L with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=15)");
				
			} else if(checkForPattern(s, "\\prod_[~]^~ ~")) {
				//L
				ArrayList<String> contents = getPatternContents(s, "\\prod_[~]^~ ~");					
				String countername = "";
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();

				MathfunctionTree mft_lb = new MathfunctionTree(new STC("F",""));
				String[] lb = fm.get("["+contents.get(0)+"]").split("=");
				if(lb.length != 2) throw new Exception("error lower boundary in product");
				countername = lb[0];
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,lb[1],b);
				mft_lb.getRoot().addR(b);
				
				MathfunctionTree mft_ub = new MathfunctionTree(new STC("F",""));
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);
				mft_ub.getRoot().addR(b);
				
				detectedVars.add(countername);			
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));	
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(2),b);
				mft.getRoot().addR(b);
				
				btn.setData(new STC("L","prod",countername,mft_lb,mft_ub,mft));
		
		
		//S	
			} else if(checkForPattern(s, "\\sqrt[~][~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=16)");
				
			} else if(checkForPattern(s, "\\sqrt[~]~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=17)");
				
			} else if(checkForPattern(s, "\\sqrt[~]")) {
				//S			
				ArrayList<String> contents = getPatternContents(s, "\\sqrt[~]");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("S","sqrt",mft));		
				
			} else if(checkForPattern(s, "\\sqrt ~[~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=18)");
				
			} else if(checkForPattern(s, "\\sqrt ~ ~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=19)");
				
			} else if(checkForPattern(s, "\\sqrt ~")) {
				//S
				ArrayList<String> contents = getPatternContents(s, "\\sqrt ~");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("S","sqrt",mft));	
				
		
			} else if(checkForPattern(s, "\\ln[~][~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=20)");
				
			} else if(checkForPattern(s, "\\ln[~]~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=21)");
				
			} else if(checkForPattern(s, "\\ln[~]")) {
				//S			
				ArrayList<String> contents = getPatternContents(s, "\\ln[~]");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("S","ln",mft));	
				
			} else if(checkForPattern(s, "\\ln ~[~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=22)");
				
			} else if(checkForPattern(s, "\\ln ~ ~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=23)");
				
			} else if(checkForPattern(s, "\\ln ~")) {
				//S
				ArrayList<String> contents = getPatternContents(s, "\\ln ~");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("S","ln",mft));	
				

			} else if(checkForPattern(s, "\\sin^[~][~][~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=24)");
	
			} else if(checkForPattern(s, "\\sin^[~][~]~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=25)");
	
			} else if(checkForPattern(s, "\\sin^[~][~]")) {
				//S 
				ArrayList<String> contents = getPatternContents(s, "\\sin^[~][~]");					

				btn.setData(new STC("O","^"));	
				
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);

				b = new BinaryTreeNode<STC>(new STC("S","sin",mft));
								
				btn.addL(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addR(b);			
				
	
			} else if(checkForPattern(s, "\\sin^~[~][~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=26)");
	
			} else if(checkForPattern(s, "\\sin^~[~]~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=27)");
	
			} else if(checkForPattern(s, "\\sin^~[~]")) {
				//S 
				ArrayList<String> contents = getPatternContents(s, "\\sin^~[~]");					

				btn.setData(new STC("O","^"));	
				
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);

				b = new BinaryTreeNode<STC>(new STC("S","sin",mft));
								
				btn.addL(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),b);
				btn.addR(b);			
				
	
			} else if(checkForPattern(s, "\\sin^[~]~[~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=28)");
	
			} else if(checkForPattern(s, "\\sin^[~]~ ~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=29)");
	
			} else if(checkForPattern(s, "\\sin^[~]~")) {
				//S 
				ArrayList<String> contents = getPatternContents(s, "\\sin^[~]~");					

				btn.setData(new STC("O","^"));	
				
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);

				b = new BinaryTreeNode<STC>(new STC("S","sin",mft));
								
				btn.addL(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addR(b);			
				
	
			} else if(checkForPattern(s, "\\sin^~ ~[~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=30)");
	
			} else if(checkForPattern(s, "\\sin^~ ~ ~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=31)");
	
			} else if(checkForPattern(s, "\\sin^~ ~")) {
				//S 
				ArrayList<String> contents = getPatternContents(s, "\\sin^~ ~");					

				btn.setData(new STC("O","^"));	
				
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);

				b = new BinaryTreeNode<STC>(new STC("S","sin",mft));
								
				btn.addL(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),b);
				btn.addR(b);			
				
		
			} else if(checkForPattern(s, "\\sin[~][~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=32)");
				
			} else if(checkForPattern(s, "\\sin[~]~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=33)");
				
			} else if(checkForPattern(s, "\\sin[~]")) {
				//S			
				ArrayList<String> contents = getPatternContents(s, "\\sin[~]");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("S","sin",mft));			
				
			} else if(checkForPattern(s, "\\sin ~[~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=34)");
				
			} else if(checkForPattern(s, "\\sin ~ ~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=35)");
				
			} else if(checkForPattern(s, "\\sin ~")) {
				//S
				ArrayList<String> contents = getPatternContents(s, "\\sin ~");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("S","sin",mft));
				

			} else if(checkForPattern(s, "\\cos^[~][~][~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=36)");
		
			} else if(checkForPattern(s, "\\cos^[~][~]~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=37)");
		
			} else if(checkForPattern(s, "\\cos^[~][~]")) {
				//S 
				ArrayList<String> contents = getPatternContents(s, "\\cos^[~][~]");					

				btn.setData(new STC("O","^"));	
				
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);

				b = new BinaryTreeNode<STC>(new STC("S","cos",mft));
								
				btn.addL(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addR(b);			
				
		
			} else if(checkForPattern(s, "\\cos^~[~][~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=38)");
		
			} else if(checkForPattern(s, "\\cos^~[~]~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=39)");
		
			} else if(checkForPattern(s, "\\cos^~[~]")) {
				//S 
				ArrayList<String> contents = getPatternContents(s, "\\cos^~[~]");					

				btn.setData(new STC("O","^"));	
				
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);

				b = new BinaryTreeNode<STC>(new STC("S","cos",mft));
								
				btn.addL(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),b);
				btn.addR(b);			
				
		
			} else if(checkForPattern(s, "\\cos^[~]~[~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=40)");
		
			} else if(checkForPattern(s, "\\cos^[~]~ ~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=41)");
		
			} else if(checkForPattern(s, "\\cos^[~]~")) {
				//S 
				ArrayList<String> contents = getPatternContents(s, "\\cos^[~]~");					

				btn.setData(new STC("O","^"));	
				
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);

				b = new BinaryTreeNode<STC>(new STC("S","cos",mft));
								
				btn.addL(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addR(b);			
				
		
			} else if(checkForPattern(s, "\\cos^~ ~[~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=42)");
		
			} else if(checkForPattern(s, "\\cos^~ ~ ~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=43)");
		
			} else if(checkForPattern(s, "\\cos^~ ~")) {
				//S 
				ArrayList<String> contents = getPatternContents(s, "\\cos^~ ~");					

				btn.setData(new STC("O","^"));	
				
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);

				b = new BinaryTreeNode<STC>(new STC("S","cos",mft));
								
				btn.addL(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),b);
				btn.addR(b);			
					
		
			} else if(checkForPattern(s, "\\cos[~][~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=44)");
				
			} else if(checkForPattern(s, "\\cos[~]~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=45)");
				
			} else if(checkForPattern(s, "\\cos[~]")) {
				//S		
				ArrayList<String> contents = getPatternContents(s, "\\cos[~]");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("S","cos",mft));		
				
			} else if(checkForPattern(s, "\\cos ~[~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=46)");
				
			} else if(checkForPattern(s, "\\cos ~ ~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=47)");
				
			} else if(checkForPattern(s, "\\cos ~")) {
				//S
				ArrayList<String> contents = getPatternContents(s, "\\cos ~");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("S","cos",mft));
				
		
			} else if(checkForPattern(s, "\\abs[~][~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=48)");
				
			} else if(checkForPattern(s, "\\abs[~]~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=49)");
				
			} else if(checkForPattern(s, "\\abs[~]")) {
				//S		
				ArrayList<String> contents = getPatternContents(s, "\\abs[~]");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("S","abs",mft));
				
		
			} else if(checkForPattern(s, "\\floor[~][~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=50)");
				
			} else if(checkForPattern(s, "\\floor[~]~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=51)");
				
			} else if(checkForPattern(s, "\\floor[~]")) {
				//S		
				ArrayList<String> contents = getPatternContents(s, "\\floor[~]");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("S","floor",mft));
				
		
			} else if(checkForPattern(s, "\\ceil[~][~]")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=52)");
				
			} else if(checkForPattern(s, "\\ceil[~]~")) {
				//S with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=53)");
				
			} else if(checkForPattern(s, "\\ceil[~]")) {
				//S	
				ArrayList<String> contents = getPatternContents(s, "\\ceil[~]");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("S","ceil",mft));
		
		
		//P	
			} else if(checkForPattern(s, "\\log_[~][~][~]")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=54)");
				
			} else if(checkForPattern(s, "\\log_[~][~]~")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=55)");
				
			} else if(checkForPattern(s, "\\log_[~][~]")) {
				//P				
				ArrayList<String> contents = getPatternContents(s, "\\log_[~][~]");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				MathfunctionTree mft_p = new MathfunctionTree(new STC("F",""));
				mft_p.getRoot().addR(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("P","log",mft_p,mft));
		
			} else if(checkForPattern(s, "\\log_[~]~[~]")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=56)");
				
			} else if(checkForPattern(s, "\\log_[~]~ ~")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=57)");
				
			} else if(checkForPattern(s, "\\log_[~]~")) {
				//P				
				ArrayList<String> contents = getPatternContents(s, "\\log_[~]~");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				MathfunctionTree mft_p = new MathfunctionTree(new STC("F",""));
				mft_p.getRoot().addR(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("P","log",mft_p,mft));
				
		
			} else if(checkForPattern(s, "\\log_~[~][~]")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=56)");
				
			} else if(checkForPattern(s, "\\log_~[~]~")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=57)");
				
			} else if(checkForPattern(s, "\\log_~[~]")) {
				//P				
				ArrayList<String> contents = getPatternContents(s, "\\log_~[~]");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),b);
				MathfunctionTree mft_p = new MathfunctionTree(new STC("F",""));
				mft_p.getRoot().addR(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("P","log",mft_p,mft));
				
		
			} else if(checkForPattern(s, "\\log_~ ~[~]")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=56)");
				
			} else if(checkForPattern(s, "\\log_~ ~ ~")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=57)");
				
			} else if(checkForPattern(s, "\\log_~ ~")) {
				//P				
				ArrayList<String> contents = getPatternContents(s, "\\log_~ ~");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),b);
				MathfunctionTree mft_p = new MathfunctionTree(new STC("F",""));
				mft_p.getRoot().addR(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("P","log",mft_p,mft));
				
		
			} else if(checkForPattern(s, "\\log[~][~]")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=58)");
				
			} else if(checkForPattern(s, "\\log[~]~")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=59)");
				
			} else if(checkForPattern(s, "\\log[~]")) {
				//P				
				ArrayList<String> contents = getPatternContents(s, "\\log[~]");					

				MathfunctionTree mft_p = new MathfunctionTree(new STC(10));
				
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("P","log",mft_p,mft));
		
			} else if(checkForPattern(s, "\\log ~[~]")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=60)");
				
			} else if(checkForPattern(s, "\\log ~ ~")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=61)");
				
			} else if(checkForPattern(s, "\\log ~")) {
				//P		
				ArrayList<String> contents = getPatternContents(s, "\\log ~");	
				
				MathfunctionTree mft_p = new MathfunctionTree(new STC(10));
				
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("P","log",mft_p,mft));
		
		
			} else if(checkForPattern(s, "\\sqrt_[~][~][~]")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=62)");
				
			} else if(checkForPattern(s, "\\sqrt_[~][~]~")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=63)");
				
			} else if(checkForPattern(s, "\\sqrt_[~][~]")) {
				//P				
				ArrayList<String> contents = getPatternContents(s, "\\sqrt_[~][~]");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				MathfunctionTree mft_p = new MathfunctionTree(new STC("F",""));
				mft_p.getRoot().addR(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("P","nroot",mft_p,mft));
		
			} else if(checkForPattern(s, "\\sqrt_[~]~[~]")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=64)");
				
			} else if(checkForPattern(s, "\\sqrt_[~]~ ~")) {
				//P with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=65)");
				
			} else if(checkForPattern(s, "\\sqrt_[~]~")) {
				//P	
				ArrayList<String> contents = getPatternContents(s, "\\sqrt_[~]~");					

				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				MathfunctionTree mft_p = new MathfunctionTree(new STC("F",""));
				mft_p.getRoot().addR(b);
				
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);
				MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
				mft.getRoot().addR(b);
								
				btn.setData(new STC("P","nroot",mft_p,mft));
				
				
		//C				
			} else if(checkForPattern(s, "\\pi[~]")) {
				//C with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=66)");
				
			} else if(checkForPattern(s, "\\pi ~")) {
				//C with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=67)");
				
			} else if(checkForPattern(s, "\\pi")) {
				//C
				btn.setData(new STC("C","pi"));


			} else if(checkForPattern(s, "e[~]")) {
				//C with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=68)");
				
			} else if(checkForPattern(s, "e ~")) {
				//C with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=69)");
				
			} else if(checkForPattern(s, "e")) {
				//C
				btn.setData(new STC("C","e"));
			
				
		//O
			} else if(checkForPattern(s, "[~]+[~][~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=70)");

			} else if(checkForPattern(s, "[~]+[~]~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=71)");
				
			} else if(checkForPattern(s, "[~]+[~]")) {
				//O
				ArrayList<String> contents = getPatternContents(s, "[~]+[~]");				
				btn.setData(new STC("O","+"));
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addL(b);
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				btn.addR(b);

				
			} else if(checkForPattern(s, "[~]-[~][~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=72)");
				
			} else if(checkForPattern(s, "[~]-[~]~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=73)");
				
			} else if(checkForPattern(s, "[~]-[~]")) {
				//O
				ArrayList<String> contents = getPatternContents(s, "[~]-[~]");				
				btn.setData(new STC("O","-"));
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addL(b);
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				btn.addR(b);

				
			} else if(checkForPattern(s, "[~]*[~][~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=74)");
				
			} else if(checkForPattern(s, "[~]*[~]~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=75)");
				
			} else if(checkForPattern(s, "[~]*[~]")) {
				//O
				ArrayList<String> contents = getPatternContents(s, "[~]*[~]");				
				btn.setData(new STC("O","*"));
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addL(b);
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				btn.addR(b);

				
			} else if(checkForPattern(s, "[~]/[~][~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=81)");
				
			} else if(checkForPattern(s, "[~]/[~]~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=82)");
				
			} else if(checkForPattern(s, "[~]/[~]")) {
				//O
				ArrayList<String> contents = getPatternContents(s, "[~]/[~]");				
				btn.setData(new STC("O","/"));
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addL(b);
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				btn.addR(b);

				
			} else if(checkForPattern(s, "[~]/~[~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=83)");
				
			} else if(checkForPattern(s, "[~]/~ ~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=84)");
				
			} else if(checkForPattern(s, "[~]/~")) {
				//O
				ArrayList<String> contents = getPatternContents(s, "[~]/~");				
				btn.setData(new STC("O","/"));
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addL(b);
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				btn.addR(b);

				
			} else if(checkForPattern(s, "\\frac[~][~][~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=85)");
				
			} else if(checkForPattern(s, "\\frac[~][~]~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=86)");
				
			} else if(checkForPattern(s, "\\frac[~][~]")) {
				//O
				ArrayList<String> contents = getPatternContents(s, "\\frac[~][~]");				
				btn.setData(new STC("O","/"));
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addL(b);
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				btn.addR(b);

				
			} else if(checkForPattern(s, "[~]%[~][~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=87)");
				
			} else if(checkForPattern(s, "[~]%[~]~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=88)");
				
			} else if(checkForPattern(s, "[~]%[~]")) {
				//O
				ArrayList<String> contents = getPatternContents(s, "[~]%[~]");				
				btn.setData(new STC("O","%"));
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addL(b);
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				btn.addR(b);

				
			} else if(checkForPattern(s, "[~]^[~][~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=89)");
				
			} else if(checkForPattern(s, "[~]^[~]~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=90)");
				
			} else if(checkForPattern(s, "[~]^[~]")) {
				//O
				ArrayList<String> contents = getPatternContents(s, "[~]^[~]");				
				btn.setData(new STC("O","^"));
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addL(b);
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				btn.addR(b);

				
			} else if(checkForPattern(s, "[~]^~[~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=112)");
				
			} else if(checkForPattern(s, "[~]^~ ~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=113)");
				
			} else if(checkForPattern(s, "[~]^~")) {
				//O
				ArrayList<String> contents = getPatternContents(s, "[~]^~");				
				btn.setData(new STC("O","^"));
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
				btn.addL(b);
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);
				btn.addR(b);
				
			} else if(checkForPattern(s, "~^[~][~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=114)");
				
			} else if(checkForPattern(s, "~^[~]~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=115)");
				
			} else if(checkForPattern(s, "~^[~]")) {
				//O
				ArrayList<String> contents = getPatternContents(s, "~^[~]");				
				btn.setData(new STC("O","^"));
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),b);
				btn.addL(b);
				b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
				btn.addR(b);

				
			} else if(checkForPattern(s, "rand[~][~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=91)");
				
			} else if(checkForPattern(s, "rand[~]~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=92)");
				
			} else if(checkForPattern(s, "rand[~]")) {
				//O
				ArrayList<String> contents = getPatternContents(s, "rand[~]");		
				String s1 = fm.get("["+contents.get(0)+"]");	
				if(checkForPattern(s1, "[~],[~]")) {				
					btn.setData(new STC("O","rand"));
					
					contents = getPatternContents(s1, "[~],[~]");
					BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
					convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(0)+"]"),b);
					btn.addL(b);
					b = new BinaryTreeNode<STC>();
					convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
					btn.addR(b);	
					
				} else {
					throw new Exception("parameters for random operator not valid");
				}


			} else if(checkForPattern(s, "[~][~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=93)");
				
			} else if(checkForPattern(s, "[~][~]~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=94)");
				
			} else if(checkForPattern(s, "[~]~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=95)");
				
			} else if(checkForPattern(s, "~[~]~")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=96)");
				
			} else if(checkForPattern(s, "~[~]")) {
				//O with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=97)");
				
				
		//V				
			} else if(checkForPattern(s, "~_[~]^[~][~]")) {
				//V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=104)");
				
			} else if(checkForPattern(s, "~_[~]^[~]~")) {
				//V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=105)");				
				
			} else if(checkForPattern(s, "~_[~]^[~]")) {
				//V
				ArrayList<String> contents = getPatternContents(s, "~_[~]^[~]");
				if(detectedVars.contains(contents.get(0))) {
					BinaryTreeNode<STC> a = new BinaryTreeNode<STC>();
					BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
					convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
					MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
					mft.getRoot().addR(b);
					a.setData(new STC("V",contents.get(0),mft));										

					btn.setData(new STC("O","^"));	
					
					b = new BinaryTreeNode<STC>();
					convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(2)+"]"),b);
									
					btn.addL(a);
					btn.addR(b);						
					
				} else {
					throw new Exception("variable "+contents.get(0)+" not found in varspace");
				}		
				
			} else if(checkForPattern(s, "~_[~]^~[~]")) {
				//V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=106)");
				
			} else if(checkForPattern(s, "~_[~]^~ ~")) {
				//V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=107)");				
				
			} else if(checkForPattern(s, "~_[~]^~")) {
				//V
				ArrayList<String> contents = getPatternContents(s, "~_[~]^~");
				if(detectedVars.contains(contents.get(0))) {
					BinaryTreeNode<STC> a = new BinaryTreeNode<STC>();
					BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
					convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
					MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
					mft.getRoot().addR(b);
					a.setData(new STC("V",contents.get(0),mft));										

					btn.setData(new STC("O","^"));	
					
					b = new BinaryTreeNode<STC>();
					convertFmToBtn(fm,detectedVars,contents.get(2),b);
									
					btn.addL(a);
					btn.addR(b);						
					
				} else {
					throw new Exception("variable "+contents.get(0)+" not found in varspace");
				}	
				
			} else if(checkForPattern(s, "~_~^[~][~]")) {
				//V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=108)");
				
			} else if(checkForPattern(s, "~_~^[~] ~")) {
				//V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=109)");				
				
			} else if(checkForPattern(s, "~_~^[~]")) {
				//V
				ArrayList<String> contents = getPatternContents(s, "~_~^[~]");
				if(detectedVars.contains(contents.get(0))) {
					BinaryTreeNode<STC> a = new BinaryTreeNode<STC>();
					BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
					convertFmToBtn(fm,detectedVars,contents.get(1),b);
					MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
					mft.getRoot().addR(b);
					a.setData(new STC("V",contents.get(0),mft));										

					btn.setData(new STC("O","^"));	
					
					b = new BinaryTreeNode<STC>();
					convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(2)+"]"),b);
									
					btn.addL(a);
					btn.addR(b);						
					
				} else {
					throw new Exception("variable "+contents.get(0)+" not found in varspace");
				}		
				
			} else if(checkForPattern(s, "~_~^~[~]")) {
				//V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=110)");
				
			} else if(checkForPattern(s, "~_~^~ ~")) {
				//V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=111)");				
				
			} else if(checkForPattern(s, "~_~^~")) {
				//V
				ArrayList<String> contents = getPatternContents(s, "~_~^~");
				if(detectedVars.contains(contents.get(0))) {
					BinaryTreeNode<STC> a = new BinaryTreeNode<STC>();
					BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
					convertFmToBtn(fm,detectedVars,contents.get(1),b);
					MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
					mft.getRoot().addR(b);
					a.setData(new STC("V",contents.get(0),mft));										

					btn.setData(new STC("O","^"));	
					
					b = new BinaryTreeNode<STC>();
					convertFmToBtn(fm,detectedVars,contents.get(2),b);
									
					btn.addL(a);
					btn.addR(b);						
					
				} else {
					throw new Exception("variable "+contents.get(0)+" not found in varspace");
				}		
				
				
			} else if(checkForPattern(s, "~_[~][~]")) {
				//V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=98)");
				
			} else if(checkForPattern(s, "~_[~]~")) {
				//V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=99)");
				
			} else if(checkForPattern(s, "~_[~]")) {
				//V
				ArrayList<String> contents = getPatternContents(s, "~_[~]");
				if(detectedVars.contains(contents.get(0))) {
					BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
					convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);
					MathfunctionTree mft = new MathfunctionTree(new STC("F",""));
					mft.getRoot().addR(b);
					btn.setData(new STC("V",contents.get(0),mft));
				} else {
					throw new Exception("variable "+contents.get(0)+" not found in varspace");
				}
				
				
			} else if(checkForPattern(s, "~_~[~]")) {
				//V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=100)");
			
			} else if(checkForPattern(s, "~_~ ~")) {
				//V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=101)");
				
			} else if(checkForPattern(s, "~_~")) {
				//V
				ArrayList<String> contents = getPatternContents(s, "~_~");
				if(detectedVars.contains(contents.get(0))) {
					BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
					convertFmToBtn(fm,detectedVars,contents.get(1),b);
					MathfunctionTree mft = new MathfunctionTree(b.getData());
					btn.setData(new STC("V",contents.get(0),mft));
				} else {
					throw new Exception("variable "+contents.get(0)+" not found in varspace");
				}

				
		//N or V			
			} else if(checkForPattern(s, "~^[~] [~]")) {
				//N or V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=104)");
			
			} else if(checkForPattern(s, "~^[~] ~")) {
				//N or V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=105)");
	
			} else if(checkForPattern(s, "~^[~]")) {
				//N or V				
				ArrayList<String> contents = getPatternContents(s, "~^[~]");
				BinaryTreeNode<STC> a = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),a);
				
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,fm.get("["+contents.get(1)+"]"),b);

				btn.setData(new STC("O","^"));	
				btn.addL(a);
				btn.addR(b);				
				
			} else if(checkForPattern(s, "~^~ [~]")) {
				//N or V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=104)");
			
			} else if(checkForPattern(s, "~^~ ~")) {
				//N or V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=105)");
	
			} else if(checkForPattern(s, "~^~")) {
				//N or V				
				ArrayList<String> contents = getPatternContents(s, "~^~");
				BinaryTreeNode<STC> a = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(0),a);
				
				BinaryTreeNode<STC> b = new BinaryTreeNode<STC>();
				convertFmToBtn(fm,detectedVars,contents.get(1),b);

				btn.setData(new STC("O","^"));	
				btn.addL(a);
				btn.addR(b);				
				
				
			} else if(checkForPattern(s, "~ [~]")) {
				//N or V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=102)");
			
			} else if(checkForPattern(s, "~ ~")) {
				//N or V with implicit multiplication
				throw new Exception("implicit multiplication used, not implemented. (id=103)");
	
			} else if(checkForPattern(s, "~")) {
				//N or V				
				if(s.matches(".*[0-9].*") && !(s.matches(".*[A-Z].*") || s.matches(".*[a-z].*"))){
					//N
					btn.setData(new STC(Double.parseDouble(s)));
				} else if(!s.matches(".*[0-9].*") && (s.matches(".*[A-Z].*") || s.matches(".*[a-z].*"))){
					if(detectedVars.contains(s)) {
						btn.setData(new STC("V",s));
					} else {
						btn.setData(new STC("C",s));						
					}				
				} else {
					throw new Exception("cannot distinguish N or V");
				}
			}
	}
	
	
	
	
	private boolean checkForPattern(String s, String pattern) throws Exception {		
		ArrayList<String> patterns = new ArrayList<String>(Arrays.asList(pattern.replaceAll("~", "&~&").split("&")));
		if(patterns.get(0).equals("")) {
			patterns.remove(0);
		}		
		if(patterns.size()>0) {
			for(int i=0; i<patterns.size(); i++) {
				if(patterns.get(i).equals("~")){
					if(i<patterns.size()-1) {
						String nextPat = patterns.get(i+1);
						int indexOfNextPat = s.indexOf(nextPat);
						
						if(indexOfNextPat <= 0) return false;		
						if(s.substring(0,indexOfNextPat).contains("_") 
							|| s.substring(0,indexOfNextPat).contains("[") 
							|| s.substring(0,indexOfNextPat).contains("]")
							|| s.substring(0,indexOfNextPat).contains("*") 
							|| s.substring(0,indexOfNextPat).contains("/")
							|| s.substring(0,indexOfNextPat).contains("+") 
							|| s.substring(0,indexOfNextPat).contains("-")
							|| s.substring(0,indexOfNextPat).contains("^") 
							|| s.substring(0,indexOfNextPat).contains("%")) {
							//if ~ not at the end: no special chars allowed
							return false;
						}
							
						s = s.substring(indexOfNextPat);
						
					} else {
						if(s.length() == 0) return false;
						return true;
					}
				} else {
					String pat = patterns.get(i);
					int indexOfPat = s.indexOf(pat);
					
					if(indexOfPat != 0) return false;

					s = s.substring(indexOfPat+pat.length());
				}
			}
			
			if(s.length() != 0) return false;
			
			return true;
		} else {
			throw new Exception("no pattern given");
		}
	}
	
	private ArrayList<String> getPatternContents(String s, String pattern) throws Exception {
		ArrayList<String> contents = new ArrayList<String>();
		
		ArrayList<String> patterns = new ArrayList<String>(Arrays.asList(pattern.replaceAll("~", "&~&").split("&")));
		if(patterns.get(0).equals("")) {
			patterns.remove(0);
		}		
		
		if(patterns.size()>0) {
			
			for(int i=0; i<patterns.size(); i++) {
				if(patterns.get(i).equals("~")){
					if(i<patterns.size()-1) {
						String nextPat = patterns.get(i+1);
						int indexOfNextPat = s.indexOf(nextPat);
						
						if(indexOfNextPat <= 0) throw new Exception("pattern does not match");	
						if(s.substring(0,indexOfNextPat).contains("_") 
							|| s.substring(0,indexOfNextPat).contains("[") 
							|| s.substring(0,indexOfNextPat).contains("]")
							|| s.substring(0,indexOfNextPat).contains("*") 
							|| s.substring(0,indexOfNextPat).contains("/")
							|| s.substring(0,indexOfNextPat).contains("+") 
							|| s.substring(0,indexOfNextPat).contains("-")
							|| s.substring(0,indexOfNextPat).contains("^") 
							|| s.substring(0,indexOfNextPat).contains("%")) {
							//if ~ not at the end: no special chars allowed
							throw new Exception("pattern does not match");
						}
						
						contents.add(s.substring(0,indexOfNextPat));
						s = s.substring(indexOfNextPat);
						
					} else {
						if(s.length() == 0) throw new Exception("pattern does not match");						
						contents.add(s);
						return contents;
					}
				} else {
					String pat = patterns.get(i);
					int indexOfPat = s.indexOf(pat);
					
					if(indexOfPat != 0) throw new Exception("pattern does not match");

					s = s.substring(indexOfPat+pat.length());
				}
				
			}
			
			if(s.length() != 0) throw new Exception("pattern does not match");
			
			return contents;
		} else {
			throw new Exception("no pattern given");
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
		
		//put content in <> brackets after \sqrt in own element 	
		fm.setAllUnchecked();
		for(int i=0; i<fm.size(); i++) {
			if(!fm.checked("["+i+"]")) {
				String s = fm.get("["+i+"]");
				
				if(s.contains("\\sqrt<")) {
					int closeBracketIndex = s.indexOf("\\sqrt<")+"\\sqrt<".length() +findCorrespondingBracket(s.substring(s.indexOf("\\sqrt<")+"\\sqrt<".length()+1),"<");
					String bracketcontent = s.substring(s.indexOf("\\sqrt<")+"\\sqrt<".length(), closeBracketIndex);
					s = s.substring(0, s.indexOf("\\sqrt<")+"\\sqrt<".length()-1).concat("_["+fm.add(bracketcontent)+"]").concat(s.substring(closeBracketIndex+1));
				}
				
				fm.edit("["+i+"]", s, true);		
			}
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

				s = operandsToOwnEntry(s, ",", fm);
				s = operandsToOwnEntry(s, "+", fm);
				s = operandsToOwnEntry(s, "-", fm);
				s = operandsToOwnEntry(s, "%", fm);

				
				int indexOfBackslash = s.indexOf("\\");
				if(indexOfBackslash > 0) {
					String before = s.substring(indexOfBackslash-1, indexOfBackslash);
					if(!before.equals("+") && !before.equals("-") && !before.equals("/") && !before.equals("*") && !before.equals("^") && !before.equals("%"))
					s = s.substring(0, indexOfBackslash).concat("*").concat(s.substring(indexOfBackslash));
				}
				
				s = operandsToOwnEntry(s, "*", fm);	
				s = operandsToOwnEntry(s, "/", fm);			
				
				fm.edit("["+i+"]", s, true);
			}
		}

		//find products (number*variable) and replace
		fm.setAllUnchecked();
		for(int i=0; i<fm.size(); i++) {
			if(!fm.checked("["+i+"]")) {
				String s = fm.get("["+i+"]");
				
				if(!s.contains("\\") && !s.contains("_") && !s.contains("=") && !s.contains("[") && !s.contains("^")) {
					//check if contains both letters and numbers
				    if(s.matches(".*[0-9].*") && (s.matches(".*[A-Z].*") || s.matches(".*[a-z].*"))) {
				    	s = resolveVariableNumberProd(s, fm);
						s = operandsToOwnEntry(s, "*", fm);		
				    }
				}
				
				fm.edit("["+i+"]", s, true);
			}
		}
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
		s = replaceAll(s, "~", " ");
		s = replaceAll(s, "\\cdot", "*");

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
	
	
	public Double calculate(VarSpace v) throws Exception {
		return functionTree.calculate(v);
	}
	
	
}
