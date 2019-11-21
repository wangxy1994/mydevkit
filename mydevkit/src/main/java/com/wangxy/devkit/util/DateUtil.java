package com.wangxy.devkit.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/********************************************
 * 文件名称: DateUtil.java
 * 系统名称: 综合理财系统
 * 模块名称:
 * 软件版权:  
 * 功能说明: 
 * 系统版本: 3.0.0.1
 * 开发人员: Administrator
 * 开发时间: 2010-5-1 上午12:37:57
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    	修改人员    	修改说明
 * 			20140613 	gaoxiang  	修改public static String getTime()方法的注释,防止误用
 * 			20140922 	yangxi		增加diffMonth方法用于获取两个日期相隔月份
 * 			20151103    gaoxiang    增加dateTo10和timeTo8入参为int数据的方法
 *          20190320    daijy       增加dateTo6的方法
            20190603    panyt      	getLastDayOfMonth 20190603 修改支持6位
            20190603    zhangsq     新增获取年初年末方法 *********************************************/
public class DateUtil {
 
    public static final String VERSION="@system 资金交易风险管理系统V2.0  @version 2.0.0.3  @lastModiDate  2015-11-03  @describe 增加dateTo10和timeTo8入参为int数据的方法";
	static int[][] day_tbl = { { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 }, { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 } }; // 非闰年/闰年月天数
	
	/**
	 * 日期格式化。 <br>
	 * 7位yyyy/MM 转换为6位yyyyMM
	 * 
	 * @param date
	 *            - 要格式化的日期字符串: 7位 yyyy/MM或yyyy-MM
	 * @return String - 返回格式化后的日期 <br>
	 *         若date长度不为7，即格式不为yyyy/MM形式的日期，则直接返回date。 <br>
	 *         若date为null, 则返回""
	 */
	public static String dateTo6(String date) {
		if (date == null){
			return "";
		}
		if (date.trim().length() != 7) {
			return date;
		}
		return date.substring(0, 4) + date.substring(5, 7);
	}
	
	/**
	 * 日期格式化。 <br>
	 * 10位yyyy/MM/dd 转换为8位yyyyMMdd
	 * 
	 * @param date
	 *            - 要格式化的日期字符串: 10位 yyyy/MM/dd或yyyy-MM-dd
	 * @return String - 返回格式化后的日期 <br>
	 *         若date长度不为10，即格式不为yyyy/MM/dd形式的日期，则直接返回date。 <br>
	 *         若date为null, 则返回""
	 */
	public static String dateTo8(String date) {
		if (date == null)
			return "";
		if (date.trim().length() != 10) {
			return date;
		}
		return date.substring(0, 4) + date.substring(5, 7)
				+ date.substring(8, 10);
	}

	/**
	 * 文件名中的日期转换 <br>
	 * 我文件名中的YYYYMMDD转换成当前的系统日期20050621
	 * 
	 * @param date
	 * @return String
	 */
	public static String formatFileName(String filename) {
		String yyyy = null;
		String mm = null;
		String dd = null;
		String hs_date = null;
		String fmtfilename = null;
		int index = -1;
		hs_date = DateUtil.getDate();
		yyyy = hs_date.substring(0, 4);
		mm = hs_date.substring(4, 6);
		dd = hs_date.substring(6, 8);
		index = filename.indexOf("YYYY");
		if (index != -1) {
			fmtfilename = filename.substring(0, index) + yyyy
					+ filename.substring((index + 4), filename.length());
		}
		filename = fmtfilename;
		index = filename.indexOf("MM");
		if (index != -1) {
			fmtfilename = filename.substring(0, index) + mm
					+ filename.substring((index + 2), filename.length());
		}
		filename = fmtfilename;
		index = filename.indexOf("DD");
		if (index != -1) {
			fmtfilename = filename.substring(0, index) + dd
					+ filename.substring((index + 2), filename.length());
		}
		filename = fmtfilename;
		return filename;

	}

