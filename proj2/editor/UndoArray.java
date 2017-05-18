import java.awt.print.Printable;
import java.util.ArrayList;


public class UndoArray<E> extends ArrayList<E> {
		private int size;
		private int cursor;//records the cursor indicating which copy of rawD is in use at present
		private E Data;
		public UndoArray(int si) {
			size=si;
			cursor=-1;
		}
		
		@Override
		public boolean add(E e){
			if (size>0){
				super.add(e);
				if(size()<size){
					if (size()>0)
						cursor+=1;
				}else if (size==size()){
					remove(0);
				}
				return true;
			}
			return false;
		}
		
		public void print(){
			for (int i=0;i<size();i++){
				System.out.println(this.get(i));
			}
		}
		
		public E undo(){
			E e;
			if (cursor<=0){
				System.out.println("Maximum undo operations # reached! Cannot undo anymore!");
			}else{
				cursor-=1;				
			}
			if((size()>0)&&(cursor>=0)){
				e=this.get(cursor);
			}else {
				e=Data;
			}
			return e;
		}
		
		public E redo(){
			E e;
			if((cursor==size()-1)||(size()==0)){
				System.out.println("Cannot redo anymore!");
			}else{
				cursor++;
			}
			if(size()>0){
				e=this.get(cursor);
			}else {
				e=Data;
			}
			return e;
		}
		
		public void setData(E e){
			Data=e;
			this.add(e);
		}
		
		public void removeToLast(){
			if((size()>0)&& (cursor<size())){
				this.removeRange(cursor+1, size());
			}
		}
		public void setCursor(int s){
			cursor=s;
		}
		
}
