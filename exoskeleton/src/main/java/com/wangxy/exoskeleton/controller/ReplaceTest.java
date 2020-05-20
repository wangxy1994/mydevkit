package com.wangxy.exoskeleton.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;

public class ReplaceTest {
    public static void main(String[] args) throws IOException{
        System.out.println("hello");
        String line = "asynExecute('bfm.basedata.virtualcombi.VirtualCombiUpdate.service?resCode=virtualCombi&opCode=delService',res,succ, null,'1正在删除+++') ;";
//        System.out.println(line.replaceAll(",'(.*)\\+{3,}'",",processing"));
        Set<String> translateLine = getTranslateLine(line);
        for (String string : translateLine) {
			System.out.println(string);
		}
        
//        String path = "G:/同步dev/国际化/virtualcombiBefore/virtualCombiList.jsp";
//        jsoupDeal(path);
        String url = "bizframe.dict.DictSvr.service?resCode=dict&opCode=items&key=K_DZSPBJLX";
        String paramIn = url.substring(url.indexOf("?") + 1, url.length());
        paramIn = paramIn.replaceAll("=","\":\"");
        paramIn = paramIn.replaceAll("&","\",\"");
        paramIn = "{\"" + paramIn +"\"}";
        JSONObject jsonObject = JSONObject.parseObject(paramIn);
        System.out.println(jsonObject.getString("key"));
        
        String options = "field:'busin_typesStr',width:300,title:'业务类别权限',sortable:false,formatter:render";
        options = options.replaceAll("'","");
        options = options.replaceAll("\"","");
        options = options.replaceAll(":","\":\"");
		options = options.replaceAll(",","\",\"");
		options = "{\"" + options +"\"}";
		
		System.out.println(options);
        JSONObject opt = JSONObject.parseObject(options);
        System.out.println(opt.getString("title"));
        
        
        String path = "G:/同步dev/国际化/virtualcombiBefore/virtualCombiList";
//        path = "D:\work\IRM\avengers-pub\src\main\webapp\topjui\jsp\basedata\businright\businRightList";
        System.out.println(path.split("/")[0]);
        

    }
    
    
    /**
	 * 获取固定情况下的中文，field，翻译作为集合
	 * 
	 * @param path
	 * @throws IOException
	 */
	private static void jsoupDeal(String path) throws IOException {
		File input = new File(path);
		Document doc = Jsoup.parse(input, "UTF-8");
		
		Map<String,String> chinese = new HashMap<String,String>();
		
		Elements allTh = doc.getElementsByTag("th");
		for (Element th : allTh) {
			  String options = th.attr("data-options");
			  options = "{"+options+"}";
			  JSONObject jsonObject = JSONObject.parseObject(options);
			  chinese.put(jsonObject.getString("title"), jsonObject.getString("field"));
		}
		/*
		List<String> reultLines = new ArrayList<>();
		List<String> list = FileUtils.readLines(new File(path));
		StringBuilder sb = new StringBuilder();
		for (String ss : list) {
			sb.append(ss + "\n");
			// 根据指定情况替换

		}
		System.out.println(sb.toString());
		String resultPath = path.split("\\.")[0] + "_res_" + ".jsp";
		File resultFile = new File(resultPath);
		FileUtils.writeLines(resultFile, list);
		*/
	}
	private static Set<String> getTranslateLine(String line) {
		Set<String> translateLine = new HashSet<String>();
		String regex = "'[^,]*'";
//		String regex = "['](.*?)[']";
		List<String> matchString = getMatchString(line, regex);
		for (String matchWord : matchString) {
			if (isContainChinese(matchWord)){
				translateLine.add(matchWord);
			}
		}
		return translateLine;
	}
	
	/**
	 * 正则表达式获得匹配内容
	 * @param sourceString
	 * @param regex
	 * @return
	 */
	public static List<String> getMatchString(String sourceString,String regex) {
		List<String> matchString = new ArrayList<>();

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(sourceString);
		if(matcher.find()){
			for(int i=0; i<=matcher.groupCount(); i++){
				matchString.add(matcher.group(i));
			}
		}
		return matchString;
	}
	/**
	 * 判断字符串是否包含中文
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

}
