package com.wangxy.exoskeleton.risk;

public class AdaBoostCal {
	final double  MINERR=0.00001;
	
	/**
	 * 计算可能的划分点
	 * @param x
	 * @return
	 */
	public double[] generateGxList(double[] x) {
		double[] gxlist = new double[x.length];
		for (int i = 0; i < x.length; i++) {// in range(len(x) - 1):
			double gx = (x[i] + x[i + 1]) / 2;
			gxlist[i] = gx;

		}
		return gxlist;
	}

	/**
	 * 计算弱分类器权重
	 * @param minError
	 * @return
	 */
	public double calcAlpha(double minError) {
		double alpha = 1 / 2 * Math.log((1 - minError) / minError);
		return alpha;
	}

	/**
	 * 计算当前弱分类器线性组合的错误率     
	 * @param fx
	 * @param n
	 * @param x
	 * @param y
	 * @return
	 */
	public double calcFxError(double[][] fx, int n, double[] x, double[] y) {
		int errorNum = 0;
		double fgx = 0;
		for (int i = 0; i < x.length; i++) {// in range(len(x)):
			int fi = 0;
			for (int j = 0; i < n; j++) {
				double fxiAlpha = fx[j][0];
				double fxiGx = fx[j][1];
				double ygx = fx[j][2];
				if (i < fxiGx) {
					fgx = ygx;
				} else {
					fgx = -ygx;
				}
				fi += fxiAlpha * fgx;
				if (Math.signum(fi) != y[i]) {
					errorNum += 1;
				}
			}
		}
		return errorNum / x.length;
	}

	/**
	 * 计算样本新权重
	 * @param alpha
	 * @param ygx
	 * @param weight
	 * @param gx
	 * @param y
	 * @return
	 */
	public double[] calcNewWeight(double alpha,double ygx, double[] weight, double gx, double[]y){
	    double newWeight[] = new double[weight.length];
	    double sumWeight = 0;
	    for (int i=0;i<weight.length;i++){//i in range(len(weight)):
	        int flag = 1;
	        if( i < gx && y[i] != ygx)flag = -1;
	        if (i > gx && y[i] != -ygx) flag = -1;
	        double weighti = weight[i]*Math.exp(-alpha*flag);
	        newWeight[i]=weighti;
	        sumWeight += weighti;
	    }
	    for(int i=0;i<weight.length;i++)
	    {
	    	newWeight[i] = newWeight[i] / sumWeight;
	    }
	    return newWeight;
	}
	
	/**
	 * 训练基本弱分类器
	 * @param fx
	 * @param i
	 * @param x
	 * @param y
	 * @param weight
	 * @return
	 */
	public double[] trainfxi(double[] fx, int i, double[] x, double[] y, double[] weight) {
		double minError = Double.MAX_VALUE;
		double bestGx = 0.5;
		double[] gxlist = generateGxList(x);
		double bestygx = 1;
		// 计算基本分类器
		for (int xi = 0; xi < gxlist.length; xi++) {
			double[] ret = calcErrorNum(xi, x, y, weight);
			double ygx = ret[1];
			double error = ret[0];
			if (error < minError) {
				minError = error;
				bestGx = xi;
				bestygx = ygx;
			}
		}

		fx['1'] = bestGx;
		//#计算alpha
		double alpha = calcAlpha(minError);
		fx['0'] = alpha;
		fx['2'] = bestygx;
		//计算新的训练数据权值
		double newWeight[] = calcNewWeight(alpha, bestygx, weight, bestGx, y);
		return newWeight;
	}

	/**
	 * 计算错误次数
	 * @param gx
	 * @param x
	 * @param y
	 * @param weight
	 * @return
	 */
	public double[] calcErrorNum(double gx, double[] x, double[] y, double[] weight) {
		// #判断以gx为切分点的两种方式里，哪种会让误差更小
		double error1 = 0;
		double errorNeg1 = 0;
		double ygx = 1;
		for (int i = 0; i < x.length; i++) //i in range(len(x)):
		{
			if (i < gx && y[i] != 1) {
				error1 += weight[i];
			}
			if (i > gx && y[i] != -1)
				error1 += weight[i];
			if (i < gx && y[i] != -1)
				errorNeg1 += weight[i];
			if (i > gx && y[i] != 1)
				errorNeg1 += weight[i];
		}
		double[] ret = new double[2];
		if (errorNeg1 < error1) {
			ret[0] = errorNeg1;
			ret[1] = -1;//#x>gx,则fgx = 1
		} else {
			ret[0] = error1;
			ret[1] = 1;//#x>gx,则fgx = 1
		}
		return ret;
	}
	
	/**
	 * 训练强分类器
	 * @param x
	 * @param y
	 * @param errorThreshold
	 * @param maxIterNum
	 * @return
	 */
	public double[][] trainAdaBoost(double x[],double y[], double errorThreshold,double maxIterNum){
	    double fx[][] = new double[x.length][3];
	    double weight[] = new double[x.length];
	    int xNum = x.length;
	    for(int i=0;i<xNum; i++){
	       double w = 1d/xNum;
	        weight[i]=w;
	    }

	    for(int i=0;i<maxIterNum; i++){
	        //fx[i] = {}
	        double newWeight[] = trainfxi(fx[i], i, x, y, weight);
	        weight = newWeight;
	        double fxError = calcFxError(fx, i+1, x, y);
	        if(fxError < errorThreshold) break;
	    }
	    return fx;
	}
}
