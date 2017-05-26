/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 *
 **/
public class RadixSort
{
	public static int[][] countingSort(int[][] arr,int index){
		int[] counts=new int[256];
		int[][] sorted=new int[arr.length][];
		for(int[] ar:arr){
			counts[ar[index]]++;
		}
		counts=add(counts);
		for(int[] ar:arr){
			sorted[counts[ar[index]]]=ar;
			counts[ar[index]]++;
		}
		return sorted;
	}
	
	public static String[] printFromIntArr(int[][] arr){
		String[] sArray=new String[arr.length];
		for (int i=0;i<arr.length;i++){
			sArray[i]=IntArray2String(arr[i]);
		}
		return sArray;
		}
	
	
	//Presumed length>=s.length
	//If res is longer than c, then res will be padded -1 until its end. As spaces are
	//regarded as less than any other characters and thus represented as 0;
	public static int[] String2IntArray(String s,int length){
		char[] c=s.toCharArray();
		int[] res=new int[length];
		int size=c.length;
		for(int i=0;i<size;i++){
			res[i]=(int)c[i];
		}
		for(int i=size;i<length;i++){
			res[i]=0;
		}
		return res;
	}
	
	public static int[][] StringArray2IntArray(String[] sArray){
		int[][] res=new int[sArray.length][];
		int length=findLongest(sArray);
		for (int i=0;i<sArray.length;i++){
			res[i]=String2IntArray(sArray[i], length);
		}
		return res;
	}
	
	public static String IntArray2String(int[] arr){
		char[] c=new char[arr.length];
		for(int i=0;i<arr.length;i++){
			c[i]=(char)arr[i];
		}
		return String.valueOf(c);
	}
	
	

	public static int findLongest(String[] sArray){
		int max=Integer.MIN_VALUE;
		for(String s:sArray){
			if(s.length()>max)
				max=s.length();
		}
		return max;
	}
    /**
     * Does Radix sort on the passed in array with the following restrictions:
     *  The array can only have ASCII Strings (sequence of 1 byte characters)
     *  The sorting is stable and non-destructive
     *  The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     **/
    public static String[] sort(String[] asciis)
    {
    	String[] sorted;
    	int length=findLongest(asciis);
    	sorted=printFromIntArr(countingSort(StringArray2IntArray(asciis), length-1));
    	for(int i=length-2;i>=0;i--){
    		sorted=printFromIntArr(countingSort(StringArray2IntArray(sorted), i));
    	}
    	return sorted;
    }

    
    public static void main(String[] args) {
    	
	//	for(int i:arr)
		//	System.out.println(i);
    	String[] arr={"Allantis","Altruis","Allen","Alen","Alan","Allan"};
    	print(sort(arr));
    	/*System.out.println((int)'A');
    	int[] a=String2IntArray(arr[0], 5);
    	String s=IntArray2String(a);
    	System.out.println(s);*/
    	
    	
    	
    	
    	
    	
    	
		
	}
    
    public static void print(String[] sorted){
    	for(String s:sorted){
    		System.out.println(s);
    	}
    }
    
    //(1,2,0,1)=>(0,1,3,3)
    public static int[] add(int[] arr){
    	int[] res=new int[arr.length];
    	res[0]=0;
    	for(int i=1;i<arr.length;i++){
    		res[i]=res[i-1]+arr[i-1];
    	}
    	return res;
    }

}
