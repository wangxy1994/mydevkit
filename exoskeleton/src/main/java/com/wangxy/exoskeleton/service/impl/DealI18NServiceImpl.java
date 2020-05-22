package com.wangxy.exoskeleton.service.impl;

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

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.wangxy.exoskeleton.api.BaiduTranslateUtil;
import com.wangxy.exoskeleton.entity.DictItem;
import com.wangxy.exoskeleton.entity.Pagelable;
import com.wangxy.exoskeleton.entity.TranslateResult;
import com.wangxy.exoskeleton.service.IDealI18NService;
import com.wangxy.exoskeleton.service.IDictItemService;
import com.wangxy.exoskeleton.service.IPageLableService;
@Service
public class DealI18NServiceImpl implements IDealI18NService {
	@Autowired
	IPageLableService pageLableService;
	@Autowired
	IDictItemService dictItemService;

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#autoDealI18N(java.lang.String)
	 */
	@Override
	public void autoDealI18N(String path) throws IOException {
		String[] pathPart = path.split("\\.");
        //注意要替换转义字符\得用\\\\
        String[] folderAndFile = pathPart[0].split("\\\\");
        //文件夹名
        String pageId = folderAndFile[folderAndFile.length - 1];

        File file = new File(path);
        File[] tempList = file.listFiles();
        Set<String> dictSet = new HashSet<String>();
        Map<String, String> chineseWords = new HashMap<String, String>();
        for (File file1 : tempList) {
            String absolutePath = file1.getAbsolutePath();
            //html中中文字
            chineseWords.putAll(jsoupGetCnWordFromHtml(absolutePath));
            //数据字典
            dictSet.addAll(getDict(absolutePath));
        }
        
		String dictSqlPath = path+"/" + pageId+"_dictSql.sql";
		generateDictSqlFile(dictSet,dictSqlPath);
        String pageSqlPath = path+"/" + pageId+"_pageSql.sql";
        // html处理。产生sql脚本
        Map<String, TranslateResult> htmlCodeMap = htmlCodeDeal(pageSqlPath, pageId,chineseWords);
        // 根据数据库,用中文查询对应的标签语法，并替换到文件中
        for (File file1 : tempList) {
        	 String absolutePath = file1.getAbsolutePath();

             List<String> list = FileUtils.readLines(new File(absolutePath));
             // 固定替换
             List<String> resultListAfterHtml = fixReplaceDeal4Html(list,htmlCodeMap);
             // js代码处理。
             // 固定替换
             List<String> resultList = fixReplaceDeal(resultListAfterHtml);
             // 固定替换后进行翻译替换
             // resultList = translateDeal(resultList);

             //直接覆盖原文件
             File resultFile = new File(absolutePath);
             FileUtils.writeLines(resultFile, resultList);
        }
	}

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#generateDictSqlFile(java.util.Set, java.lang.String)
	 */
	@Override
	public void generateDictSqlFile(Set<String> dict, String path) throws IOException {
		if (dict == null||dict.size()==0) {
			return ;
		}
		List<String> dictSql = new ArrayList<String>();
		List<String> cnDictSql = null;
		List<String> enDictSql = null;
		for (String dictEntryCode : dict) {
			List<DictItem> enDictItems = dictItemService.getDictItems(dictEntryCode, "en");
			if (enDictItems==null||enDictItems.size()==0) {//不存在en
				List<DictItem> cnDictItems = dictItemService.getCnDictItems(dictEntryCode);
				cnDictSql = new ArrayList<String>();
				enDictSql = new ArrayList<String>();
				
				for (DictItem dictItem : cnDictItems) {
					cnDictSql.add(generateInsDictSql(dictItem, "zh_CN"));
					//翻译dict中中文
					String transResult = BaiduTranslateUtil.translate4PrdUpt(dictItem.getDictItemName(), "zh", "en");
					dictItem.setDictItemName(firstUpperFormat(transResult," "));
					enDictSql.add(generateInsDictSql(dictItem, "en"));
				}
				
				String cnLang = "zh_CN";
				String delItemExample = "delete from tsys_dict_item where dict_entry_code='"+dictEntryCode+"' and lang='"+cnLang+"';\n";
				dictSql.add(delItemExample);
				dictSql.addAll(cnDictSql);
				dictSql.add("\n");
				dictSql.add("\n");
				String enLang = "en";
				String delEnItemExample = "delete from tsys_dict_item where dict_entry_code='"+dictEntryCode+"' and lang='"+enLang+"';\n";
				dictSql.add(delEnItemExample);
				dictSql.addAll(enDictSql);
				dictSql.add("\n");
				dictSql.add("\n");
				
			}
			
		}
		
		File resultFile = new File(path);
		FileUtils.writeLines(resultFile, "UTF-8", dictSql);
	}

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#generateInsDictSql(com.wangxy.exoskeleton.entity.DictItem, java.lang.String)
	 */
	@Override
	public String generateInsDictSql(DictItem dictItem, String lang) {
		String example = "insert into tsys_dict_item (DICT_ITEM_CODE, DICT_ENTRY_CODE, DICT_ITEM_NAME, DICT_ITEM_ORDER, LIFECYCLE, PLATFORM, REL_CODE, LANG)\n" 
				+ "values ("+"'" + dictItem.getDictItemCode() + "',"
				+"'" + dictItem.getDictEntryCode()+ "',"
				+"'" + dictItem.getDictItemName()+ "',"
			    + dictItem.getDictItemOrder()+ ","
				+"null" +","
				+"null" +","
				+"null" +","
				+"'" + lang  +"'"
				+");";
		return example;
	}

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#getDict(java.lang.String)
	 */
	@Override
	public Set<String> getDict(String path) throws IOException {
		File input = new File(path);
		Document doc = Jsoup.parse(input, "UTF-8");
		
		Set<String> dict = new HashSet<String>();
		//jui:Init标签属性
		Elements allDict = doc.getElementsByTag("jui:Init");
		for (Element dictTag : allDict) {
			String params = dictTag.attr("params");
			String[] dictNames = params.split(",");
			for (String dictName : dictNames) {
				dict.add(dictName);
			}
		}
		//input标签data-options里的url属性的&key=
		Elements allInput = doc.getElementsByTag("input");
		for (Element dictTag : allInput) {
			String options = dictTag.attr("data-options");
			if (options.contains("url") && options.contains("key=")) {
				
				options = toStrictJson(options);
				
				JSONObject jsonObject = JSONObject.parseObject(options);
				String url = jsonObject.getString("url");
				//获取url参数
				String paramIn = url.substring(url.indexOf("?") + 1, url.length());
				paramIn = paramIn.replaceAll("=", "\":\"");
				paramIn = paramIn.replaceAll("&", "\",\"");
				paramIn = "{\"" + paramIn + "\"}";
				JSONObject urlParam = JSONObject.parseObject(paramIn);
				dict.add(urlParam.getString("key"));
			}
			
		}
		return dict;
	}

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#htmlCodeDeal(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	@Transactional
	public Map<String, TranslateResult> htmlCodeDeal(String path, String pageId,Map<String, String> chineseWords) throws IOException {
		//翻译结果Map，包括直接界面英文，page表现有，百度翻译
		Map<String, TranslateResult> translateMap = new HashMap<String, TranslateResult>();
		//包括百度翻译部分，直接界面英文
		List<TranslateResult> wordNeedGenSql = new ArrayList<TranslateResult>();
		
		// 翻译后，产生tsys_pagelable对象，用这个对象来产生i18N脚本，把对象插入数据库
		//获取最大id
		int id = pageLableService.getMaxId();
		//int id = 901000;
		id = id/100*100;
		id += 100;
		for (Map.Entry<String, String> entry : chineseWords.entrySet()) {
			// System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			TranslateResult res = new TranslateResult();
			// 现有的没有才去翻译.拿中文匹配
			Pagelable matchPagelable = pageLableService.matchPagelable(entry.getKey());
			if (matchPagelable != null) {
				Pagelable pagelableInEn = pageLableService.getPagelable(matchPagelable.getPageId(), matchPagelable.getLableId(), "en");
				res.setPageId(pagelableInEn.getPageId());
				res.setLabelId(pagelableInEn.getLableId());
				res.setSource(entry.getKey());
				res.setTranslateResult(pagelableInEn.getLableInfo());
			} else {
				res.setPageId(pageId);
				res.setLabelId(String.valueOf(id));
				res.setSource(entry.getKey());
				// 属性有英文的直接用
				if (!StringUtils.isEmpty(entry.getValue())) {
					String translatedWord = entry.getValue();
					res.setTranslateResult(firstUpperFormat(translatedWord, "_"));
				} else {
					String transResult = BaiduTranslateUtil.translate4PrdUpt(res.getSource(), "auto", "en");
					res.setTranslateResult(firstUpperFormat(transResult, " "));
				}
				pageLableService.addPagelable(convertTranslateResultToPagelable(res));
				wordNeedGenSql.add(res);
				id++;
			}
			translateMap.put(res.getSource(), res);
		}
		// 产生翻译表，产生脚本，插入数据库
		List<String> pageLableSql = new ArrayList<String>();
		if (wordNeedGenSql.size()>0){
			//pageLableSql
			String delExample = "delete from tsys_pagelable where page_id='"+wordNeedGenSql.get(0).getPageId()+"';";
			pageLableSql.add(delExample);
		}
		Pagelable enLable = new Pagelable();
		Pagelable cnLable = new Pagelable();
		for (TranslateResult translateResult : wordNeedGenSql) {
			String enExample = "insert into tsys_pagelable (PAGE_ID, LABLE_ID, LANG, LABLE_INFO)\n" +
					"values ('"+translateResult.getPageId()+"', '"+translateResult.getLabelId()+"', 'en', '"+translateResult.getTranslateResult()+"');";
			String cnExample = "insert into tsys_pagelable (PAGE_ID, LABLE_ID, LANG, LABLE_INFO)\n" +
					"values ('"+translateResult.getPageId()+"', '"+translateResult.getLabelId()+"', 'zh_CN', '"+translateResult.getSource()+"');";
			pageLableSql.add(enExample);
			pageLableSql.add(cnExample);
			
			enLable.setPageId(translateResult.getPageId());
			enLable.setLableId(translateResult.getLabelId());
			enLable.setLang("en");
			enLable.setLableInfo(translateResult.getTranslateResult());
			
			cnLable.setPageId(translateResult.getPageId());
			cnLable.setLableId(translateResult.getLabelId());
			cnLable.setLang("cn");
			cnLable.setLableInfo(translateResult.getSource());
			pageLableService.addPagelable(enLable);
			pageLableService.addPagelable(cnLable);
		}

		File resultFile = new File(path);
		FileUtils.writeLines(resultFile,"UTF-8",pageLableSql);
		return translateMap;
	}

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#convertTranslateResultToPagelable(com.wangxy.exoskeleton.entity.TranslateResult)
	 */
	@Override
	public Pagelable convertTranslateResultToPagelable(TranslateResult res) {
		Pagelable pagelable = new Pagelable();
		pagelable.setPageId(res.getPageId());
		pagelable.setLableId(res.getLabelId());
		pagelable.setLang("en");
		pagelable.setLableInfo(res.getTranslateResult());
		return pagelable;
	}

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#fixReplaceDeal4Html(java.util.List, java.util.Map)
	 */
	@Override
	public List<String> fixReplaceDeal4Html(List<String> oriList,Map<String, TranslateResult> htmlCodeMap) throws IOException {
		List<String> reultLines = new ArrayList<>();
		boolean isJsCode = false;
		for (String ss : oriList) {
			String resultLine =ss;
			if (ss.contains("<script>")) {
				isJsCode = true;
			}
			if (!isJsCode) {
				//遍历key，如果包括key，就去替换
				for (Map.Entry<String, TranslateResult> entry : htmlCodeMap.entrySet()) {
					if (ss.contains(entry.getKey())) {
						TranslateResult translateResult = entry.getValue();
						resultLine = resultLine.replace(entry.getKey(), gen18NTag(translateResult));
					}
				}
			}
			reultLines.add(resultLine);
		}
		// System.out.println(sb.toString());
		return reultLines;
	}
	
	
	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#gen18NTag(com.wangxy.exoskeleton.entity.TranslateResult)
	 */
	@Override
	public String gen18NTag(TranslateResult translateResult) {
		return "<jui:I18N page=\""+translateResult.getPageId()+"\" id=\""+translateResult.getLabelId()+"\" ></jui:I18N>";
	}

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#fixReplaceDeal(java.util.List)
	 */
	@Override
	public List<String> fixReplaceDeal(List<String> oriList) throws IOException {
		List<String> reultLines = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		boolean isJsCode = false;
		for (String ss : oriList) {
			// sb.append(ss + "\n");
			if (ss.contains("<script>")) {
				isJsCode = true;
			}
			String resultLine =ss;
			if (isJsCode) {
				resultLine = autoReplaceDeal(ss);
			}
			reultLines.add(resultLine);
		}
		// System.out.println(sb.toString());
		return reultLines;
	}

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#translateDeal(java.util.List)
	 */
	@Override
	public List<String> translateDeal(List<String> oriList) throws IOException {
		Set<String> lineToTranslate = new HashSet<String>();
		//这里只翻译js脚本中
		boolean isJsCode = false;
		for (String ss : oriList) {
			if (ss.contains("<script>")) {
				isJsCode = true;
			}
			if (isJsCode) {
				lineToTranslate.addAll(getTranslateLine(ss));
			}
		}
		// System.out.println(sb.toString());
		//翻译产生map集合
		//中文字符串-----对应翻译结果对象（id,翻译结果1）
		List<TranslateResult> translateResults = generateTranslateResult(lineToTranslate);

		//循环再次替换
		List<String> reultLines = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (String ss : oriList) {
			String resultLine = new String("");
			for (TranslateResult translateResult : translateResults) {
				resultLine = ss.replaceAll(translateResult.getSource(),translateResult.getPageId()+"."+translateResult.getLabelId());
			}
			reultLines.add(resultLine);
		}
		return reultLines;
	}

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#generateTranslateResult(java.util.Set)
	 */
	@Override
	public List<TranslateResult> generateTranslateResult(Set<String> lineToTranslate) {
		List<TranslateResult> translateResults = new ArrayList<TranslateResult>();
		int i =0;
		for (String line : lineToTranslate) {
			i++;
			TranslateResult tr = new TranslateResult();
			tr.setPageId("temp");
			tr.setLabelId("info"+i);
			tr.setSource(line);
			String transResult = BaiduTranslateUtil.translate4PrdUpt(line, "zh", "en");
			tr.setTranslateResult(firstUpperFormat(transResult," "));
			translateResults.add(tr);
		}
		return translateResults;
	}


	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#getTranslateLine(java.lang.String)
	 */
	@Override
	public Set<String> getTranslateLine(String line) {
		Set<String> translateLine = new HashSet<String>();
		String regex = "'(.*)'";
		List<String> matchString = getMatchString(line, regex);
		for (String matchWord : matchString) {
			if (isContainChinese(matchWord)){
				translateLine.add(matchWord);
			}
		}
		return translateLine;
	}

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#getMatchString(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getMatchString(String sourceString,String regex) {
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

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#autoReplaceDeal(java.lang.String)
	 */
	@Override
	public String autoReplaceDeal(String inLine) {
		inLine = inLine.replaceAll("['|\"]提示['|\"]", "dialogMsg.info");
		inLine = inLine.replaceAll("['|\"]警告['|\"]", "dialogMsg.warn");
		inLine = inLine.replaceAll("['|\"]确认['|\"]", "dialogMsg.confirm");
		inLine = inLine.replaceAll("['|\"]请选择要删除的数据！['|\"]", "dialogMsg.delWarnInfo");
		inLine = inLine.replaceAll("['|\"]您确认想要删除(.*)['|\"]", "dialogMsg.delConfirmInfo");
		//inLine = inLine.replaceAll("[^" + "'*\\+\\+\\+'" + "]", "dialogMsg.processingInfo");
		inLine = inLine.replaceAll("['|\"]请选择一条要修改(.*)['|\"]", "dialogMsg.edtWarnInfo");
		inLine = inLine.replaceAll(",['|\"](.*)\\+{3,}['|\"]",",dialogMsg.processingInfo");
		//inLine = inLine.replaceAll("'正在提交\\+\\+\\+'", "dialogMsg.processingInfo");
		return inLine;
	}

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#needAutoReplace(java.lang.String)
	 */
	@Override
	public boolean needAutoReplace(String inLine) {
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#jsoupGetCnWordFromHtml(java.lang.String)
	 */
	@Override
	public Map<String,String> jsoupGetCnWordFromHtml(String path) throws IOException {
		File input = new File(path);
		Document doc = Jsoup.parse(input, "UTF-8");
		
		Map<String,String> chinese = new HashMap<String,String>();
		//th标签属性
		Elements allTh = doc.getElementsByTag("th");
		for (Element th : allTh) {
			String options = th.attr("data-options");
			options = toStrictJson(options);
			JSONObject jsonObject = JSONObject.parseObject(options);
			if (jsonObject.getString("field").trim().length() > 0) {
				String key = jsonObject.getString("title");
				if (!StringUtils.isEmpty(key)) {
					chinese.put(key, jsonObject.getString("field"));
				}
			}
		}
		//input标签属性
		Elements allInput = doc.getElementsByTag("input");
		for (Element inputTag : allInput) {
			String options = inputTag.attr("data-options");
			options = toStrictJson(options);
			options = options.replaceAll("\\[(.*)\\]", "temp");
			JSONObject jsonObject = JSONObject.parseObject(options);
			
			String key = jsonObject.getString("label");
			if (!StringUtils.isEmpty(key)) {
				chinese.put(key, jsonObject.getString("name"));
			}
		}
		//a标签包裹值
		Elements allATag = doc.getElementsByTag("a");
		for (Element aTag : allATag) {
//			chinese.put(aTag.text(), aTag.attr("id"));
			String key = aTag.text();
			if (!StringUtils.isEmpty(key)) {
				chinese.put(key, "");
			}
		}
		//legend标签包裹值
		Elements allLegendTag = doc.getElementsByTag("a");
		for (Element legend : allLegendTag) {
			String key = legend.text();
			if (!StringUtils.isEmpty(key)) {
				chinese.put(key, "");
			}
		}
		
		
		
		
		return chinese;
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

	/* (non-Javadoc)
	 * @see com.wangxy.exoskeleton.controller.IDealI18NService#toStrictJson(java.lang.String)
	 */
	@Override
	public String toStrictJson(String options) {
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
	 * 首字母大写
	 * 产生英文格式调整，首字母大写
	 * @param targetLangWord
	 * @return 
	 */
	public static String firstUpperFormat(String targetLangWord,String splitword) {
		String[] words = targetLangWord.split(splitword);
		String newWord = "";
		//按空格分隔成数组
	    for (int i = 0; i <words.length ; i++) {
	    // .substring(0,1) 截取数组的首字母   +split[i].substring(1); 再加上后面
	        String word = words[i].substring(0, 1).toUpperCase()+words[i].substring(1);
	        newWord+=" ";
	        newWord+=word;
	    }
		return newWord;
	}


}