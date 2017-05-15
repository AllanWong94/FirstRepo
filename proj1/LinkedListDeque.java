package Proj1;

import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class LinkedListDeque<Item> {
	public class ItemNode<Item> {
		public Item itm;
		public ItemNode next;
		public ItemNode prev;
		public ItemNode(Item item, ItemNode pre, ItemNode nxt){
			itm=item;
			next=nxt;
			prev=pre;
		}
	}
	
	
	
	private int size;
	Item randItem;
	private ItemNode itemnode;
	private ItemNode lastnode;
	private ArrayList<Item> a=new ArrayList<Item>();
	private int count=0;
	private ItemNode n;
	
	public LinkedListDeque(){
		size=0;
		itemnode=new ItemNode<Item>(randItem, null,null);
		lastnode=itemnode;
	}
	
	public LinkedListDeque(Item item){
		itemnode=new ItemNode(item, null,null);
		size=1;
		itemnode.next=new ItemNode<Item>(item,itemnode, lastnode);
		lastnode=itemnode.next;
	}
	public void addFirst(Item item){
		ItemNode oldNode=itemnode.next;
		itemnode.next=new ItemNode(item, itemnode, oldNode);
		oldNode=lastnode;
		size+=1;
	}
	public void addLast(Item item){
		ItemNode newnode=new ItemNode<Item>(item, lastnode, null);
		size+=1;
		ItemNode p=itemnode;
		lastnode.next=newnode;
		lastnode=newnode;
	}
	public boolean isEmpty(){
		if (size==0)
			return true;
		return false;
	}
	public int size(){
		return size;
	}
	public void printDeque(){
		ItemNode i=itemnode;
		while(i.next!=null){
			System.out.println(i.next.toString());
			i=i.next;
		}
		return;
	}
	public Item removeFirst(){
		Item temp=(Item) itemnode.next.itm;
		itemnode.next=itemnode.next.next;
		itemnode.next.prev=itemnode;
		size-=1;
		return temp;
	}
	public Item removeLast(){
		Item temp=(Item) lastnode.itm;
		lastnode.prev.next=null;
		return temp;
	}
	public Item get(int index){
		ItemNode n=itemnode;
		for (int t=0;t<index;t++){
			n=n.next;
		}
		return (Item) n.itm;
		
		
	}
	public Item getRecursive(int index){
		if (count==0){
			n=itemnode;
		}
		if (count<index){
			n=n.next;
			count++;
			getRecursive(index);
		}
		count=0;
		return (Item) n.next.itm;
	}
}
