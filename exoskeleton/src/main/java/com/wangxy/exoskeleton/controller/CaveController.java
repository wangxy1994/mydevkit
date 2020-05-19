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

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.annotations.Param;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangxy.exoskeleton.api.BaiduTranslateUtil;
import com.wangxy.exoskeleton.entity.Pagelable;
import com.wangxy.exoskeleton.entity.TranslateResult;
import com.wangxy.exoskeleton.service.IPageLableService;

@RestController
@RequestMapping("/cave")
public class CaveController {
	@Autowired
	IPageLableService pageLableService;
	@Autowired
	DefaultResourceLoader resourceLoader;

	@RequestMapping("/entrance")
	public String entrance(@Param("path") String path) throws IOException {
		System.out.println("path==="+path);
		if (path.trim().length()==0) {
			path = "G:/同步dev/国际化/virtualcombiBefore/virtualCombiList.jsp";
		}
		// path = "I:/同步dev/国际化/virtualcombiBefore/virtualCombiList.jsp";
		// path = "D:/work/IRM/avengers-systemweb/src/main/webapp/ifm/pub/js/transdate/TransDateSet.js";
		// String content = IOUtils.toString(resourceLoader .getResource(path).getInputStream());
		//
		// (path);
		String[] pathPart = path.split("\\.");
		String[] folderAndFile = pathPart[0].split("/");
		String pageId = folderAndFile[folderAndFile.length - 1];
		// html处理
		//htmlCodeDeal(path, pageId);

		List<String> list = FileUtils.readLines(new File(path));
		// js代码处理。
		// 固定替换
		List<String> resultList = fixReplaceDeal(list);
		// 固定替换后进行翻译替换
		// resultList = translateDeal(resultList);

		// 生成文件

		String resultPath = pathPart[0] + "_res_." + pathPart[1];
		File resultFile = new File(resultPath);
		FileUtils.writeLines(resultFile, resultList);

		String result = "hello";
		System.out.println(result);
		Pagelable pagelable = pageLableService.getPagelable(" ", "000001", "en");
		return pagelable.getLableInfo();
	}

	private void htmlCodeDeal(String path, String pageId) throws IOException {
		Map<String, String> chineseWords = jsoupDeal(path);

		// 翻译后，产生tsys_pagelable对象，用这个对象来产生i18N脚本，把对象插入数据库
		// TODO 获取最大id
		int id = 901000;
		id = id/100*100;
		id += 100;
		List<TranslateResult> tr = new ArrayList<TranslateResult>();
		for (Map.Entry<String, String> entry : chineseWords.entrySet()) {
			// System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			TranslateResult res = new TranslateResult();
			res.setPageId(pageId);
			res.setLabelId(String.valueOf(id));
			res.setSource(entry.getKey());
			String transResult = BaiduTranslateUtil.getApi().getTransResult(res.getSource(), "auto", "en");
			res.setTranslateResult(transResult);
			tr.add(res);
			id++;
		}
		// 产生翻译表，产生脚本，插入数据库

		// pageLableService.addPagelable(pagelable);

		// 根据数据库,用中文查询对应的标签语法，并替换到文件中

		// 数据字典
	}

	private List<String> fixReplaceDeal(List<String> oriList) throws IOException {
		List<String> reultLines = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (String ss : oriList) {
			// sb.append(ss + "\n");
			String resultLine = new String("");
			// 根据指定情况替换
			if (needAutoReplace(ss)) {
				resultLine = autoReplaceDeal(ss);
			}
			reultLines.add(resultLine);
		}
		// System.out.println(sb.toString());
		return reultLines;
	}

	/**
	 * 剩余部分翻译
	 * @param oriList
	 * @return
	 * @throws IOException
	 */
	private List<String> translateDeal(List<String> oriList) throws IOException {
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

	/**
	 * 产生翻译结果对象集合
	 * @param lineToTranslate
	 * @return
	 */
	private List<TranslateResult> generateTranslateResult(Set<String> lineToTranslate) {
		List<TranslateResult> translateResults = new ArrayList<TranslateResult>();
		int i =0;
		for (String line : lineToTranslate) {
			i++;
			TranslateResult tr = new TranslateResult();
			tr.setPageId("temp");
			tr.setLabelId("info"+i);
			tr.setSource(line);
			String transResult = BaiduTranslateUtil.getApi().getTransResult(line, "zh", "en");
			JSONObject transResultJson = JSONObject.parseObject(transResult);
			JSONArray resultArray = transResultJson.getJSONArray("trans_result");
			tr.setTranslateResult(resultArray.getJSONObject(0).getString("dst"));
			translateResults.add(tr);
		}
		return translateResults;
	}


	/**
	 * 获得需要翻译的文本
	 * @param line
	 * @return
	 */
	private Set<String> getTranslateLine(String line) {
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

	/**
	 * 正则表达式获得匹配内容
	 * @param sourceString
	 * @param regex
	 * @return
	 */
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

	/**
	 * 固定替换处理
	 * @param inLine
	 * @return
	 */
	private String autoReplaceDeal(String inLine) {
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

	private boolean needAutoReplace(String inLine) {
		//
		return true;
	}

	/**
	 * 获取固定情况下的中文，field，翻译作为集合
	 * 
	 * @param path
	 * @throws IOException
	 */
	private Map<String,String> jsoupDeal(String path) throws IOException {
		File input = new File(path);
		Document doc = Jsoup.parse(input, "UTF-8");
		
		Map<String,String> chinese = new HashMap<String,String>();
		//th标签属性
		Elements allTh = doc.getElementsByTag("th");
		for (Element th : allTh) {
			String options = th.attr("data-options");
			options = "{" + options + "}";
			JSONObject jsonObject = JSONObject.parseObject(options);
			if (jsonObject.getString("field").trim().length() > 0) {
				chinese.put(jsonObject.getString("title"), jsonObject.getString("field"));
			}
		}
		//input标签属性
		Elements allInput = doc.getElementsByTag("input");
		for (Element inputTag : allInput) {
			String options = inputTag.attr("data-options");
			options = "{" + options + "}";
			JSONObject jsonObject = JSONObject.parseObject(options);
			chinese.put(jsonObject.getString("label"), jsonObject.getString("name"));
		}
		//a标签包裹值
		Elements allATag = doc.getElementsByTag("a");
		for (Element aTag : allATag) {
//			chinese.put(aTag.text(), aTag.attr("id"));
			chinese.put(aTag.text(), "");
		}
		//legend标签包裹值
		Elements allLegendTag = doc.getElementsByTag("a");
		for (Element legend : allLegendTag) {
			chinese.put(legend.text(), "");
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

}
