package com.wangxy.exoskeleton.risk.matrix;

public class MatrixAddition {
 
	public static double[][] matrixAddition(double[][] matrixA, double[][] matrixB) {
		int k = matrixA.length;
		int l = matrixA[0].length;
		int m = matrixB.length;
		int n = matrixB[0].length;
		if (k != m || l != n) {
			return null;
		} else {
			double[][] addition = new double[k][l];
			for (int i = 0; i < k; i++) {
				for (int j = 0; j < l; j++) {
					addition[i][j] = matrixA[i][j] + matrixB[i][j];
				}
			}
			return addition;
		}
	}
	
}
