package swe_mo.fitnessfunction;

import java.util.ArrayList;
import java.util.Queue;

import swe_mo.solver.de.CRN;
import swe_mo.ui.clogger;

public class MathfunctionTree {
	
	final static String AUTH = "MFT";
	
	private BinaryTreeNode<STC> root;
	private VarSpace varspace;
	
	public MathfunctionTree(STC rootData) {
		root = new BinaryTreeNode<STC>(rootData);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MathfunctionTree(BinaryTreeNode treeRoot) {
		root = treeRoot;
	}
	
	
	
	
	public BinaryTreeNode<STC> getRoot(){
		return root;
	}
	
	
	
	
	public void setVarSpace(VarSpace vars) {
		this.varspace = vars;
	}
	public double getFromVarSpace(String var, int index) throws Exception {
		return varspace.get(var, index);
	}
		

	
	
	
	@SuppressWarnings("unchecked")
	public double calculate() throws Exception {		
		BinaryTreeNode<STC> resultNode = calculateNode(root);
		
		return ((STC)resultNode.getData()).number;
	}
	
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BinaryTreeNode calculateNode(BinaryTreeNode node) throws Exception {
		BinaryTreeNode<STC> replacementNode = new BinaryTreeNode();
		
		if(((STC)node.getData()).getType().equals("N")) {
			//if node is already a number
			return node;
			
		} else if(((STC)node.getData()).getType().equals("C")) {
			//if node is a constant
			double resolved = ((STC)node.getData()).resolveC();
			replacementNode = new BinaryTreeNode(new STC(resolved));
			
		} else if(((STC)node.getData()).getType().equals("V")) {
			//if node is a variable	
			Double index = 0.0;
			if(((STC)node.getData()).tree != null) {
				((STC)node.getData()).tree.setVarSpace(varspace);
				index = ((STC)node.getData()).tree.calculate();
			}
			Double resolved = getFromVarSpace(((STC)node.getData()).variable, index.intValue());
			replacementNode = new BinaryTreeNode(new STC(resolved));
			
		} else if(((STC)node.getData()).getType().equals("O")) {
			//if node is an arithmetic operator
			
			STC dataL = new STC();
				dataL.number = null;
			STC dataR = new STC();
				dataR.number = null;
				
			if(node.getChildL() != null) {
				dataL = (STC)calculateNode(node.getChildL()).getData();
				if(!dataL.getType().equals("N")) {
					throw new Exception("Cannot calculate operation node. (did not calculate left node)");								
				}
			}
			if(node.getChildR() != null) {
				dataR = (STC)calculateNode(node.getChildR()).getData();
				if(!dataR.getType().equals("N")) {
					throw new Exception("Cannot calculate operation node. (did not calculate right node)");								
				}
			} else {
				throw new Exception("Cannot calculate operation node. (right side empty)");				
			}
			String op = ((STC)node.getData()).operation;
			Double resolved = null;
			switch(op) {
				case "+":
					if(dataL.number!=null)
						resolved = dataL.number + dataR.number;
					else
						resolved = dataR.number;
					break;
				case "-":
					if(dataL.number!=null)
						resolved = dataL.number - dataR.number;
					else
						resolved = (-1.0)*dataR.number;
					break;
				case "*":
					if(dataL.number!=null)
						resolved = dataL.number * dataR.number;
					else
						throw new Exception("Cannot calculate operation node. (left node empty)");
					break;
				case "/":
					if(dataL.number!=null)
						resolved = dataL.number / dataR.number;
					else
						throw new Exception("Cannot calculate operation node. (left node empty)");
					break;
				case "^":
					if(dataL.number!=null)
						resolved = Math.pow(dataL.number, dataR.number);
					else
						throw new Exception("Cannot calculate operation node. (left node empty)");
					break;
				case "%":
					if(dataL.number!=null)
						resolved = dataL.number % dataR.number;
					else
						throw new Exception("Cannot calculate operation node. (left node empty)");
					break;
				case "rand":
					if(dataL.number!=null)
						resolved = CRN.rn(dataL.number, dataR.number);
					else
						throw new Exception("Cannot calculate operation node. (left node empty)");
					break;
				default:
					throw new Exception("Cannot calculate operation node. (unknown operation)");
			}
			
			replacementNode = new BinaryTreeNode(new STC(resolved));

		} else if(((STC)node.getData()).getType().equals("L")) {
			//if node is a special function
			String op = ((STC)node.getData()).loopop;
			Double resolved = null;
			int lB;
			int uB;
			VarSpace vsnew;
			ArrayList<Double> vsarrl;
			switch(op) {
				case "sum":
					resolved = 0.0;
					((STC)node.getData()).lowerBound.setVarSpace(varspace);
					lB = (int)((STC)node.getData()).lowerBound.calculate();
					((STC)node.getData()).upperBound.setVarSpace(varspace);
					uB = (int)((STC)node.getData()).upperBound.calculate();
					for(int i=lB; i<=uB; i++) {
						vsnew = new VarSpace(varspace);
								vsarrl = new ArrayList<Double>();
								vsarrl.add((double)i);
							vsnew.addVector(((STC)node.getData()).counterName, vsarrl);
						((STC)node.getData()).tree.setVarSpace(vsnew);
						resolved +=((STC)node.getData()).tree.calculate();
					}
					break;
				case "prod":
					resolved = 1.0;
					((STC)node.getData()).lowerBound.setVarSpace(varspace);
					lB = (int)((STC)node.getData()).lowerBound.calculate();
					((STC)node.getData()).upperBound.setVarSpace(varspace);
					uB = (int)((STC)node.getData()).upperBound.calculate();
					for(int i=lB; i<=uB; i++) {
						vsnew = new VarSpace(varspace);
								vsarrl = new ArrayList<Double>();
								vsarrl.add((double)i);
							vsnew.addVector(((STC)node.getData()).counterName, vsarrl);
						((STC)node.getData()).tree.setVarSpace(vsnew);
						resolved *=((STC)node.getData()).tree.calculate();
					}
					break;
				default:
					throw new Exception("Cannot calculate operation node. (unknown operation)");
			}
			replacementNode = new BinaryTreeNode(new STC(resolved));
			
		} else if(((STC)node.getData()).getType().equals("S")) {
			//if node is a special function
			String op = ((STC)node.getData()).specialop;
			Double resolved = null;
			((STC)node.getData()).tree.setVarSpace(varspace);
			switch(op) {
				case "sqrt":
					resolved = Math.sqrt(((STC)node.getData()).tree.calculate());
					break;
				case "ln":
					resolved = Math.log(((STC)node.getData()).tree.calculate());
					break;
				case "sin":
					resolved = Math.sin(((STC)node.getData()).tree.calculate());
					break;
				case "cos":
					resolved = Math.cos(((STC)node.getData()).tree.calculate());
					break;
				case "abs":
					resolved = Math.abs(((STC)node.getData()).tree.calculate());
					break;
				case "floor":
					resolved = Math.floor(((STC)node.getData()).tree.calculate());
					break;
				case "ceil":
					resolved = Math.ceil(((STC)node.getData()).tree.calculate());
					break;
				default:
					throw new Exception("Cannot calculate operation node. (unknown operation)");
			}
			replacementNode = new BinaryTreeNode(new STC(resolved));
			
		} else if(((STC)node.getData()).getType().equals("P")) {
			//if node is a special function
			String op = ((STC)node.getData()).paramop;
			Double resolved = null;
			((STC)node.getData()).tree.setVarSpace(varspace);
			((STC)node.getData()).parameter.setVarSpace(varspace);
			switch(op) {
				case "log":
					resolved = (double)Math.log(((STC)node.getData()).tree.calculate()) / (double)Math.log(((STC)node.getData()).parameter.calculate());
					break;
				case "nroot":
					resolved = Math.pow(((STC)node.getData()).tree.calculate(), 1.0/((STC)node.getData()).parameter.calculate());
					break;
				default:
					throw new Exception("Cannot calculate operation node. (unknown operation)");
			}
			replacementNode = new BinaryTreeNode(new STC(resolved));
			
		} else if(((STC)node.getData()).getType().equals("F")) {
			//if node is a function
			Double resolved = null;
			if(node.getChildL() != null && node.getChildR() == null) {
				resolved = ((STC)calculateNode(node.getChildL()).getData()).number;			
			} else if(node.getChildL() == null && node.getChildR() != null) {
				resolved = ((STC)calculateNode(node.getChildR()).getData()).number;			
			} else if(node.getChildL() == null && node.getChildR() == null) {
				throw new Exception("Cannot resolve function node. (no expression given)");								
			} else {
				throw new Exception("Cannot clearly resolve function node. (expression on both sides)");				
			}
			
			replacementNode = new BinaryTreeNode(new STC(resolved));
			
		} else {
			throw new Exception("Cannot calculate node. (STC type unknown)");
		}
		
		return replacementNode;
	}
	

	
	
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String toString() {
		try {
			String s = "";
			Queue<BinaryTreeNode> sequence;
			
		/*
		 	sequence = root.preorderTraversal();
			while(!sequence.isEmpty()) {
				BinaryTreeNode node = sequence.poll();
				s += ((STC)node.getData()) + " ";
			}
			s += "\n";
		
			sequence = root.postorderTraversal();
			while(!sequence.isEmpty()) {
				BinaryTreeNode node = sequence.poll();
				s += ((STC)node.getData()) + " ";
			}
			s += "\n";
		*/
			sequence = root.inorderTraversal();
			while(!sequence.isEmpty()) {
				BinaryTreeNode node = sequence.poll();
				s += ((STC)node.getData()) + " ";
			}
		/*
			s += "\n";
			
			sequence = root.levelorderTraversal();
			while(!sequence.isEmpty()) {
				BinaryTreeNode node = sequence.poll();
				s += ((STC)node.getData()) + " ";
			}
		*/
			
			return s;
		} catch(Exception e) {
			clogger.err(AUTH, "toString", e);
			return "";
		}
	}
}
