package data;

public class StateMatrix {
	
	private int[][] matrix = null;
	private char[] inCh = null;
//			new int[][]{
//		{0,1,2},
//		{1,3,2},
//		{2,1,5},
//		{3,3,4},
//		{4,6,5},
//		{5,6,5},
//		{6,3,4}
		
//		{0,1,2,3},
//		{1,1,5,-1},
//		{2,5,-1,-1},
//		{3,1,-1,4},
//		{4,-1,5,-1},
//		{5,-1,6,-1},
//		{6,6,-1,-1}
//	};
//	private char[] inCh = null;
//			new char[]{' ','a','b','c'};
	
	public int[][] getMatrix() {
		return matrix;
	}
	
	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}
	
	public char[] getInCh() {
		return inCh;
	}
	
	public void setInCh(char[] labelArray) {
		this.inCh = labelArray;
	}
	
	public int stateTotal() {
		int length = 0, state = -1;
		for(int i = 0; i < matrix.length; i++) {
			if(matrix[i][0] != -1 && matrix[i][0] > state) {
				length++;
				state = matrix[i][0];
			}
		}
		return length;
	}
	
}
