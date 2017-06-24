package data;

public class StateMatrix {
	
	private int[][] matrix = null;
	private char[] inCh = null;
	
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
