package data;

/**
 * 状态转换矩阵类
 * @author ele
 *
 */
public class StateMatrix {
	
	private int[][] matrix = null;
	private char[] inCh = null;
	
	/**
	 * 从StateMatrix对象中获取存有状态矩阵的二维数组
	 * @return
	 */
	public int[][] getMatrix() {
		return matrix;
	}
	
	/**
	 * 将存有状态矩阵的二维数组添加到StateMatrix对象中
	 * @param matrix
	 */
	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}
	
	/**
	 * 从StateMatrix对象中获取存有字符集的字符数组
	 * @return
	 */
	public char[] getInCh() {
		return inCh;
	}
	
	/**
	 * 将字符集以字符数组形式添加到StateMatrix对象中，作为状态矩阵表的表头
	 * @param labelArray
	 */
	public void setInCh(char[] labelArray) {
		this.inCh = labelArray;
	}
	
	/**
	 * 获取二维整型数组第一列的状态总数
	 * @return
	 */
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
