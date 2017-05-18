import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Allan Wong on 2017/5/17.
 */
public class DataHolder {
    public Text displayText;
    private double charwidth;
    private double charheight;
    private double spacing;
    private double cursorPosY;
    private double cursorPosX;
    private double YOffset;
    private int size;
    private int textX=5;
    private int textY=0;
    private double windWidth;
    private double windHeight;
    private int curCursorPos;
    private int fontSize = STARTING_FONT_SIZE;
    private static final int STARTING_FONT_SIZE = 20;
    private ArrayList<double[]> cursorPos;
    private String rawData;
    private String fontName = "Verdana";
    private static final int WINDOW_MARGIN = 5;
    private javafx.scene.shape.Rectangle textBoundingBox;
    private UndoArray<String> undoArray_rawData;
    private UndoArray<Integer> undoArray_cursorPos;
    

    public DataHolder(Rectangle rec){
    	displayText = new Text(textX, textY, "");
        textBoundingBox=rec;
        cursorPos=new ArrayList<double[]>();
        curCursorPos=0;
        windWidth=500;
        windHeight=500;
        rawData="";
        YOffset=0;
        undoArray_rawData=new UndoArray<String>(100);
        undoArray_cursorPos=new UndoArray<Integer>(100);
    }

    public void append(String s){
           		rawData = rawData.substring(0,curCursorPos) +
           							s + rawData.substring(curCursorPos,rawData.length());
                size += 1;
                curCursorPos+=1;
                undoArray_cursorPos.removeToLast();
                undoArray_rawData.removeToLast();
        		undoArray_rawData.add(rawData);
        		undoArray_cursorPos.add(curCursorPos);
    }
    /*Return a string that is ready to be displayed by the displayText with the window width
            * Input param: The window width in double
            * Modifies: cursorPosX to the x-coordinate current position of the cursor
            *           cursorPosY to the y-coordinate current position of the cursor
            *           charwidth
            *           charheight
            *           spacing
            *           rawData append the newly input data to the String
            *           cursorPos to add the new position of the cursor.
            *
            *           Calls UpdateBoundingBox() that modifies:
            *           textBoundingBox*/
    public String print(){
        String temp=null;
        char c;
        double linewidth;
        int lastIndex=0;
        cursorPosX=WINDOW_MARGIN;
        cursorPosY=0;
        if (size==0)
            return null;
        cursorPos.clear();
        for (int i=0; i<rawData.length();i++) {
            c = rawData.charAt(i);
            if (i == 0){
                temp = String.valueOf(c);
                double[] k=new double[]{cursorPosX, cursorPosY};//Add the starting position of the cursor to cursorPos
                cursorPos.add(k);
            }
            else{
                temp=temp+c;
            }
            charwidth=measureCharWidth(c);
            charheight=measureCharHeight(c);
            spacing=displayText.getLineSpacing();
            if (c=='\n'){
                nextLine(spacing, charheight/2);
            }else {
                cursorPosX += charwidth;
            }
            displayText.setText(temp);
            linewidth=displayText.getLayoutBounds().getWidth();
            if (linewidth>windWidth-2*WINDOW_MARGIN){
                lastIndex=findLastBlank(temp);
                if (lastIndex<0){
                    temp=temp.substring(0,temp.length()-1)+"\r\n"+rawData.charAt(i);
                    nextLine(spacing, charheight);
                    cursorPosX+=charwidth;
                }else{
                    String latter=temp.substring(lastIndex+1, temp.length());
                    refactor(lastIndex,i,latter);
                    temp=temp.substring(0,lastIndex)+"\r\n"+latter;
                    nextLine(spacing, charheight);
                    cursorPosX+=measureStringWidth(latter);
                }

                UpdateBoundingBox();
            }
            double[] pos=new double[2];
            pos[0]=cursorPosX;
            pos[1]=cursorPosY;
            cursorPos.add(pos);
        }
        //printArray(cursorPos);
        return temp;
    }

