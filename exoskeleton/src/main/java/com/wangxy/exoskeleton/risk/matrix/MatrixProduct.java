package com.wangxy.exoskeleton.risk.matrix;

public class MatrixProduct {
 	
	public static double[][] matrixProduct(double[][] matrixA, double[][] matrixB) {
		int k = matrixA.length;
		int l = matrixA[0].length;
		int m = matrixB.length;
		int n = matrixB[0].length;
		if (l != m){
			return null;
		} else {
			double[][] product = new double[k][n];
			for (int i = 0; i < k; i++){
				for (int j = 0; j < n; j++) {
					product[i][j] = 0;
					for (int q = 0; q < m; q++) {
						product[i][j] += matrixA[i][q] * matrixB[q][j];
					}
				}
			}
			return product;
		}
	}

}
