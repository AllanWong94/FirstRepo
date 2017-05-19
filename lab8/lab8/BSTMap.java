package lab8;

import java.util.Iterator;
import java.util.Set;


public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
	private BST T;
	
	
	public BSTMap(){
		T=null;
	}
	

	//Return the BST item whose key equals to sk, or null if sk not found.
	public BST find(BST bst, K sk){
		if(bst==null){
			return null;
		}
		if(sk.equals(bst.key()))
			return T;
		else {
			int res=sk.compareTo((K) bst.key());
			if (res<0){
				return find(bst.left, sk);
			}else{
				return find(bst.right, sk);
			}
		}
	}
	

	//Return the BST that has inserted a size-1 BSTMap with key k and 
	//val v in the right place as a leaf.
	public BST insert(BST bst, K k, V v){
		if(bst==null){
			return new BST(k, v);
		}else{
			int res=k.compareTo((K) bst.key());//Warning!!!!!-----------------------------------------
			if(res<0){
				bst.left=insert(bst.left, k, v);
			}else{
				bst.right=insert(bst.right, k, v);
			}
		}	
		bst.size=1;
		if (bst.left!=null){
			bst.size+=bst.left.size();
		}
		if (bst.right!=null){
			bst.size+=bst.right.size();
		}
		return bst;
	}
	

	
	private class BST<K extends Comparable<K>, V>{
		private K key;
		private V val;
		public BST left;
		public BST right;
		public int size;
		private K[] keySet;
		
		public BST(K k, V v){
			key=k;
			val=v;
			size=1;
			left=null;
			right=null;
		}
		
		public BST(){
			key=null;
			val=null;
			size=0;
			left=null;
			right=null;
		}
		
		public int size() {
			return size;
		}

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
				
	};
		
	@Override
	public Iterator<K> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		T=null;
	}
	

	
	@Override
	public boolean containsKey(K key) {
		BST bst=find(T, key);
		if(bst==null){
			return false;
		}
		return true;
	}

	@Override
	public V get(K key) {
		BST bst=find(T, key);
		if(bst==null){
			return null;
		}
		return (V) bst.value();
	}

	@Override
	public int size() {
		if(T==null){
			return 0;
		}
		return T.size();
	}

	@Override
	public void put(K key, V value) {
		BST bst=find(T, key);
		if(bst==null){
			T=insert(T, key, value);
		}else{
			T.setVal(value);
		}
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
	
	
}
