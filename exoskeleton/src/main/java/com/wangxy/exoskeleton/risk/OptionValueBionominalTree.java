package com.wangxy.exoskeleton.risk;
public class OptionValueBionominalTree
{
 
	double asset =100.0;
	double volatility =0.2;
	double intrate=0.1;
	double strike=100.0;
	double expiry=5.00/12.00;
	int numberstep=5;
	double [] []stockprice = new double[5][5];
	double [] []optionprice=new double[5][5];
	double [] []delta=new double [5][5];
	
	public  void aa(){
		
		double timestep=expiry/numberstep;
		double discountfactor=Math.exp(-intrate*timestep);
		double temp1=Math.exp((intrate +Math.pow(volatility,2))*timestep);
		double temp2=0.5*(discountfactor+temp1);
		double u=temp2+Math.sqrt(Math.pow(temp2,2)-1);
		double d=1/u;
		double p=(Math.exp(intrate*timestep)-d)/(u-d);
		
		
		stockprice[0][0]=asset;
		for(int i=1;i<numberstep;i++)
		{
			for(int j=0;j<i;j++)
			{
				stockprice[i][j]=u*stockprice[i-1][j];
				stockprice[i][j+1]=d*stockprice[i-1][j];
			}
		}
		for(int i=0;i<numberstep;i++)
		{
			optionprice[numberstep-1][i]=Payoff(stockprice[numberstep-1][i],strike);
		}
		for(int j=numberstep-2;j>=0;j--)
		{
			for(int i=0;i<=j;i++)
			{
				optionprice[j][i]=(p*optionprice[j+1][i]+(1-p)*optionprice[j+1][i+1])*discountfactor;
			}
		}
		for(int j=numberstep-2;j>=0;j--)
		{
			for(int i=0;i<=j;i++)
			{
				delta[j][i]=(optionprice[j+1][i]-optionprice[j+1][i+1])/((u-d)*stockprice[j][i]);
			}
		}
	}
	
	public static double Payoff(double s, double k) {
		if(s>k)
			return s-k;
		else
			return 0.0;
	}

	
}
