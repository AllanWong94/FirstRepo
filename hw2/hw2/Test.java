package hw2;


public class Test {

	public static void main(String[] args) {
		Percolation p=new Percolation(10);
		for (int i=0;i<10;i++){
			if(i!=5)
				p.open(i, 0);
		}
		p.open(4, 1);
		p.open(4, 2);
		p.open(5, 2);
		p.open(6, 1);
		p.open(6, 2);
		p.open(6, 2);
		p.open(6, 2);
		System.out.println(p.percolates());
		System.out.println(p.numberOfOpenSites());
	}

}