    public void updateCursorPos(){
    	size=rawData.length();
    	String temp="";
        char c;
        double linewidth;
        int lastIndex=0;
        cursorPosX=WINDOW_MARGIN;
        cursorPosY=YOffset;
        cursorPos.clear();
        cursorPos.add(new double[]{cursorPosX, cursorPosY});
        for (int i=0; i<size;i++) {
            c = rawData.charAt(i);
            temp=temp+c;
            charwidth=measureCharWidth(c);
            charheight=measureCharHeight(c);
            spacing=displayText.getLineSpacing();
            if (c=='\n'){
                nextLine(spacing, charheight/2);
            }else {
                cursorPosX += charwidth;
            }
            displayText.setText(temp);
            linewidth=displayText.getLayoutBounds().getWidth();
            if (linewidth>windWidth-2*WINDOW_MARGIN){
                lastIndex=findLastBlank(temp);
                if (lastIndex<0){
                    temp=temp.substring(0,temp.length()-1)+"\r\n"+rawData.charAt(i);
                    nextLine(spacing, charheight);
                    cursorPosX+=charwidth;
                }else{
                    String latter=temp.substring(lastIndex+1, temp.length());
                    refactor(lastIndex,i,latter);
                    temp=temp.substring(0,lastIndex)+"\r\n"+latter;
                    nextLine(spacing, charheight);
                    cursorPosX+=measureStringWidth(latter);
                }

                UpdateBoundingBox();
            }
            double[] pos=new double[2];
            pos[0]=cursorPosX;
            pos[1]=cursorPosY;
            cursorPos.add(pos);
        }
    }
    
    public void refactor(int lastIndex, int i,String latter){

        double[] x;
        for (int k=0;k<i-lastIndex-1;k++){
            double stringwidth=measureStringWidth(latter.substring(0,k));
            x=new double[]{0,cursorPosY+charheight+spacing};
            x[0]=WINDOW_MARGIN+stringwidth;
            cursorPos.set(k+lastIndex+2, x);

        }
    }

    /*  Helper function that prints out the contents of cursorPos
               and the current cursor position.
           * */
    public void printArray(ArrayList<double[]> a){
        System.out.println("\nPrinting array:");
        for (int i=0;i<a.size();i++){
            double[] temp=a.get(i);
            System.out.println("Cursor #"+i+":");
            System.out.println(temp[0]);
            System.out.println(temp[1]);
        }
    }

    public double measureStringWidth(String s){
        double l=0;
        displayText.setText(s);
        l=displayText.getLayoutBounds().getWidth();
        return l;
    }

    public double measureCharHeight(char c){
        displayText.setText(String.valueOf(c));
        return displayText.getLayoutBounds().getHeight();
    }

    public double measureCharWidth(char c){
        displayText.setText(String.valueOf(c));
        return displayText.getLayoutBounds().getWidth();
    }

    public int getCursorIndex(){
    	return curCursorPos;
    }

    public void delete(){
    	if (curCursorPos>0){
    		if (curCursorPos<size){
    			rawData=rawData.substring(0, curCursorPos-1)+rawData.substring(curCursorPos, rawData.length());
    		}
    		else {
    			rawData=rawData.substring(0, curCursorPos-1);
			}
	    	size-=1;
	    	curCursorPos-=1;
    		System.out.println("size: "+size+"\ncurCursorPos: "+curCursorPos);
	    	render();
    		undoArray_rawData.add(rawData);
    		undoArray_cursorPos.add(curCursorPos);
    	}
       
    }
    
    public void render() {
    	displayText.setY(YOffset);
        displayText.setText(print());
        updateCursorPos();
        setCursor(curCursorPos);
    }

    public void setCursorBack(){
    	curCursorPos=rawData.length();
    	undoArray_cursorPos.setData(curCursorPos);
    }
    
    public void nextLine(double spacing, double charh){
        cursorPosY+=(spacing+charh);
        cursorPosX=WINDOW_MARGIN;
    }

    public void cursorLeft(){
        if (curCursorPos>0){
            curCursorPos-=1;
            setCursor(curCursorPos);
            System.out.println("Current cursor position: "+curCursorPos);
        }
    }

    public void cursorRight(){
        if (curCursorPos<size){
            curCursorPos+=1;
            setCursor(curCursorPos);
            System.out.println("Current cursor position: "+curCursorPos);
        }
    }

    public void cursorUp(){
        curCursorPos=findUp(curCursorPos);
        System.out.println("Current cursor position: "+curCursorPos);
    }

    public void cursorDown(){
        curCursorPos=findDown(curCursorPos);
        System.out.println("Current cursor position: "+curCursorPos);
    }

