package com.wangxy.exoskeleton;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangxy.exoskeleton.entity.TranslateResult;

public class ReplaceTest {
    public static void main(String[] args) throws IOException{
    	System.out.println("1".compareTo("2"));
    	PrdFeeBalance pb1 = new PrdFeeBalance();
    	pb1.setFeeCalMethod("1");
    	PrdFeeBalance pb2 = new PrdFeeBalance();
    	pb2.setFeeCalMethod("2");
    	List<PrdFeeBalance> prdFeeBalances = new ArrayList<PrdFeeBalance>();
    	prdFeeBalances.add(pb2);
    	prdFeeBalances.add(pb1);
    	for (PrdFeeBalance prdFeeBalance : prdFeeBalances) {
    		System.out.println(prdFeeBalance.getFeeCalMethod());
		}
    	Collections.sort(prdFeeBalances, new Comparator<PrdFeeBalance>() {
	            @Override
	            public int compare(PrdFeeBalance o1, PrdFeeBalance o2) {
	                //升序
	                return o1.getFeeCalMethod().compareTo(o2.getFeeCalMethod());
	            }
	    });
    	for (PrdFeeBalance prdFeeBalance : prdFeeBalances) {
    		System.out.println(prdFeeBalance.getFeeCalMethod());
		}
    	
    	
    	LocalDateTime dateTime = LocalDateTime.now();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");  
    	System.out.println(dateTime.format(formatter)); 
    	
    	String md5Time = DigestUtils.md5DigestAsHex(dateTime.format(formatter).getBytes());
    	String token = "7f8faf8e1dff64acc5095c4d64b6f34f";
    	String sign = DigestUtils.md5DigestAsHex((token+md5Time).getBytes());
    	System.out.println("sign="+sign);
    	
    	System.out.println(1/0);
        System.out.println("hello");
        String line = "asynExecute('bfm.basedata.virtualcombi.VirtualCombiUpdate.service?resCode=virtualCombi&opCode=delService',res,succ, null,'1正在删除+++') ;";
//        System.out.println(line.replaceAll(",'(.*)\\+{3,}'",",processing"));
        Set<String> translateLine = getTranslateLine(line);
        for (String string : translateLine) {
			System.out.println(string);
			System.out.println(string.substring(1,string.length()-1));
			System.out.println("\"8976\"".substring(1,"\"8976\"".length()-1));
		}
        
        String resultLine = "'需求日期'";
        String tempRegex = "[\"|']"+"需求日期"+"[\"|']";
		String labelKey = "date";
		List<String> matchString1 = getMatchString(resultLine, tempRegex);
		for (String matchWord : matchString1) {
			resultLine = resultLine.replace(matchWord, matchWord.substring(0, 1)+labelKey+matchWord.substring(matchWord.length()-1));
			System.out.println("resultLine=="+resultLine);
		}
        
        
        Map<String, Object> enJsMap = new HashMap<String, Object>();
        enJsMap.put("msgInfo1", "There is no information configured for this currency pair");
        enJsMap.put("msgInfo2", "non-existent");
        JSONObject enjson = new JSONObject(enJsMap);
        System.out.println(enjson.toJSONString());
        System.out.println(enjson.toJSONString(enjson, true));
        String enJsPath = "D:\\work\\IRM\\avengers-fx\\src\\main\\webapp\\topjui\\jsp\\fx\\biz\\metaltrade\\metaltrans\\js\\test.en.js";
		File enJs = new File(enJsPath );
        FileUtils.write(enJs, "testMsg="+enjson.toJSONString(enjson, true));
        
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
        
        String line1 = "editable:true,required:false,validType:['length[0,64]','alphaDash']";
        String regex = "\\[.*\\]";
//		String regex = "['](.*?)[']";
        List<String> matchString = getMatchString(line1, regex);
		for (String matchWord : matchString) {
			System.out.println(matchWord);
		}
		
		String line2 = "width:200,readonly:true,readonly:true,required:true,value:top.currUserName,buttonText:'',buttonIcon:'fa fa-search', buttonParams:{'text':'user_name','value':'user_id','calBackFun':null}, onClickButton:juipub.showUserSelector,buttonAlign:'right'";
        String regex2 = "\\{(.*)\\}";
//		String regex = "['](.*?)[']";
        List<String> matchString2 = getMatchString(line2, regex2);
		for (String matchWord : matchString2) {
			System.out.println(matchWord);
			System.out.println(line2.replace(matchWord, "temp"));
		}
		
//		path = "D:\\work\\IRM\\avengers-fx\\src\\main\\webapp\\topjui\\jsp\\fx\\biz\\tradeconfirm\\foreignExchangeSpotDetail.jsp";
		path = "D:\\work\\IRM\\avengers-fx\\src\\main\\webapp\\topjui\\jsp\\fx\\biz\\metaltrade\\metaltrans";
		String pageId = null;
        String directoryPath = null;
        File file = new File(path);
        File[] tempList = new File[1];
        if (file.isDirectory()) {
        	tempList = file.listFiles();
        	directoryPath = path;
        	String[] split = directoryPath.split("\\\\");
        	pageId = split[split.length-1];
		}else {
			tempList[0]= file;
			directoryPath = file.getParent();
			String[] pathPart = path.split("\\.");
			//注意要替换转义字符\得用\\\\
			String[] folderAndFile = pathPart[0].split("\\\\");
			//文件名
			pageId = folderAndFile[folderAndFile.length - 1];
		}
		String dictSqlPath = directoryPath+"/" + pageId+"_dictSql.sql";
        String pageSqlPath = directoryPath+"/" + pageId+"_pageSql.sql";
        System.out.println("dictSqlPath=="+dictSqlPath);
        System.out.println("pageSqlPath=="+pageSqlPath);
        
        
        String oriOptions = "width:200,readonly:true,readonly:true,required:true,value:top.currUserName,buttonText:'',buttonIcon:'fa fa-search', buttonParams:{'text':'user_name','value':'user_id','calBackFun':null}, onClickButton:juipub.showUserSelector,buttonAlign:'right'";
        oriOptions = oriOptions.replaceAll("\\{(.*)\\}", "temp");
        System.out.println("afterReplace1=="+oriOptions);
		oriOptions = toStrictJson(oriOptions);
		System.out.println("afterReplace2=="+oriOptions);
		oriOptions = oriOptions.replaceAll("\\[(.*)\\]", "temp");
		System.out.println("afterReplace3=="+oriOptions);
		JSONObject jsonObject1 = null;
		try {
			jsonObject1 = JSONObject.parseObject(oriOptions);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		options = options.replaceAll("\\n", "");
		
		
		String cnJsContent = "";
		JSONObject cnjsJsonObj = JSON.parseObject(cnJsContent.substring(cnJsContent.indexOf("{")));
		
    }
    
    public static String toStrictJson(String options) {
		String regex = "\\[.*\\]";
		options = options.replaceAll(regex,"validType");
		
		options = options.replaceAll("'","");
		options = options.replaceAll("\"","");
		options = options.replaceAll(":","\":\"");
		options = options.replaceAll(",","\",\"");
		options = "{\"" + options +"\"}";
		return options;
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
		String regex = "[\"|'][^,]*?[\"|']";
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
		while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
			matchString.add(matcher.group());//获取当前匹配的值
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
