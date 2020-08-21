package com.wangxy.exoskeleton.risk.util;

import java.math.BigDecimal;

/********************************************
 * 文件名称: BigDecimalUtil.java
 * 系统名称: 资金风险管理系统
 * 模块名称:
 * 软件版权:  
 * 功能说明: 精确计算处理类
 * 系统版本: 
 * 开发人员:  
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *               20190822    daijy         增加指数计算方法
 *********************************************/
public class BigDecimalUtil {
 
    public static final String VERSION="@system 资金交易风险管理系统V2.0  @version 2.5.0.1  @lastModiDate  2019-07-09  @describe ";
	
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 16;

	/**
	 * 
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * 
	 * @param v2
	 *            加数
	 * 
	 * @return 两个参数的和
	 * 
	 */

	public static double add(double v1, double v2) {

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.add(b2).doubleValue();

	}
	
	/**
	 * 多个参数相加（使用慎重） ，参数从左到右依次相加
	 * @param param
	 * @return
	 */
	public static double add(double ... param){
	      if(0==param.length){
	    	  throw new IllegalArgumentException("illegal argument");
	      }
		  double retValue=0.0D;
	      for(int i=0;i<param.length;i++){
	    	  retValue=BigDecimalUtil.add(retValue,param[i]);
	      }
	      return retValue;
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.subtract(b2).doubleValue();

	}
	
	/**
	 * 多个参数相减少，从左到右依次相减
	 * @param param
	 * @return
	 */
	public static double sub(double ...param){
		if(param.length==0){
			throw  new IllegalArgumentException("illegal argument");
		}
		double retValue=param[0];
		for(int i=1;i<param.length;i++){
			retValue=BigDecimalUtil.sub(retValue,param[i]);
		}
		return retValue;
	}
	

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.multiply(b2).doubleValue();

	}
	
	/**
	 * 多个double相乘，从左到右依次相乘法
	 * @param param
	 * @return
	 */
	public static double mul(double ...param){
		if(0==param.length){
			throw new java.lang.IllegalArgumentException("illegal argument");
		}
		double retValue=1;
	    for(int i=0;i<param.length;i++){
	    	retValue=BigDecimalUtil.mul(retValue,param[i]);
	    }
		return retValue;
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {

		return div(v1, v2, DEF_DIV_SCALE);

	}

	/**
	 * 
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 
	 * 定精度，以后的数字四舍五入。
	 */

	public static double div(double v1, double v2, int scale) {

		if (scale < 0) {

			throw new IllegalArgumentException(

			"The scale must be a positive integer or zero");

		}

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}
	
	/**
	 * 提供精确的指数运算。
	 * 
	 * @param number
	 *            底数
	 * @param power
	 *            指数
	 * @return 底数的指数次方
	 * @author daijy
	 * @since 20190822
	 */
	public static double power(double number, int power) {

		BigDecimal b1 = new BigDecimal(Double.toString(number));

		return b1.pow(power).doubleValue();

	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {

		if (scale < 0) {
			throw new IllegalArgumentException(
			"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		
	}
	
	/**
	 * 提供精确的小数位数进位处理
	 * @param v
	 *        需要进位的数字
	 * @param scale
	 *        小数点后保留几位
	 * @return 进位后的结果
	 */
	public static double carry(double v ,int scale){
		if (scale < 0) {
			throw new IllegalArgumentException(
			"The scale must be a positive integer or zero");
		}

		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_CEILING).doubleValue();
	}

    
	/**
     * 	提供精确的小数位数截位处理
     * @param v
     *        需要截位的数字
     * @param scale
     *        小数点后保留几位
     * @return 截位后的结果
     */
	public static double trunc(double v,int scale){
		if (scale < 0) {
			throw new IllegalArgumentException(
			"The scale must be a positive integer or zero");
		}
		//return Math.floor(v * Math.pow(10, scale)) 
		/// Math.pow(10, scale);
		return  BigDecimalUtil.div( Math.floor( BigDecimalUtil.mul(v , Math.pow(10, scale))) , Math.pow(10, scale));
	}
	
	/**
	 * 判断两个数值的大小
	 * @param val1  
	 * @param val2
	 * @return  0--相等    >0的整数---va1>val2    <0的整数   val1<val2
	 */
    public static int compare(double val1,double val2){
        return Double.compare(val1,val2);        
    }
    
    
    /**
     * 根据精度处理方式和位数处理精度
     * @param inpuVal 输入数字
     * @param scale   精度位数
     * @param mode    精度处理方式 0:四舍五入;1:截位;2:进位，默认按四舍五入处理
     * @return 处理结果
     */
	public static double precision(double inputVal, int scale, int mode){
		if(mode == 1){
			return trunc(inputVal, scale);
		}
		else if(mode == 2){
			return carry(inputVal, scale);
		}
		else{
			return round(inputVal, scale);			
		}
	}
	
	
	
	 /**
	  * 检查double型数据小数点后的有效位数,是否在指定的有效数字范围内
	  * @param value  待检查有效位数的double型值
	  * @param num    有效位数
	  * @return
	  */
	 public static boolean checkEffectiveNumberBits(double value,int num){
			int i=0;
			double tmpAmt= Math.abs(BigDecimalUtil.sub(value, new BigDecimal(Double.toString(value)).longValue()));   //获取小数部分，再判断精度
			if(num > 0 && tmpAmt > 0){
				for(i=0; i<num;i++)
					tmpAmt= BigDecimalUtil.mul(tmpAmt, 10);
				if( BigDecimalUtil.compare(tmpAmt,Math.floor(tmpAmt))> 0)
					return false;
			}
			return true;
	 }
     
     /**
      * 比较两个值取最小值
      * @param val1  
      * @param val2
      * @return
      */
	 public static double minValue(double val1,double val2){
		 if(BigDecimalUtil.compare(val1, val2)>=0){
			   return val2;
		 }else{
			   return val1;
		 }
	 }
	 
	 /**
	  * 比较两个值取最大值
	  * @param val1
	  * @param val2
	  * @return
	  */
	 public static double maxValue(double val1,double val2){
		 if(BigDecimalUtil.compare(val1, val2)<=0){
			   return val2;
		 }else{
			   return val1;
		 }
		 
	 }
	 
	 /**
	 * 整除判断<br>
	 * (v1能否被v2整除的判断,v2为零返回true;)
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static boolean modIsZero(double v1, long v2) {
		if (v2 == 0) {
			return true;
		}

		BigDecimal d1 = BigDecimal.valueOf(v1);
		BigDecimal d2 = BigDecimal.valueOf(v2);

		if (d1.remainder(d2).compareTo(BigDecimal.valueOf(0)) == 0) {
			return true;
		} else {
			return false;
		}
	}

	 /**
	 * 整除判断<br>
	 * (v1能否被v2整除的判断,v2为零返回true;)
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static boolean modIsZero(double v1, double v2) {
		if (Double.compare(v2, 0.0d) == 0) {
			return true;
		}

		BigDecimal d1 = BigDecimal.valueOf(v1);
		BigDecimal d2 = BigDecimal.valueOf(v2);

		if (d1.remainder(d2).compareTo(BigDecimal.valueOf(0)) == 0) {
			return true;
		} else {
			return false;
		}
	}

}
