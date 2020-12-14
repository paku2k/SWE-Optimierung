package swe_mo.fitnessfunction;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

@SuppressWarnings("rawtypes")
public class BinaryTreeNode<T> {
	private T nodeData;
	private BinaryTreeNode parent = null;
	private BinaryTreeNode childLeft = null;
	private BinaryTreeNode childRight = null;

	
	public BinaryTreeNode() {
		this.parent = null;
	}
	public BinaryTreeNode(T nodeData) {
		this();
		setData(nodeData);
	}


	public void setData(T nodeData) {
		this.nodeData = nodeData;
	}
	public T getData() {
		return nodeData;
	}
	public void addL(BinaryTreeNode childLeft) {
		this.childLeft = childLeft;
		childLeft.setParent(this);
	}
	public void addR(BinaryTreeNode childRight) {
		this.childRight = childRight;
		childRight.setParent(this);
	}
	public BinaryTreeNode getChildL() {
		return childLeft;
	}
	public BinaryTreeNode getChildR() {
		return childRight;
	}	
	public BinaryTreeNode getParent() {
		return parent;
	}
	private void setParent(BinaryTreeNode parent) {
		this.parent = parent;
	}
	
	
	
	
	/* TRAVERSALS */
	
	public Queue preorderTraversal() throws Exception {
		Stack<BinaryTreeNode> nodesToVisit = new Stack<BinaryTreeNode>();
		Queue<BinaryTreeNode> sequence = new LinkedList<BinaryTreeNode>();

		nodesToVisit.push(this);
		while(!nodesToVisit.empty()) {
			BinaryTreeNode node = nodesToVisit.pop();
			sequence.offer(node);
			if(node.getChildR() != null)
				nodesToVisit.push(node.getChildR());
			if(node.getChildL() != null)
				nodesToVisit.push(node.getChildL());
		}
		
		return sequence;
	}
	
	public Queue postorderTraversal() throws Exception {
		Stack<BinaryTreeNode> nodesToVisit = new Stack<BinaryTreeNode>();
		Stack<BinaryTreeNode> output = new Stack<BinaryTreeNode>();
		Queue<BinaryTreeNode> sequence = new LinkedList<BinaryTreeNode>();

		nodesToVisit.push(this);
		while(!nodesToVisit.empty()) {
			BinaryTreeNode node = nodesToVisit.pop();
			output.push(node);
			if(node.getChildL() != null)
				nodesToVisit.push(node.getChildL());
			if(node.getChildR() != null)
				nodesToVisit.push(node.getChildR());
		}
		while(!output.empty()) {
			sequence.offer(output.pop());				
		}
		
		return sequence;
	}
	
	public Queue inorderTraversal() throws Exception {
		Stack<BinaryTreeNode> nodesToVisit = new Stack<BinaryTreeNode>();
		Queue<BinaryTreeNode> sequence = new LinkedList<BinaryTreeNode>();

		BinaryTreeNode node = this;
		while(node != null || !nodesToVisit.empty()) {
			while(node != null) {
				nodesToVisit.push(node);
				node = node.getChildL();				
			}
			node = nodesToVisit.pop();
			sequence.offer(node);
			node = node.getChildR();
		}
		
		return sequence;
	}
	
	public Queue levelorderTraversal() throws Exception {
		Queue<BinaryTreeNode> nodesToVisit = new LinkedList<BinaryTreeNode>();
		Queue<BinaryTreeNode> sequence = new LinkedList<BinaryTreeNode>();

		nodesToVisit.offer(this);
		while(!nodesToVisit.isEmpty()) {
			BinaryTreeNode node = nodesToVisit.poll();
			sequence.offer(node);
			if(node.getChildL() != null)
				nodesToVisit.offer(node.getChildL());
			if(node.getChildR() != null)
				nodesToVisit.offer(node.getChildR());
		}
		
		return sequence;
	}
}
