package Proj1;

import Proj1.LinkedListDeque.ItemNode;

public class ArrayDeque<Item> {
	private int size;
	private int nextFirst;
	private int nextLast;
	private int capacity;
	private int resizeFactor;
	private boolean spaceNeeded;
	private Item[] items;
	
	public ArrayDeque() {
		size=0;
		nextFirst=0;
		nextLast=0;
		capacity=8;
		resizeFactor=2;
		spaceNeeded=false;
		items=(Item[])new Object[capacity];
	}
	
	public void addFirst(Item item){
		items[nextFirst]=item;
		nextFirst=getMinusOne(nextFirst);
		size+=1;
		sizeFit();
		return;
	}
	public void addLast(Item item){
		items[nextLast]=item;
		nextLast=getPlusOne(nextLast);
		size+=1;
		sizeFit();
		return;
	}
	public boolean isEmpty(){
		if (size==0){
			return true;
		}
		return false;
		
	}
	public int size(){
		return size;
	}
	public void printDeque(){
		for (int i=getPlusOne(nextFirst);i!=getMinusOne(nextLast);i=getPlusOne(i)){
			System.out.println(items[i].toString());
		}
		return;
	}
	public Item removeFirst(){
		nextFirst=getPlusOne(nextFirst);
		Item i=items[nextFirst];
		items[nextFirst]=null;
		size-=1;
		sizeFit();
		return i;
	}
	public Item removeLast(){
		nextLast=getMinusOne(nextLast);
		Item i=items[nextLast];
		items[nextLast]=null;
		size-=1;
		sizeFit();
		return i;
	}
	public Item get(int index){
		int i=getPlusOne(nextFirst)+index;
		return items[i];
	}
	
	public int getMinusOne(int index){
		int res=0;
		if ((index-1)<=0){
			res=capacity;
		}else{
			res=index-1;
		}
		return res;
	}
	public int getPlusOne(int index){
		int res=0;
		if ((index+1)>=capacity){
			res=0;
		}else{
			res=index+1;
		}
		return res;		
	}
	
	/*resize(int cap)copies the original array into a new array size of 'cap', starting from index 0 */
	public void resize(int cap){
		Item[] itms=(Item[])new Object[cap];
		int first=getPlusOne(nextFirst);
		int last=getMinusOne(nextLast);
		if (first > last){
			System.arraycopy(items, first, itms, 0, capacity-first);
			System.arraycopy(items, 0, itms, capacity-first, last+1);
			items=itms;			
		}else{
			System.arraycopy(items, 0, itms, 0, size);
		}
		capacity=cap;
		items=itms;
	}
	
	public double Usage(){
		return size/capacity;
	}
	
	public boolean needResize(){
		if (capacity<16){
			return false;
		}else{
			if (Usage()<0.25){
				spaceNeeded=false;
				return true;
			}else{
				if(nextFirst==nextLast){
					spaceNeeded=true;
					return true;
				}
			}
		}
		return false;
	}

	public void sizeFit(){
		int c=0;
		while (needResize()){
			if(Usage()<0.25 && capacity>16){
				c=capacity/resizeFactor;
				resize(c);
			}
			if(nextFirst==nextLast){
				c=capacity*resizeFactor;
				resize(c);
			}
		}
		return;
	}



}