	/**
	 * 日期格式化。 <br>
	 * 8位yyyyMMdd 转换为10位yyyy-MM-dd
	 * 
	 * @param date
	 *            - 要格式化的日期字符串: 8位 yyyyMMdd
	 * @return String - 返回格式化后的日期 <br>
	 *         若date长度不为8，即格式不为yyyyMMdd形式的日期，则直接返回date。 <br>
	 *         若date为null, 则返回""
	 */
	public static String dateTo10(String date) {
		if (date == null)
			return "";
		if (date.trim().length() != 8)
			return date;
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8);
	}
	
	/**
	日期格式化。 <br>
	 * 8位yyyyMMdd 转换为10位yyyy-MM-dd
	 * 
	 * @param date
	 *            - 要格式化的日期字符串: 8位 yyyyMMdd
	 * @return String - 返回格式化后的日期 <br>
	 *         若date长度不为8，即格式不为yyyyMMdd形式的日期，则直接返回date。 <br>
	 *         若date为0, 则返回""
	 */
	public static String dateTo10(int date){
		if (date == 0)
			return "";
		String returnDate = DataUtil.formatString(date);
		if (returnDate.length() != 8)
			return returnDate;
		return returnDate.substring(0, 4) + "-" + returnDate.substring(4, 6) + "-"
				+ returnDate.substring(6, 8);
	}

	/**
	 * 日期格式化。 <br>
	 * 8位yyyyMMdd 转换为10位yyyy/MM/dd <br>
	 * yyyy、MM和dd之间的分隔字符自定义(flag)
	 * 
	 * @param date
	 *            - 要格式化的日期字符串: 8位 yyyyMMdd
	 * @param flag
	 *            - yyyy、MM和dd之间的分隔字符
	 * @return String - 返回格式化后的日期 <br>
	 *         若date长度不为8，即格式不为yyyyMMdd形式的日期，则直接返回date。 <br>
	 *         若date为null, 则返回""
	 */
	public static String dateTo10(String date, char flag) {
		if (date == null)
			return "";
		if (date.trim().length() != 8)
			return date;
		return date.substring(0, 4) + flag + date.substring(4, 6) + flag
				+ date.substring(6, 8);
	}
	
	/**
	 * 日期格式化。 <br>
	 * 8位yyyyMMdd 转换为10位yyyy/MM/dd <br>
	 * yyyy、MM和dd之间的分隔字符自定义(flag)
	 * 
	 * @param date
	 *            - 要格式化的日期字符串: 8位 yyyyMMdd
	 * @param flag
	 *            - yyyy、MM和dd之间的分隔字符
	 * @return String - 返回格式化后的日期 <br>
	 *         若date长度不为8，即格式不为yyyyMMdd形式的日期，则直接返回date。 <br>
	 *         若date为null, 则返回""
	 */
	public static String dateTo10(int date, char flag) {
		if (date == 0)
			return "";
		String returnDate = DataUtil.formatString(date);
		if (returnDate.length() != 8)
			return returnDate;
		return returnDate.substring(0, 4) + flag + returnDate.substring(4, 6) + flag
				+ returnDate.substring(6, 8);
	}

	/**
	 * 时间格式化。 <br>
	 * 8位(HH:mm:ss)或7位(H:mm:ss)的时间转换为6位(HHmmss)或5位(Hmmss) <br>
	 * 时间的分隔字符可以是任意字符，一般为冒号(:)
	 * 
	 * @param time
	 *            - 要格式化的时间字符串:8位(HH:mm:ss)或7位(H:mm:ss)
	 * @return String - 返回格式化后的时间 <br>
	 *         若time长度不为8或7，即格式不为8位(HH:mm:ss)或7位(H:mm:ss)形式的时间，则直接返回date。 <br>
	 *         若time为null, 则返回""
	 */
	public static String timeTo6(String time) {
		int len = time.length();
		if (len < 7 || len > 8)
			return time;
		return time.substring(0, len - 6) + time.substring(len - 5, len - 3)
				+ time.substring(len - 2, len);
	}
	
	/**
	 * 将格式化后的时间转换成整形
	 * @param time
	 * @return
	 */
	public static int timeTo6Int(String time){
		return Integer.parseInt(DateUtil.timeTo6(time),10);
	}

	/**
	 * 时间格式化。 <br>
	 * 6位(HHmmss)或5位(Hmmss)的时间转换为8位(HH:mm:ss)或7位(H:mm:ss)
	 * 
	 * @param date
	 *            - 要格式化的时间字符串: 6位(HHmmss)或5位(Hmmss)
	 * @return String - 返回格式化后的时间 <br>
	 *         若time长度不为5或6，即格式不为6位(HHmmss)或5位(Hmmss)形式的时间，则直接返回date。 <br>
	 *         若time为null, 则返回""
	 */
	public static String timeTo8(String time) {
		String formatTime = time;
		if (DataUtil.isNullStr(formatTime) ) {
			return "";
		}
		if (formatTime.length() < 6) {
			formatTime = DataUtil.fix0BeforeString(formatTime, 6);
		}
		int len = formatTime.length();
		if (len < 5 || len > 6)
			return "";
		return formatTime.substring(0, len - 4) + ":"
				+ formatTime.substring(len - 4, len - 2) + ":"
				+ formatTime.substring(len - 2, len);
	}

	
	/**
	 * 时间格式化。 <br>
	 * 6位(HHmmss)或5位(Hmmss)的时间转换为8位(HH:mm:ss)或7位(H:mm:ss)
	 * 
	 * @param date
	 *            - 要格式化的时间字符串: 6位(HHmmss)或5位(Hmmss)
	 * @return String - 返回格式化后的时间 <br>
	 *         若time长度不为5或6，即格式不为6位(HHmmss)或5位(Hmmss)形式的时间，则直接返回date。 <br>
	 *         若time为null, 则返回""
	 */
	public static String timeTo8(int time){
		return DateUtil.timeTo8(String.valueOf(time));
	}
	
	/**
	 * 时间格式化。 <br>
	 * 6位(HHmmss)或5位(Hmmss)的时间转换为8位(HH:mm:ss)或7位(H:mm:ss) <br>
	 * 时间的分隔字符是自定义的字符(flag)
	 * 
	 * @param date
	 *            - 要格式化的时间字符串: 6位(HHmmss)或5位(Hmmss)
	 * @param flag
	 *            - 时间的分隔字符
	 * @return String - 返回格式化后的时间 <br>
	 *         若time长度不为5或6，即格式不为6位(HHmmss)或5位(Hmmss)形式的时间，则直接返回date。 <br>
	 *         若time为null, 则返回""
	 */
	public static String timeTo8(String time, char flag) {
		String formatTime = time;
		if (formatTime == null) {
			return "";
		}
		if (formatTime.length() < 6) {
			formatTime = DataUtil.fix0BeforeString(formatTime, 6);
		}

		int len = formatTime.length();
		if (len < 5 || len > 6)
			return "";
		return formatTime.substring(0, len - 4) + flag
				+ formatTime.substring(len - 4, len - 2) + flag
				+ formatTime.substring(len - 2, len);
	}
	
	/**
	 * 时间格式化。 <br>
	 * 6位(HHmmss)或5位(Hmmss)的时间转换为8位(HH:mm:ss)或7位(H:mm:ss) <br>
	 * 时间的分隔字符是自定义的字符(flag)
	 * 
	 * @param date
	 *            - 要格式化的时间字符串: 6位(HHmmss)或5位(Hmmss)
	 * @param flag
	 *            - 时间的分隔字符
	 * @return String - 返回格式化后的时间 <br>
	 *         若time长度不为5或6，即格式不为6位(HHmmss)或5位(Hmmss)形式的时间，则直接返回date。 <br>
	 *         若time为null, 则返回""
	 */
	public static String timeTo8(int time, char flag) {
	     return timeTo8(String.valueOf(time),flag);
	}

	/**
	 * 时间格式化。 <br>
	 * 6位(HHmmss)或5位(Hmmss)的时间转换为8位(HH:mm:ss)或7位(H:mm:ss) <br>
	 * 时间的分隔字符是自定义的字符(flag)
	 * 
	 * @param date
	 *            - 要格式化的时间字符串: 6位(HHmmss)或5位(Hmmss)
	 * @param flag
	 *            - 时间的分隔字符
	 * @return String - 返回格式化后的时间 <br>
	 *         若time长度不为5或6，即格式不为6位(HHmmss)或5位(Hmmss)形式的时间，则直接返回date。 <br>
	 *         若time为null, 则返回""
	 */
	public static String timeTo8(String time, String flag) {
		String formatTime = time;
		if (formatTime == null) {
			return "";
		}
		if (formatTime.length() < 6) {
			formatTime = DataUtil.fix0BeforeString(formatTime, 6);
		}

		int len = formatTime.length();
		if (len < 5 || len > 6)
			return "";
		return formatTime.substring(0, len - 4) + flag
				+ formatTime.substring(len - 4, len - 2) + flag
				+ formatTime.substring(len - 2, len);
	}

	/**
	 * 时间格式化。 <br>
	 * 6位(HHmmss)或5位(Hmmss)的时间转换为8位(HH:mm:ss)或7位(H:mm:ss) <br>
	 * 时间的分隔字符是自定义的字符(flag)
	 * 
	 * @param date
	 *            - 要格式化的时间字符串: 6位(HHmmss)或5位(Hmmss)
	 * @param flag
	 *            - 时间的分隔字符
	 * @return String - 返回格式化后的时间 <br>
	 *         若time长度不为5或6，即格式不为6位(HHmmss)或5位(Hmmss)形式的时间，则直接返回date。 <br>
	 *         若time为null, 则返回""
	 */
	public static String timeTo8(int time, String flag) {
	    return timeTo8(String.valueOf(time),flag);	
	}
	
	/**
	 * 获取当前日期或时间。 <br>
	 * 根据不同的格式化字符串获取不同格式的日期或时间 <br>
	 * 具体用法参见类java.text.SimpleDateFormat <br>
	 * 例如: format="yyyy" 则为获取当前年份 <br>
	 * format="MM" 则为获取当前月份 <br>
	 * format="dd" 则为获取当前日期号day <br>
	 * ...
	 * 
	 * @param format
	 *            - 指定获取当前日期或时间格式的字符串
	 * @return String - 返回指定格式的日期或时间字符串 <br>
	 *         若格式字符串非法，则返回"";
	 */
	public static String getDateTime(String format) {
		SimpleDateFormat dataFormat = null;
		try {
			dataFormat = new SimpleDateFormat(format);
		} catch (Exception e) {
			return "";
		}
		Date date = new Date();
		String dateString = dataFormat.format(date);
		return dateString;
	}

	/**
	 * 获取当前日期。 <br>
	 * 获取的日期格式为yyyyMMdd
	 * 
	 * @return String - 返回当前日期
	 */
	public static String getDate() {
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String dateString = dataFormat.format(date);
		return dateString;
	}

	/**
	 * 获取当前日期。 <br>
	 * @return int  - 返回当前日期
	 */
	public static int getSysDate() {
		return Integer.parseInt(DateUtil.getDate());
	}
	
	/**
	 * 获取当前是星期几。 <br>
	 * 0为星期天、1为星期一、以此类推。
	 * 
	 * @return String - 返回当前星期几
	 */
	public static int getWeek() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int posOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		posOfWeek--; // Calendar格式转化成普通格式 0星期天， 1 星期一...
		return posOfWeek;
	}
	
	/**
	 * 获取指定按期的当天的星期几
	 * @param srcDate   日期YYYYMMDD <br>
	 * 0为星期天、1为星期一、以此类推。
	 * @return  - 返回当前星期几
	 */
	public static int getWeek(int srcDate){
		return DateUtil.getWeek(String.valueOf(srcDate));
	}
		
	
	/**
	 * 获取指定按期的当天的星期几
	 * @param srcDate   日期YYYYMMDD <br>
	 * 0为星期天、1为星期一、以此类推。
	 * @return  - 返回当前星期几
	 */
	public static int getWeek(String srcDate){
		int posOfWeek=-1;
		try {
			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
			Date date;
		
			date = simpledateformat.parse(srcDate);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
	   posOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		posOfWeek--; // Calendar格式转化成普通格式 0星期天， 1 星期一...
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return posOfWeek;
		
	}
	
	//20140613  gaoxiang 修改注释,防止误用,只会返回6位时间(HHmmss),不会返回5位时间(Hmmss)
	/**
	 * 获取当前时间。 <br>
	 * 获取的时间的格式为6位(HHmmss)
	 * @return String - 返回当前时间
	 */
	public static String getTime() {
		SimpleDateFormat dataFormat = new SimpleDateFormat("HHmmss");
		Date date = new Date();
		String timeString = dataFormat.format(date);
		return timeString;
	}

	/**
	 * 获取当前时间。 <br>
	 * 获取的时间的格式为6位(HHmmss)或5位(Hmmss)
	 * 
	 * @return String - 返回当前时间
	 */
	public static int getSysTime() {
		return Integer.parseInt(DateUtil.getTime(),10);
	}

	
	/**
	 * 月份相加运算。 <br>
	 * 指定日期(yyyyMM) 加上月份数(月份数正负都可以)，计算出新的日期(yyyyMM) <br>
	 * 例如 指定日期为200307, 加上月数为-8, 则计算得到日期为 200211
	 * 
	 * @param date
	 *            - 指定日期,格式为yyyyMM
	 * @param months
	 *            - 指定相加月数
	 * @return String - 返回计算后的日期
	 */
	public static String addMonth(String date, int months) {
		if (date.length() != 6)
			return "";
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		month += months;
		while (month <= 0) {
			year--;
			month += 12;
		}
		while (month > 12) {
			year++;
			month -= 12;
		}
		String ret = "" + year;
		if (month >= 10)
			ret += month;
		else
			ret += "0" + month;
		return ret;
	}
	  /**
	   * 月份相加运算（慎用，20111031+4月 为20120229）

	   * 等同于getDateOfMonthsLater和getDateOfMonthsBefore累计
	   */
	  public static String addMonth8(String date, int months) {
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			try {
				if(date != null && !date.equals("")){
					c.setTime(sdf.parse(date));
					c.add(Calendar.MONTH, months); // 目前时间加指定月份
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String resultDateStr = sdf.format(c.getTime());
			return resultDateStr;
	  }

	/**
	 * 方法 addMonth 得到与指定日期相差指定月数的日期
	 * 
	 * @param baseDate
	 *            时间字符串，如：2001-07-17 11:00:00
	 * @param monthCount
	 *            向前或向后的月数，向后为正数，向前为负值
	 * @return 相加后的日期字符串
	 */
	public static int addMonthAll(String baseDate, int monthCount) {
		int year = Integer.parseInt(baseDate.substring(0, 4));
		int month = Integer.parseInt(baseDate.substring(4, 6));
		int day = Integer.parseInt(baseDate.substring(6, 8));
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		calendar.add(Calendar.MONTH, monthCount);
		Date _date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(_date);

		return Integer.parseInt(dateString);
	}
	/**
	 * 根据指定日期，间隔月数获取日期
	 * @param date1  源日期
	 * @param months  正数 往后累加月数，负数往前累加
	 * @return
	 */
	public static String calDateByMonth(String date1,int months){
		Calendar cal = Calendar.getInstance();
		DateFormat fmt = new SimpleDateFormat("yyyyMMdd"); 
         Date date;
         int days=0;
		try {
			
			date = fmt.parse(date1);
			cal.setTime(date);
			
			String month=DateUtil.getMonth(cal);
			String year=DateUtil.getYear(cal);
			int base=months>0?1:-1;
			for(int i=1;i<=Math.abs(months);i++){
			    String tempDate= DateUtil.addMonth(String.valueOf(year)+String.valueOf(month), i*base);	
			    int tempYear=Integer.parseInt(tempDate.substring(0,4));
			    int tempMonth=Integer.parseInt(tempDate.substring(4));
			    days+=DateUtil.days(tempYear,tempMonth);
			}
			days=base*days;
		    return DateUtil.calOffDate(date1, days);	
			
		} catch (ParseException e) {
			e.printStackTrace();
			throw  new java.lang.RuntimeException("calDateByMonth fail");
		}
	
	
	}
	
	/**
	 * 月份相加运算。 <br>
	 * 指定日期(yyyyMM) 加上月份数(月份数正负都可以)，计算出新的日期(yyyyMM) <br>
	 * 例如 指定日期为200307, 加上月数为-8, 则计算得到日期为 200211
	 * 
	 * @param date
	 *            - 指定日期,格式为yyyyMM
	 * @param months
	 *            - 指定相加月数
	 * @return int - 返回计算后的日期
	 */
	public static int addMonth(int date, int months) {
		return  Integer.parseInt(DateUtil.addMonth(String.valueOf(date), months),10);
	}

	/**
	 * 日期相加运。 <br>
	 * 指定日期(yyyyMMdd) 加上天数(天数正负都可以)，计算出新的日期(yyyyMMdd) <br>
	 * 例如 指定日期为20030702, 加上月数为-3, 则计算得到日期为 20030629
	 * 
	 * @param date
	 *            - 指定日期,格式为yyyyMMdd
	 * @param days
	 *            - 指定相加天数
	 * @return String - 返回计算后的日期
	 */
	public static String addDate(String date, int days) {
		if (Long.parseLong(date) < 19010101)
			return date;
		String str = date;
		int year = Integer.parseInt(str.substring(0, 4));
		int month = Integer.parseInt(str.substring(4, 6));
		int day = Integer.parseInt(str.substring(6, 8));
		GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
		calendar.add(Calendar.DATE, days);
		long newDate = calendar.get(Calendar.YEAR) * 10000
				+ (calendar.get(Calendar.MONTH) + 1) * 100
				+ calendar.get(Calendar.DAY_OF_MONTH);
		return "" + newDate;
	}

	/**
	 * 日期相加运。 <br>
	 * 指定日期(yyyyMMdd) 加上天数(天数正负都可以)，计算出新的日期(yyyyMMdd) <br>
	 * 例如 指定日期为20030702, 加上月数为-3, 则计算得到日期为 20030629
	 * 
	 * @param date
	 *            - 指定日期,格式为yyyyMMdd
	 * @param days
	 *            - 指定相加天数
	 * @return int - 返回计算后的日期
	 */
	public static int addDate(int date, int days) {
	     return Integer.parseInt(DateUtil.addDate(String.valueOf(date), days),10);	
	}
	
    /**
     * 输入年月日，得到下个月的年月日
          如20080102----> 20080202
            20080131----> 20080229
            20081231----> 20090131	
     * @param date   日期
     * @param months  月数目
     * @return
     */
	public static int getDateOfMonthsLater(int date,int months){
		int year;
	    int month;
	    int day;
	    int monthDays[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	    year = (int)(date / 10000);
	    month = (int)(date / 100) % 100;
	    day = (int)date % 100;
	    year = year + (month + months) / 12;
	    month = (month + months) % 12;
	    if(month == 0)
	    {
	       month = 12;
	       year --;
	    }
	    if(month == 2)
	    {
	        if((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))
	        {
	            if(day > 29)
	            {
	                day = 29;
	            }
	        }
	        else
	        {
	            if(day > 28)
	            {
	                day = 28;
	            }
	        }
	    }
	    else
	    {
	       if(day > monthDays[month - 1])
	       {
	           day = monthDays[month - 1];
	       }
	    }
	    return (year * 100 + month) * 100 + day;
	}

    /**
     * 输入年月日，得到下个月的年月日
          如20080102----> 20080202
            20080131----> 20080229
            20081231----> 20090131	
     * @param date   日期
     * @param months  月数目
     * @return
     */
	public static String getDateOfMonthsLater(String date,int months){
		 if(DataUtil.isNullStr(date)||!DataUtil.isDigit(date)||date.length()!=8){
			 throw new java.lang.IllegalArgumentException("illeagal argument date ");
		 }
		 
	     return String.valueOf(getDateOfMonthsLater(Integer.parseInt(date),months));	
	}

	/**
	 * 输入年月日，得到下个月的年月日
           如20080102----> 20080102
          
	 * @param date
	 * @param months
	 * @return
	 */
	public static int getDateOfMonthsBefore(int date,int months){
		
		if(months <=0){
			throw new java.lang.IllegalArgumentException("illegal argument months ");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, date/10000);
		calendar.set(Calendar.MONTH, date%10000/100  - 1);
		calendar.set(Calendar.DAY_OF_MONTH, date%100);
		calendar.add(Calendar.MONTH, -1*months);
		return calendar.get(Calendar.YEAR)*10000+(calendar.get(Calendar.MONTH)+1)*100+calendar.get(Calendar.DAY_OF_MONTH);
		
	}
	
	/**
	 * 计算日期
	 * @param date
	 * @param months
	 * @return
	 */
	public static String getDateOfMonthsBefore(String date,int months){
		if(DataUtil.isNullStr(date)||!DataUtil.isDigit(date)||date.length()!=8){
			 throw new java.lang.IllegalArgumentException("illeagal argument date ");
		 }
		return String.valueOf(getDateOfMonthsBefore(Integer.parseInt(date),months));
	}
	
	/**
	 * 时间相加运算。 <br>
	 * 指定时间(HHmmss或Hmmss) 加上秒数(秒数正负都可以)，计算出新的时间 <br>
	 * 例如 指定时间为203001, 加上秒数为-60, 则计算得到日期为 202901
	 * 
	 * @param time
	 *            - 指定时间,格式为HHmmss或Hmmss
	 * @param seconds
	 *            - 指定相加秒数
	 * @return String - 返回计算后的时间
	 */
	public static String addTime(String time, int seconds) {
		int len = time.length();
		if (len < 5 || len > 6)
			return "";
		int hh = Integer.parseInt(time.substring(0, len - 4));
		int mm = Integer.parseInt(time.substring(len - 4, len - 2));
		int ss = Integer.parseInt(time.substring(len - 2, len));

		int s = seconds % 60;
		int m = seconds / 60;

		mm += m;
		ss += s;

		// 处理秒
		if (ss < 0) {
			mm--;
			ss += 60;
		}
		if (ss >= 60) {
			mm++;
			ss -= 60;
		}

		// 处理分
		hh += mm / 60;
		mm = mm % 60;
		if (mm >= 60) {
			hh++;
			mm -= 60;
		}
		if (mm < 0) {
			hh--;
			mm += 60;
		}

		// 处理小时
		hh = hh % 24;
		if (hh < 0) {
			hh += 24;
		}

		// 组返回字符串
		String newTime = "" + hh;
		if (mm < 10)
			newTime += "0" + mm;
		else
			newTime += mm;
		if (ss < 10)
			newTime += "0" + ss;
		else
			newTime += ss;
		return newTime;
	}

	/**
	 * 时间相加运算。 <br>
	 * 指定时间(HHmmss或Hmmss) 加上秒数(秒数正负都可以)，计算出新的时间 <br>
	 * 例如 指定时间为203001, 加上秒数为-60, 则计算得到日期为 202901
	 * 
	 * @param time
	 *            - 指定时间,格式为HHmmss或Hmmss
	 * @param seconds
	 *            - 指定相加秒数
	 * @return int - 返回计算后的时间
	 */
	public static int addTime(int time, int seconds) {
	    return Integer.parseInt(DateUtil.addTime(String.valueOf(time), seconds), 10);
	    
	}	
	/**
	 * 计算两个日期相隔天数。 <br>
	 * 计算结果统一成正数
	 * 
	 * @param date1
	 *            - 指定日期(yyyyMMdd)的其中一个
	 * @param date2
	 *            - 指定日期(yyyyMMdd)的另外一个
	 * @return int - 返回计算后的天数 失败返回-1
	 */
	public static int diffDate(String date1, String date2) {
		try {

			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
			Date d1 = simpledateformat.parse(date1);
			Date d2 = simpledateformat.parse(date2);

			long tmp = d1.getTime() - d2.getTime();
			if (tmp < 0)
				tmp = -tmp;

			int diff = (int) (tmp / (24 * 60 * 60 * 1000));

			return diff;
		} catch (Exception e) {
			// e.printStackTrace();
			return -1;
		}

	}
  	/**
   	 * 计算两个日期时间相隔秒数。
   	 * <br>第一个减去第二个，允许返回负数
   	 * @param date1 - 指定日期(yyyyMMddhhmmss)的其中一个
   	 * @param date2 - 指定日期(yyyyMMddhhmmss)的另外一个
   	 * @return int - 返回计算后的天数  失败返回-1
   	 */
  	public static long diffDateTime(String date1, String date2){
    	try {
			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddhhmmss");
      		Date d1 = simpledateformat.parse(date1);
      		Date d2 = simpledateformat.parse(date2);

     		long diff = d1.getTime() - d2.getTime();
      		return diff;
    	} catch(Exception e) {
    		return -1;
    	}
  	}

	/**
	 * 计算两个日期相隔天数。 <br>
	 * 计算结果统一成正数
	 * 
	 * @param date1
	 *            - 指定日期(yyyyMMdd)的其中一个
	 * @param date2
	 *            - 指定日期(yyyyMMdd)的另外一个
	 * @return int - 返回计算后的天数 失败返回-1
	 */
	public static int diffDate(int date1, int date2) {
	    return DateUtil.diffDate(String.valueOf(date1), String.valueOf(date2));
	}
	
	/**
	 * 计算两个日期相隔约束。 <br>
	 * 计算结果统一成正数
	 * 
	 * @param date1
	 *            - 指定日期(yyyyMMdd)的其中一个
	 * @param date2
	 *            - 指定日期(yyyyMMdd)的另外一个
	 * @return int - 返回计算后的月数，失败返回-1
	 * 		如：起始日期Y1M1D1   结束日期Y2M2D2
	 *				1、计算Y2M2和Y1M1的间隔月份M0
	 *				2、如果 D2>D1  M=M0+1,否则M=M0
	 */
	public static int diffMonth(int date1, int date2) {
		int diff = 0;
		try {
			if(date2 < date1) {
				int tmp = date2;
				date2 = date1;
				date1 = tmp;
			}
			diff = getYear(date1+"")*12+getMonth(date1+"")-getYear(date2+"")*12-getMonth(date2+"");
			if(diff < 0) {
				diff = -diff;
			}
			if(getDay(date2+"") > getDay(date1+"")) {
				diff = diff+1;
			}
		} catch (ParseException e) {
			return -1;
		}
	    return diff;
	}
	
	/**
	 * 获取指定天数后的日期
	 * 
	 * @param date1
	 *            当前日期
	 * @param dayOffSet
	 *            指定天数
	 * @return
	 */
	public static String calOffDate(String date1, int dayOffSet) {

		try {

			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
			Date d1 = simpledateformat.parse(date1);
			long tmp = d1.getTime() + 24l * 60l * 60l * 1000l * dayOffSet;

			return simpledateformat.format(new Date(tmp));

		} catch (Exception e) {
		    throw new java.lang.RuntimeException(e);
		}
		
	}
	/**
	 * 获取指定天数后的日期
	 * 
	 * @param date1
	 *            当前日期
	 * @param dayOffSet
	 *            指定天数
	 * @return
	 */
	public static int calOffDate(int date1, int dayOffSet) {
        return Integer.parseInt(DateUtil.calOffDate(String.valueOf(date1), dayOffSet));
	}

	/**
	 * 获取一个月的最后一天。 <br>
	 * 例如20030111,即一月份的最后一天是20030131
	 * 
	 * @param date
	 *            - 指定日期(yyyyMMdd)
	 * @return String - 返回计算后的日期
	 */
	public static String getLastDayOfMonth(String date) {
	    //pyt 20190603 修改支持6位
		if (date.length() == 8||date.length() ==6){
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(4, 6));
            int day = 0;
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                    || month == 10 || month == 12) {
                day = 31;
            } else if (month == 2) {
                if (((year % 4) == 0) && ((year % 100) != 0)) {
                    day = 29;
                } else {
                    day = 28;
                }
            } else {
                day = 30;
            }

            String newDate = "" + year;
            if (month < 10) {
                newDate += "0" + month + day;
            } else {
                newDate += "" + month + day;
            }
            return newDate;
        }else {
		    return "";
        }

	}

	/**
	 * 获取一个月的最后一天。 <br>
	 * 例如20030111,即一月份的最后一天是20030131
	 * 
	 * @param date
	 *            - 指定日期(yyyyMMdd)
	 * @return int - 返回计算后的日期
	 */
	public static int getLastDayOfMonth(int date) {
	  return   Integer.parseInt(DateUtil.getLastDayOfMonth(String.valueOf(date)));
	}
	
	/**************************** 时间日历操作 **************************************/

	/**
	 * 根据输入长度格式化时间
	 * 
	 * @param DateString
	 *            时间字符串
	 * @return 格式化时间串
	 */

	public static String FormatDate(String DateString) {
		String ReturnDate = "";
		if (DateString != null) {
			DateString = DateString.trim();
			int len = DateString.length();
			switch (len) {
			case 4:
				ReturnDate = DateString;
				break;
			case 6:
				ReturnDate = DateString.substring(0, 2) + ":"
						+ DateString.substring(2, 4) + ":"
						+ DateString.substring(4, 6);
				break;
			case 8:
				ReturnDate = DateString.substring(0, 4) + "/"
						+ DateString.substring(4, 6) + "/"
						+ DateString.substring(6, 8);
				break;
			case 14:
				ReturnDate = DateString.substring(0, 4) + "/"
						+ DateString.substring(4, 6) + "/"
						+ DateString.substring(6, 8) + " "
						+ DateString.substring(8, 10) + ":"
						+ DateString.substring(10, 12) + ":"
						+ DateString.substring(12, 14);
				break;
			case 15:
				ReturnDate = DateString.substring(0, 4) + "/"
						+ DateString.substring(4, 6) + "/"
						+ DateString.substring(6, 8) + " "
						+ DateString.substring(9, 11) + ":"
						+ DateString.substring(11, 13) + ":"
						+ DateString.substring(13, 15);
				break;
			default:
				ReturnDate = "日期格式有误";
				break;
			}
		} else {
			ReturnDate = "日期为空!";
		}
		return ReturnDate;
	}

	/**
	 * 返回日历的年字符串
	 * 
	 * @param cal
	 *            日历
	 * @return 日历的年字符串
	 */
	public static String getYear(Calendar cal) {
		return String.valueOf(cal.get(Calendar.YEAR));
	}

	/**
	 * 返回日历的月字符串(两位)
	 * 
	 * @param cal
	 *            日历
	 * @return 返回日历的月字符串(两位)
	 */
	public static String getMonth(Calendar cal) {
		return DataUtil.fix0BeforeString(String
				.valueOf(cal.get(Calendar.MONTH) + 1), 2);
	}

	/**
	 * 返回日历的日字符串(两位)
	 * 
	 * @param cal
	 *            日历
	 * @return 返回日历的日字符串(两位)
	 */
	public static String getDay(Calendar cal) {
		return DataUtil.fix0BeforeString(String.valueOf(cal
				.get(Calendar.DAY_OF_MONTH)), 2);
	}

	/**
	 * 返回日历的时字符串(两位)
	 * 
	 * @param cal
	 *            日历
	 * @return 返回日历的时字符串(两位)
	 */
	public static String getHour(Calendar cal) {
		return DataUtil.fix0BeforeString(String.valueOf(cal
				.get(Calendar.HOUR_OF_DAY)), 2);
	}

	/**
	 * 返回日历的分字符串(两位)
	 * 
	 * @param cal
	 *            日历
	 * @return 返回日历的分字符串(两位)
	 */
	public static String getMinute(Calendar cal) {
		return DataUtil.fix0BeforeString(String.valueOf(cal
				.get(Calendar.MINUTE)), 2);
	}

	/**
	 * 返回日历的秒字符串(两位)
	 * 
	 * @param cal
	 *            日历
	 * @return 返回日历的秒字符串(两位)
	 */
	public static String getSecond(Calendar cal) {
		return DataUtil.fix0BeforeString(String.valueOf(cal
				.get(Calendar.SECOND)), 2);
	}

	/**
	 * 返回日历的日期字符串（格式："yyyy-mm-dd"）
	 * 
	 * @param cal
	 *            日历
	 * @return 返回日历的日期字符串（格式："yyyy-mm-dd"）
	 */
	public static String getDateStr(Calendar cal) {
		return getYear(cal) + "-" + getMonth(cal) + "-" + getDay(cal);
	}

	/**
	 * 返回日历的日期字符串（格式："yyyymmdd"）
	 * 
	 * @param cal
	 *            日历
	 * @return 返回日历的日期字符串（格式："yyyymmdd"）
	 */
	public static String getNumDateStr(Calendar cal) {
		return getYear(cal) + getMonth(cal) + getDay(cal);
	}

	/**
	 * 返回日历的时间字符串（格式："hh:ss:mm"）
	 * 
	 * @param cal
	 *            日历
	 * @return 返回日历的时间字符串（格式："hh:ss:mm"）
	 */
	public static String getTimeStr(Calendar cal) {
		return getHour(cal) + ":" + getMinute(cal) + ":" + getSecond(cal);
	}

	/**
	 * 返回日历的时间字符串（格式："hhssmm"）
	 * 
	 * @param cal
	 *            日历
	 * @return 返回日历的时间字符串（格式："hhssmm"）
	 */
	public static String getNumTimeStr(Calendar cal) {
		return getHour(cal) + getMinute(cal) + getSecond(cal);
	}

	/**
	 * 返回日历的日期时间字符串（格式："yyyy-mm-dd hh:ss:mm"）
	 * 
	 * @param cal
	 *            日历
	 * @return 返回日历的日期时间字符串（格式："yyyy-mm-dd hh:ss:mm"）
	 */
	public static String getDate(Calendar cal) {
		return getDateStr(cal) + " " + getTimeStr(cal);
	}

	/**
	 * 返回日历的日期时间字符串（格式："yyyymmdd hhssmm"）
	 * 
	 * @param cal
	 *            日历
	 * @return 返回日历的日期时间字符串（格式："yyyymmdd hhssmm"）
	 */
	public static String getNumDate(Calendar cal) {
		return getNumDateStr(cal) + " " + getNumTimeStr(cal);
	}

	/**
	 * 取当前日期时间的字符串，格式为"yyyy-mm-dd hh:ss:mm"
	 * 
	 * @return 返回当前日期时间的字符串，格式为"yyyy-mm-dd hh:ss:mm"
	 */
	public static String getNow() {
		Calendar now = Calendar.getInstance();
		return getDateStr(now) + " " + getTimeStr(now);
	}

	/**
	 * 取当前日期时间的字符串，格式为"yyyymmdd hhssmm"
	 * 
	 * @return 返回当前日期时间的字符串，格式为"yyyymmdd hhssmm"
	 */
	public static String getNumNow() {
		Calendar now = Calendar.getInstance();
		return getNumDateStr(now) + " " + getNumTimeStr(now);
	}

	/**
	 * 取当前日期的字符串，格式为"yyyy-mm-dd"
	 * 
	 * @return 返回当前日期的字符串，格式为"yyyy-mm-dd"
	 */
	public static String getNowDate() {
		Calendar now = Calendar.getInstance();
		return getDateStr(now);
	}

	/**
	 * 取当前日期的字符串，格式为"yyyymmdd"
	 * 
	 * @return 返回当前日期的字符串，格式为"yyyymmdd"
	 */
	public static String getNumNowDate() {
		Calendar now = Calendar.getInstance();
		return getNumDateStr(now);
	}

	/**
	 * 取当前时间的字符串，格式为"hh:ss:mm"
	 * 
	 * @return 返回当前时间的字符串，格式为"hh:ss:mm"
	 */
	public static String getNowTime() {
		Calendar now = Calendar.getInstance();
		return getTimeStr(now);
	}

	/**
	 * 取当前时间的字符串，格式为"hhssmm"
	 * 
	 * @return 返回当前时间的字符串，格式为"hhssmm"
	 */
	public static String getNumNowTime() {
		Calendar now = Calendar.getInstance();
		return getNumTimeStr(now);
	}

	/**
	 * 比较两个日期前后
	 * 
	 * @param todaystring
	 *            String 时间串1
	 * @param compareString
	 *            String 时间串2
	 * @return 时间1 早于 时间2 返回true
	 */
	public static boolean beforeDate(String todaystring, String compareString) {
		try {
			SimpleDateFormat simpledateformat = new SimpleDateFormat(
					"yyyyMMdd hhmmss");
			Date d1 = simpledateformat.parse(todaystring);
			Date d2 = simpledateformat.parse(compareString);
			return d1.before(d2);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 比较两个日期前后
	 * 
	 * @param todaystring
	 *            String 时间串1
	 * @param compareString
	 *            String 时间串2
	 * @return 时间1 晚于 时间2 返回true
	 */

	public static boolean afterDate(String todaystring, String compareString) {
		try {
			SimpleDateFormat simpledateformat = new SimpleDateFormat(
					"yyyyMMdd hhmmss");
			Date d1 = simpledateformat.parse(todaystring);
			Date d2 = simpledateformat.parse(compareString);
			return d1.after(d2);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断时间是否在后两个时间之间
	 * 
	 * @param todaystring
	 *            String 时间串1
	 * @param startdate
	 *            String 时间串2
	 * @param enddate
	 *            String 时间串3
	 * @return boolean
	 */
	public static boolean betweenDate(String todaystring, String startdate,
			String enddate) {
		return (beforeDate(todaystring, enddate) && afterDate(todaystring,
				startdate));
	}

	/**
	 * 根据年份和月份判断该月有几天
	 * 
	 * @param year
	 *            - 年份
	 * @param month
	 *            - 月份
	 * @return int
	 */
	public static int days(int year, int month) {
		int total = 30;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			total = 31;
			break;
		case 2:
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
				total = 29;
			else
				total = 28;
			break;
		default:
			break;
		}
		return total;
	}

	/**
	 * 根据年份和月份判断该月的第一天是星期几
	 * 
	 * @param year
	 *            - 年份
	 * @param month
	 *            - 月份
	 * @return int
	 */
	public static int firstDayOfWeek(int year, int month) {
		Date firstDate = new Date(year - 1900, month - 1, 1);
		return firstDate.getDay();
	}

	/**
	 * 根据年份和月份判断该月的最后一天是星期几
	 * 
	 * @param year
	 *            - 年份
	 * @param month
	 *            - 月份
	 * @return int
	 */
	public static int lastDayOfWeek(int year, int month) {
		Date lastDate = new Date(year - 1900, month - 1, days(year, month));
		return lastDate.getDay();
	}
	
	
    
	/**
     * 	判断指定日期是否合法
     * @param srcDate 日期  格式YYYYMMDD
     * @return
     */
	public static boolean isDate(String srcDate){
		if(DataUtil.isNullStr(srcDate) || srcDate.length()!=8 || !DataUtil.isInteger(srcDate) ){
			return false;
		}
		int year =Integer.parseInt(srcDate.substring(0,4),10);
		int month=Integer.parseInt(srcDate.substring(4,6),10);
		int day=Integer.parseInt(srcDate.substring(6), 10);
	    
		if(month<1 || month>12){
	    	return false;
	    }
	    
		if(day>DateUtil.days(year, month) || day<1){
	    	return false;
	    }
		
		return true;	
	}
	
	/**
	 * 判断指定日期是否合法
	 * @param srcDate  日期  格式YYYYMMDD
	 * @return
	 */
	public static boolean isDate(int srcDate){
         return DateUtil.isDate(String.valueOf(srcDate));		
	}
    
	/**
     * 	计算两个时间的时间差
     * @param beginTime  起始时间  HHmmss
     * @param endTime  结束时间  HHmmss
     * @return
     */
	public static int diffTime(int beginTime,int endTime){
		 SimpleDateFormat dfs = new SimpleDateFormat("HHmmss");
		String time1Str=String.valueOf(beginTime);
		String time2Str=String.valueOf(endTime);
		time1Str=time1Str.length()<6?DataUtil.fix0BeforeString(time1Str, 6):time1Str;
		time2Str=time2Str.length()<6?DataUtil.fix0BeforeString(time2Str, 6):time2Str;
		int between = 0;
		 try {
			 Date begin=dfs.parse(time1Str);
			 Date end = dfs.parse(time2Str);
			 between=(int)((end.getTime()-begin.getTime())/1000);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("cal time diff error");
		}
		 return between;
	}
	
	/**
	 * 取两个日期间隔秒数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getTimeSecond(Date startDate,Date endDate){
		long seconds = 0;
		try {
			seconds = (endDate.getTime() - startDate.getTime())/1000;
		} catch (Exception e) {
		}
		return seconds;
	}
	/**
	 * 返回年
	 * @param date
	 * @return 年
	 * @throws ParseException
	 */
	public static int getYear(String date) throws ParseException {
		return (int)(Integer.parseInt(date) / 10000);
	}

	/**
	 * 返回月
	 * @param date
	 * @return 月
	 * @throws ParseException
	 */
	public static int getMonth(String date) throws ParseException {
		return (int)(Integer.parseInt(date) / 100) % 100;
	}
	/**
	 * 返回日
	 * @param date
	 * @return 日
	 * @throws ParseException
	 */
	public static int getDay(String date) throws ParseException {
		return (int)Integer.parseInt(date) % 100;
	}
	/**
	 * 判断是否为闰年
	 * @param year
	 * @return boolean
	 */
	public static boolean isLeapYear(int year) {
        if((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)){
        	return true;
		}else{
			return false;
		}
	}

	/**
	 * 判断日期是否是月末
	 * @author daijy
	 * @since 20121112
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static boolean isEndOfMonth(String date) throws ParseException{
		int year = 0; // 计息日的年份
		int month = 0; // 计息日的月份
		int day = 0; // 计息日的日子

	    year = (int)(Integer.parseInt(date) / 10000);
	    month = (int)(Integer.parseInt(date) / 100) % 100;
	    day = (int)Integer.parseInt(date) % 100;
		
		int leap = 0;
        if((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))
        {
        	leap = 1;
		}
		if(day==day_tbl[leap][month]){
			return true;
		}
		return false;
	}
	/**
	 * 将日期转换为Calendar,方便运算
	 * @param date YYYYMMDD
	 * @return
	 */
	public static Calendar parseCalendar(String date){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateF = new SimpleDateFormat("yyyyMMdd");
		try{
			cal.setTime(dateF.parse(date));
		}catch(ParseException e){
			throw new RuntimeException(e);
		}
		return cal;
	}

	public static String getLastUpTimeForInstrction(String date){
		char LastUpTime [] = date.toCharArray();
		StringBuilder sbBuilder = new StringBuilder();
		for(int i=0;i<LastUpTime.length;i++){
			sbBuilder.append(LastUpTime[i]);
			if(i == 3 || i==5){
				sbBuilder.append("-");
			}
			if(i == 7){
				sbBuilder.append(" ");
			}
			if(i ==9 || i == 11){
				sbBuilder.append(":");
			}
		}
		return sbBuilder.toString();
	}
	
	
	//为了能让LastUpTime插入到数据库的临时方法
	public static String getLastUpTime(String IsoDateTime){
		char LastUpTime [] = IsoDateTime.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<LastUpTime.length - 4;i++){
			if(LastUpTime[i] >= '0' && LastUpTime[i] <= '9' ){
				sb.append(LastUpTime[i]);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取一年的最后一天
	 * @param date 日期
	 * @return 年末
	 */
	public static int getLastDayOfYear(int date){
		String dateStr = String.valueOf(date);
		if (dateStr==null || dateStr.length() != 8){
			return 0;
		}
		int year = Integer.parseInt(dateStr.substring(0, 4));
		return year*10000+1231;
	}
	
	/**
	 * 获取一年的第一天
	 * @param date 日期
	 * @return 年初
	 */
	public static int getFirstDayOfYear(int date){
		String dateStr = String.valueOf(date);
		if (dateStr==null || dateStr.length() != 8){
			return 0;
		}
		int year = Integer.parseInt(dateStr.substring(0, 4));
		return year*10000+101;
	}
}
