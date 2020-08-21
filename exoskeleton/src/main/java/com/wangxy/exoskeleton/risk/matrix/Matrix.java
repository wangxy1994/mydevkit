package com.wangxy.exoskeleton.risk.matrix;

public class Matrix {

	private double[][] matrix;
	private int rowCnt;
	private int colCnt;

	public Matrix(double[][] matrixA) {
		matrix = matrixA;
		if (matrixA != null) {
			rowCnt = matrix.length;
			colCnt = matrix[0].length;
		}
	}

	public double[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(double[][] matrixA) {
		this.matrix = matrixA;
	}
	
	/**
	 * 转换成字符串
	 */
	public static String toString(double[][] matrix) {
		if(matrix!=null&&matrix[0]!=null){
			StringBuilder sbr = new StringBuilder();
			int rowCnt = matrix.length;
			int colCnt = matrix[0].length;
			sbr.append("Matrix[").append(rowCnt).append(",").append(colCnt).append("]\n");
			for (int i = 0; i < rowCnt; i++) {
				for (int j = 0; j < colCnt; j++) {
					sbr.append("\t").append(matrix[i][j]);
				}
				sbr.append("\n");
			}
			return sbr.toString();
		}
		return null;
	}

	/**
	 * 转换成字符串
	 */
	public String toString() {
		return toString(matrix);
	}

	/**
	 *求矩阵行列式的值
	 */
	public double getDeterminant() {
		double determinant = 0;
		if (rowCnt == 1) {
			return matrix[0][0];
		} else {
			for (int i = 0; i < colCnt; i++) {
				determinant += matrix[0][i] * getCofactor(0, i);
			}
			return determinant;
		}
	}

	/**
	 *求代数余子式的值
	 */
	public double getCofactor(int i, int j) {
		int power = (i + 1) + (j + 1);
		double coefficient = Math.pow(-1, power);
		double[][] factor = new double[rowCnt - 1][colCnt - 1];
		for (int m = 0; m < rowCnt - 1; m++) {
			for (int n = 0; n < colCnt - 1; n++) {
				if (m < i && n < j)
					factor[m][n] = matrix[m][n];
				else if (m < i && n >= j)
					factor[m][n] = matrix[m][n + 1];
				else if (m >= i && n < j)
					factor[m][n] = matrix[m + 1][n];
				else
					factor[m][n] = matrix[m + 1][n + 1];
			}
		}
		Matrix temp = new Matrix(factor);
		return coefficient * temp.getDeterminant();
	}

	/**
	 *求矩阵的转置矩阵
	 */
	public double[][] getTranspose() {
		double[][] transpose = new double[colCnt][rowCnt];
		for (int i = 0; i < colCnt; i++) {
			for (int j = 0; j < rowCnt; j++){
				transpose[i][j] = matrix[j][i];
			}
		}
		return transpose;
	}

	/**
	 *求伴随矩阵
	 */
	public double[][] getAdjugate() {
		double[][] adjugate = new double[rowCnt][colCnt];
		for (int i = 0; i < rowCnt; i++) {
			for (int j = 0; j < colCnt; j++) {
				adjugate[j][i] = getCofactor(i, j);
			}
		}
		return adjugate;
	}

	/**
	 *求矩阵的逆矩阵
	 */
	public double[][] getInverse() {
		double inverse[][] = new double[rowCnt][colCnt];
		double temp[][] = this.getAdjugate();
		double d = this.getDeterminant();
		if (d == 0){
			return null;
		} else {
			for (int i = 0; i < rowCnt; i++) {
				for (int j = 0; j < colCnt; j++) {
					inverse[i][j] = temp[i][j] / this.getDeterminant();
				}
			}
		}
		return inverse;
	}
	
	/**
	 *求矩阵与数的乘积
	 *@param a
	 *@author daijy
	 *@since 20181020
	 */
	public double[][] getMulti(double a) {
		double multi[][] = new double[rowCnt][colCnt];
		for (int i = 0; i < rowCnt; i++) {
			for (int j = 0; j < colCnt; j++) {
				multi[i][j] = multi[i][j] * a;
			}
		}
		return multi;
	}
	
}
