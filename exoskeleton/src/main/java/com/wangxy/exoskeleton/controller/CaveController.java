package com.wangxy.exoskeleton.controller;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangxy.exoskeleton.api.BaiduTranslateUtil;
import com.wangxy.exoskeleton.entity.TranslateResult;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.annotations.Param;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wangxy.exoskeleton.entity.Pagelable;
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
		System.out.println(99999999);
//		path = "G:/同步dev/国际化/virtualcombiBefore/virtualCombiList.jsp";
		path = "I:/同步dev/国际化/virtualcombiBefore/virtualCombiList.jsp";
		// path = "D:/work/IRM/avengers-systemweb/src/main/webapp/ifm/pub/js/transdate/TransDateSet.js";
		// String content = IOUtils.toString(resourceLoader .getResource(path).getInputStream());
		List<String> list = FileUtils.readLines(new File(path));
		//
		// (path);
		// jsoupDeal(path);
		// html处理

		// js代码处理。
		// 固定替换
		List<String> resultList = apachCommonDeal(list);
		//固定替换后进行翻译替换
		resultList = translateDeal(resultList);


		//生成文件
		String[] pathPart = path.split("\\.");
		String resultPath = pathPart[0] + "_res_." + pathPart[1];
		File resultFile = new File(resultPath);
		FileUtils.writeLines(resultFile, resultList);

		String result = "hello";
		System.out.println(result);
		Pagelable pagelable = pageLableService.getPagelable(" ", "000001", "en");
		return pagelable.getLableInfo();
	}

	private List<String> apachCommonDeal(List<String> oriList) throws IOException {
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

	private List<String> translateDeal(List<String> oriList) throws IOException {
		Set<String> lineToTranslate = new HashSet<String>();

		for (String ss : oriList) {
			lineToTranslate.addAll(getTranslateLine(ss));
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
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	private String autoReplaceDeal(String inLine) {
		inLine = inLine.replaceAll("'警告'", "dialogMsg.warn");
		inLine = inLine.replaceAll("'确认'", "dialogMsg.confirm");
		inLine = inLine.replaceAll("'请选择要删除的数据！'", "dialogMsg.delWarnInfo");
		inLine = inLine.replaceAll("'您确认想要删除(.*)'", "dialogMsg.delConfirmInfo");
		//inLine = inLine.replaceAll("[^" + "'*\\+\\+\\+'" + "]", "dialogMsg.processingInfo");
		inLine = inLine.replaceAll("'请选择一条要修改(.*)'", "dialogMsg.edtWarnInfo");
		inLine = inLine.replaceAll(",'(.*)\\+{3,}'",",dialogMsg.processingInfo");
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
	private void jsoupDeal(String path) throws IOException {
		File input = new File(path);
		Document doc = Jsoup.parse(input, "UTF-8");

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
	}

}
