package lab8;

import java.util.Iterator;
import java.util.Set;

import com.sun.corba.se.impl.orbutil.graph.Node;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
	
	
	private K key;
	private V val;
	public BSTMap left;
	public BSTMap right;
	public BSTMap parent;
	private int size;
	private K[] keySet;
	
	
	
	public BSTMap(K k, V v){
		key=k;
		val=v;
		size=1;
		left=null;
		right=null;
	}
	
	public BSTMap(){
		key=null;
		val=null;
		size=0;
		left=null;
		right=null;
	}
	
	
	//Return the BSTMap item whose key equals to sk, or null if sk not found.
	public BSTMap find(BSTMap T, K sk){
		if(T==null){
			return null;
		}
		if(sk.equals(key))
			return T;
		else {
			int res=sk.compareTo(key);
			if (res<0){
				return find(T.left, sk);
			}else{
				return find(T.right, sk);
			}
		}
	}
	
	//Return the BSTMap that has inserted a size-1 BSTMap with key k and 
	//val v in the right place as a leaf.
	public BSTMap insert(BSTMap T, K k, V v){
		if(T==null){
			return new BSTMap(k, v);
		}else{
			int res=k.compareTo(key);//Warning!!!!!-----------------------------------------
			if(res<0){
				T.left=insert(T.left, k, v);
			}else{
				T.right=insert(T.right, k, v);
			}
		}	
		T.size=T.left.size()+T.right.size()+1;
		return T;
	}
	
	
	private class Node{
		K label;
		V val;
		Node left;
		Node right;
		
		Node(K l, V v, Node Left, Node Right){
			label=l;
			val=v;
			left=Left;
			right=Right;
		}
		//return a Node that contains the label of l
		Node get(K l){
			if (l!=null&&l.equals(label)){
				return this;
			}
			int res=l.compareTo(label);
			if(res<0){
				if(left==null){
					return null;
				}
				return left.get(l);//Major Difference!!!!! UNKNOWN MEANING IN ULLMap.java!
			}//                      ^
			else{
				if(right==null){
					return null;
				}
				return right.get(l);
			}
		}
		
		
	};
	@Override
	public Iterator<K> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		key=null;
		val=null;
		left=null;
		right=null;
		size=0;
	}
	

	
	@Override
	public boolean containsKey(K key) {
		BSTMap T=find(this, key);
		if(T==null){
			return false;
		}
		return true;
	}

	@Override
	public V get(K key) {
		BSTMap T=find(this, key);
		if(T==null){
			return null;
		}
		return (V) T.value();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void put(K key, V value) {
		BSTMap T=find(this, key);
		if(T==null){
			insert(this, key, value);
		}
		T.setVal(value);
	}

	@Override
	public Set<K> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V remove(K key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public V remove(K key, V value) {
		throw new UnsupportedOperationException();
	}

	public void printInOrder(){
		
	}
	
	/*public BSTMap findLeastKey(BSTMap T,BSTMap Orig){
		BSTMap orig=T;
		if(T.left==null){
			if(T.right==null){
				return T;
			}
		}
		
	}*/
	
	public void setKey(K k){
		key=k;
	}

	public void setVal(V v){
		val=v;
	}
	
	public K key(){
		return key;
	}
	
	public V value(){
		return val;
	}
	
}