    public int findDown(int curPos){
        double[] next=new double[]{-1,-1};
        double[] prev=new double[]{-2,-1};
        double[] orig=cursorPos.get(curPos);
        while((next[0]-prev[0])>0  &&  curPos<rawData.length()){
            prev=cursorPos.get(curPos);
            curPos+=1;
            next=cursorPos.get(curPos);
        }
        while(next[0]-orig[0]<0   &&  curPos<rawData.length()-1) {
            prev = cursorPos.get(curPos);
            curPos += 1;
            next = cursorPos.get(curPos);
        }
        if ((next[0]-orig[0])>(orig[0]-prev[0])&&(next[0]>orig[0])&&(curPos!=rawData.length())){
            curPos-=1;
        }
        setCursor(curPos);
        return curPos;
    }

    public int findUp(int curPos){
        double[] next=new double[]{-1,-1};
        double[] prev=new double[]{-2,-1};
        double[] orig=cursorPos.get(curPos);
        while((next[0]-prev[0])>0  &&  curPos>0){
            next=cursorPos.get(curPos);
            curPos-=1;
            prev=cursorPos.get(curPos);
        }
        while(prev[0]-orig[0]>0) {
            next = cursorPos.get(curPos);
            curPos -= 1;
            prev = cursorPos.get(curPos);
        }
        if ((next[0]-orig[0])<(orig[0]-prev[0])&&(next[0]>orig[0])){
            curPos+=1;
        }
        setCursor(curPos);
        return curPos;
    }

    public int getSize(){
    	return size;
    }

    public void setCursor(int n){
    	curCursorPos=n;
        double[] temp=cursorPos.get(curCursorPos);
        cursorPosX=temp[0];
        cursorPosY=temp[1];
        OffsetCursorPosY();
        UpdateBoundingBox();
    }


    public void UpdateBoundingBox() {
        textBoundingBox.setHeight(charheight);
        textBoundingBox.setWidth(1);
        textBoundingBox.setX(cursorPosX);
        textBoundingBox.setY(cursorPosY);
        displayText.toFront();
    }

    public int findLastBlank(String s){
        return s.lastIndexOf(" ");
    }

    public void setRawData(String s){
    	rawData=s;
    	undoArray_rawData.setData(rawData);
    }
    
    public void OffsetCursorPosY(){
		cursorPosY+=YOffset;
	}
    
    public void setCursorPosX(double x){
		cursorPosX=x;
	}

    public void setYOffset(double y){
    	YOffset=y;
    }
    
	public void setWindWidth(double w){
    	windWidth=w;
    }
    
    public void setWindHeight(double h){
    	windHeight=h;
    }

    public String returnRawData(){
    	return rawData;
    }
    
    public int findClosest(double[] X){
    	double minRange=findRange(X, cursorPos.get(0));
    	double range=0;
    	int index=0;
    	for (int i=0;i<cursorPos.size();i++){
    		range=findRange(X, cursorPos.get(i));
    		if(range<minRange){
    			minRange=range;
    			index=i;
    		}
    	}
    	return index;
    }
    
    public double findRange(double[] X,double[] Y){
    	double range=Math.sqrt((X[0]-Y[0])*(X[0]-Y[0])+(X[1]-Y[1])*(X[1]-Y[1]));
    	return range;
    }
    
    public void undo(){
    	if (undoArray_rawData.size()==0){
    		undoArray_cursorPos.setData(curCursorPos);
    		undoArray_rawData.setData(rawData);
    	}
    	rawData=undoArray_rawData.undo();
    	curCursorPos=undoArray_cursorPos.undo();
    	render();
    }
    
    public void redo(){
    	rawData=undoArray_rawData.redo();
    	curCursorPos=undoArray_cursorPos.redo();
    	render();
    }
    
    public void fontUp(int step){
    	fontSize += step;
    	displayText.setFont(Font.font(fontName, fontSize));
    	displayText.setText(print());
    	render();
    }
    
    public void printCursorPos(){
    	System.out.println("Cursor coordinates:");
    	System.out.println("X: "+cursorPosX);
    	System.out.println("Y: "+cursorPosY);
    }
    
    
    public void fontDown(int step){
    	fontSize -= step;
    	displayText.setFont(Font.font(fontName, fontSize));
    	displayText.setText(print());
    	render();
    }
}
