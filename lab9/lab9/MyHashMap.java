package lab9;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Multiset.Entry;

public class MyHashMap<K, V> implements Map61B<K, V>{
	private HashSet<K> keys;
	private ArrayList<Entry>[] arrayLists;
	private int size;
	private double LoadFactor;
	private int resizeFactor;
	
	private class Entry<K, V>{
		public K key;
		public V val;
		
		public Entry(K k,V v){
			key=k;
			val=v;
		}
		
	}
	
	
	public MyHashMap(){
		size=5;
		LoadFactor=5;
		keys=new HashSet<K>();
		resizeFactor=2;
		arrayLists=new ArrayList[size];
	}
	public MyHashMap(int initialSize){
		size=initialSize;
		LoadFactor=5;
		keys=new HashSet<K>();
		resizeFactor=2;
		arrayLists=new ArrayList[size];
	}
	public MyHashMap(int initialSize, double loadFactor){
		size=initialSize;
		LoadFactor=loadFactor;
		keys=new HashSet<K>();
		resizeFactor=2;
		arrayLists=new ArrayList[size];
	}
	
	public int modHashCode(Object obj){
		int i=obj.hashCode();
		while(i<0)
		{
			i+=size;
		}
		return i%size;
	}
	
	public void add(K k,V v){
		if (!keys.contains(k)){
			Entry ent=new Entry<K, V>(k, v);
			keys.add(k);
			int bucket=modHashCode(k);
			if (bucket==-1){
				bucket=size;
			}
			arrayLists[bucket].add(ent);
			if (tooLong()){
				resize();
			}
		}
		System.out.println("Entry with key "+k+" already existed!");
	}
	
	private void add(Entry entry){
		int bucket=modHashCode(entry.key);
		if (bucket==-1){
			bucket=size;
		}
		ArrayList<Entry> entries=arrayLists[bucket];
		if(entries==null){
			entries=new ArrayList<Entry>();
		}
		entries.add(entry);
		arrayLists[bucket]=entries;
		keys.add((K) entry.key);
		if(tooLong()){
			resize();
		}
	}
	
	
	public void resize(){
		size=size*resizeFactor;
		ArrayList<Entry>[] oldArrayLists=arrayLists;
		arrayLists=new ArrayList[size];
		Entry temp;
		for(ArrayList<Entry> al:oldArrayLists){
			if(al!=null){
				for(int i=0;i<al.size();i++){
					temp=al.get(i);
					add(temp);
				}
			}
			
		}
		if (tooLong()){
			resize();
		}
	}
	
	public boolean tooLong(){
		for (ArrayList<Entry> al:arrayLists){
			if (al!=null){
				if(al.size()>LoadFactor)
					return true;
			}
		}
		return false;
	}
	
	@Override
	public Iterator<K> iterator() {
		return keys.iterator();
	}

	@Override
	public void clear() {
		for (ArrayList<Entry> al:arrayLists){
			if(al!=null)
				al.clear();
		}
		keys.clear();
	}

	@Override
	public boolean containsKey(K key) {
		return keys.contains(key);
	}

	@Override
	public V get(K key) {
		if(containsKey(key)){
			int i=modHashCode(key);
			ArrayList<Entry> entries=arrayLists[i];
			for(Entry ent:entries){
				if (ent.key.equals(key))
					return (V) ent.val;
			}
		}
		return null;
	}

	public Entry getEntry(K key) {
		if (containsKey(key)){
			int i=modHashCode(key);
			ArrayList<Entry> entries=arrayLists[i];
			for(Entry ent:entries){
				if (ent.key.equals(key))
					return ent;
			}
		}
		return null;
		
	}
	@Override
	public int size() {
		return keys.toArray().length;
	}

	@Override
	public void put(K key, V value) {
		// TODO Auto-generated method stub
		if (containsKey(key)){
			Entry ent=getEntry(key);
			ent.val=value;
		}else{
			add(new Entry<K, V>(key, value));
		}
	}

	
	
	
	@Override
	public Set<K> keySet() {
		return keys;
	}

	@Override
	public V remove(K key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public V remove(K key, V value) {
		throw new UnsupportedOperationException();
	}

}
