package data;

public class StateMatrix {
	
	private int[][] matrix = new int[][]{
		{0,1,2},
		{1,3,2},
		{2,1,5},
		{3,3,4},
		{4,6,5},
		{5,6,5},
		{6,3,4}
		
//		{0,1,2,3},
//		{1,1,5,-1},
//		{2,5,-1,-1},
//		{3,1,-1,4},
//		{4,-1,5,-1},
//		{5,-1,6,-1},
//		{6,6,-1,-1}
	};
	private char[] inCh = new char[]{'a','b'};
	
	public int[][] getMatrix() {
		return matrix;
	}
	
	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}
	
	public char[] getInCh() {
		return inCh;
	}
	
	public void setInCh(char[] inCh) {
		this.inCh = inCh;
	}
	
	public int stateTotal() {
		return matrix.length;
	}
	
}