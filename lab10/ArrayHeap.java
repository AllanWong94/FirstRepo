import java.util.ArrayList;

/**
 * A Generic heap class. Unlike Java's priority queue, this heap doesn't just
 * store Comparable objects. Instead, it can store any type of object
 * (represented by type T), along with a priority value.
 */
public class ArrayHeap<T> {
	private ArrayList<Node> contents = new ArrayList<Node>();

	/**
	 * Inserts an item with the given priority value. This is enqueue, or offer.
	 */
	public void insert(T item, double priority) {
		int index=contents.size();
		if (index==0){
			contents.add(null);
			index=1;
		}
		contents.add(new Node(item, priority));
		bubbleUp(index);
	}

	/**
	 * Returns the Node with the smallest priority value, but does not remove it
	 * from the heap.
	 */
	public Node peek() {
		return contents.get(1);
	}

	/**
	 * Returns the Node with the smallest priority value, and removes it from
	 * the heap. This is dequeue, or poll.
	 */
	public Node removeMin() {
		int rightMost=getRightMost();
		int last=contents.size()-1;
		swap(1, rightMost);
		swap(last, rightMost);
		Node node=contents.remove(last);
		bubbleDown(1);
		bubbleUp(rightMost);
		return node;
	}

	public int getRightMost(){
		int i=1;
		while(rightExist(i)){
			i=getRightOf(i);
		}
		return i;
	}
	
	
	/**
	 * Change the node in this heap with the given item to have the given
	 * priority. For this method only, you can assume the heap will not have two
	 * nodes with the same item. Check for item equality with .equals(), not ==
	 */
	public void changePriority(T item, double priority) {
		// TODO Complete this method!
	}

	/**
	 * Prints out the heap sideways.
	 */
	@Override
	public String toString() {
		return toStringHelper(1, "");
	}

	/* Recursive helper method for toString. */
	private String toStringHelper(int index, String soFar) {
		if (getNode(index) == null) {
			return "";
		} else {
			String toReturn = "";
			int rightChild = getRightOf(index);
			toReturn += toStringHelper(rightChild, "        " + soFar);
			if (getNode(rightChild) != null) {
				toReturn += soFar + "    /";
			}
			toReturn += "\n" + soFar + getNode(index) + "\n";
			int leftChild = getLeftOf(index);
			if (getNode(leftChild) != null) {
				toReturn += soFar + "    \\";
			}
			toReturn += toStringHelper(leftChild, "        " + soFar);
			return toReturn;
		}
	}

	private Node getNode(int index) {
		if (index >= contents.size()) {
			return null;
		} else {
			return contents.get(index);
		}
	}

	private void setNode(int index, Node n) {
		// In the case that the ArrayList is not big enough
		// add null elements until it is the right size
		while (index + 1 >= contents.size()) {
			contents.add(null);
		}
		contents.set(index, n);
	}

	/**
	 * Swap the nodes at the two indices.
	 */
	private void swap(int index1, int index2) {
		Node node1 = getNode(index1);
		Node node2 = getNode(index2);
		this.contents.set(index1, node2);
		this.contents.set(index2, node1);
	}

	/**
	 * Returns the index of the node to the left of the node at i.
	 */
	private int getLeftOf(int i) {
		return i*2;
	}

	/**
	 * Returns the index of the node to the right of the node at i.
	 */
	private int getRightOf(int i) {
		return i*2+1;
	}

	/**
	 * Returns the index of the node that is the parent of the node at i.
	 */
	private int getParentOf(int i) {
		return i/2;
	}

	/**
	 * Adds the given node as a left child of the node at the given index.
	 */
	private void setLeft(int index, Node n) {
		contents.set(index*2, n);
	}

	/**
	 * Adds the given node as the right child of the node at the given index.
	 */
	private void setRight(int index, Node n) {
		contents.set(index*2+1, n);
	}

	/**
	 * Bubbles up the node currently at the given index.
	 */
	private void bubbleUp(int index) {
		int parentInd=getParentOf(index);
		if(Fit(index)){
			if(parentInd>0)
			bubbleUp(parentInd);
			return;
		}
		if (rightExist(index)&&(!leftSmaller(index))){
			swap(index, getRightOf(index));
		}else{
			swap(index, getLeftOf(index));
		}
		if(parentInd>0)
		bubbleUp(parentInd);
		
	}
	//returns true if the left exists.
	public boolean leftExist(int index){
			return getLeftOf(index)+1<=contents.size();
	}
	//returns true if the right exists.
	public boolean rightExist(int index){
		return getRightOf(index)+1<=contents.size();
	}
	//presume right exists.
	public boolean leftSmaller(int index){
		return contents.get(getLeftOf(index)).priority()<=contents.get(getRightOf(index)).priority();
	}
	
	/**
	 * Bubbles down the node currently at the given index.
	 */
	private void bubbleDown(int index) {
		if(Fit(index)){
			return;
		}
		int swapee=0;
		if(leftExist(index)){
			double left=contents.get(getLeftOf(index)).priority();
			if(rightExist(index)){
				double right=contents.get(getRightOf(index)).priority();
				if (left<=right){
					swapee=getLeftOf(index);
				}else{
					swapee=getRightOf(index);
				}
			}else{
				swapee=getLeftOf(index);
			}
			swap(index, swapee);
			bubbleDown(swapee);
		}
	}

	private boolean Fit(int index){
		int leftInd=getLeftOf(index);
		if(leftInd+1>contents.size())
			return true;
		double left=contents.get(getLeftOf(index)).priority();
		double pri=contents.get(index).priority();
		if(pri>left){
			return false;
		}
		int rightInd=getRightOf(index);
		if(rightInd+1<=contents.size()){
			double right=contents.get(getRightOf(index)).priority();
			if(pri>right){
				return false;
			}
		}
		
		return true;
	}
	/**
	 * Returns the index of the node with smaller priority. Precondition: Not
	 * both of the nodes are null.
	 */
	private int min(int index1, int index2) {
		Node node1 = getNode(index1);
		Node node2 = getNode(index2);
		if (node1 == null) {
			return index2;
		} else if (node2 == null) {
			return index1;
		} else if (node1.myPriority < node2.myPriority) {
			return index1;
		} else {
			return index2;
		}
	}

	public class Node {
		private T myItem;
		private double myPriority;

		private Node(T item, double priority) {
			myItem = item;
			myPriority = priority;
		}

		public T item() {
			return myItem;
		}

		public double priority() {
			return myPriority;
		}

		@Override
		public String toString() {
			return item().toString() + ", " + priority();
		}
	}

	public static void main(String[] args) {
		ArrayHeap<String> heap = new ArrayHeap<String>();
		heap.insert("c", 3);
		heap.insert("i", 9);
		heap.insert("g", 7);
		heap.insert("d", 4);
		heap.insert("a", 1);
		heap.insert("h", 8);
		heap.insert("e", 5);
		heap.insert("b", 2);
		heap.insert("c", 3);
		heap.insert("d", 4);
		System.out.println(heap);
		heap.removeMin();
		System.out.println(heap);
	}

}
