package com.wangxy.exoskeleton.api.tushare;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.wangxy.exoskeleton.api.tushare.vo.Data;

public class TushareApiUtil {

	 /**
    *
    * @param clazz 所要封装的javaBean
    * @param rs 记录集
    * @return ArrayList 数组里边装有 多个javaBean
    * @throws Exception
    * @说明：利用反射机制从ResultSet自动绑定到JavaBean；根据记录集自动调用javaBean里边的对应方法。如果javaBean与数据库字段类型不匹配，按String类型封装
    */
	public static <T> List<T> getList(Class<T> clazz, Data data) {

		Field field = null;
		List<T> lists = new ArrayList<T>();

		// 取得类里边的所有方法
		try {

			// 取得ResultSet列名
			List<String> fields = data.getFields();
			// 获取记录集中的列数
			int counts = fields.size();
			// 定义counts个String 变量
			String[] columnNames = new String[counts];
			// 给每个变量赋值
			for (int i = 0; i < counts; i++) {
				columnNames[i] = fields.get(i);
			}

			// 变量ResultSet
			for (List<String> item : data.getItems()) {

				T t = clazz.newInstance();
				// 反射, 从ResultSet绑定到JavaBean
				e: for (int i = 0; i < counts; i++) {
					// 根据 rs 列名 ，组装javaBean里边的其中一个set方法，object 就是数据库第一行第一列的数据了
					Object value = null;
					Class<?> dbType = null;

					// try的意义是在于 获取对象为空时
					try {
						value = item.get(i);
						// 这里是获取数据库字段的类型 判断 value是否为空 null的话跳过
						dbType = value.getClass();
					} catch (Exception e) {
						continue e;

					}
					// 设置参数类型，此类型应该跟javaBean 里边的类型一样，而不是取数据库里边的类型

					field = clazz.getDeclaredField(columnNames[i]);
					Class<?> beanType = field.getType();

					// 如果发生从数据库获取到得类型跟javaBean类型不同，按String类型取吧
					//20200817	wangxy	现在应该不存在这种情况
					if (beanType != dbType) {
						value = item.get(i);
					}
					
					String setMethodName = "set" + firstUpperCase(columnNames[i]);
					// 第一个参数是传进去的方法名称，第二个参数是 传进去的类型；
					Method m = t.getClass().getMethod(setMethodName, beanType);
					// 第二个参数是传给set方法数据；如果是get方法可以不写
					m.invoke(t, value);
				}
				lists.add(t);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return lists;
	}
    
   //首写字母变大写
     public static String firstUpperCase(String old){
          
         return old.substring(0, 1).toUpperCase()+old.substring(1);
     } 

	
    
}
