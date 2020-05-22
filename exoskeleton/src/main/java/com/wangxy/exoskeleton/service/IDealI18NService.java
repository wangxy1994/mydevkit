package com.wangxy.exoskeleton.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.wangxy.exoskeleton.entity.DictItem;
import com.wangxy.exoskeleton.entity.Pagelable;
import com.wangxy.exoskeleton.entity.TranslateResult;

public interface IDealI18NService {

	void autoDealI18N(String path) throws IOException;

	/**
	 * 产生dict翻译表，产生脚本，插入数据库
	 * @param dict
	 * @param path 
	 * @throws IOException 
	 */
	void generateDictSqlFile(Set<String> dict, String path) throws IOException;

	String generateInsDictSql(DictItem dictItem, String lang);

	/**
	 * 获取页面中所有的数据字典
	 * @param path
	 * @return
	 * @throws IOException
	 */
	Set<String> getDict(String path) throws IOException;

	/**
	 * html代码处理产生page脚本，返回中文翻译结果
	 * @param path
	 * @param pageId
	 * @return
	 * @throws IOException
	 */
	Map<String, TranslateResult> htmlCodeDeal(String path, String pageId, Map<String, String> chineseWords) throws IOException;

	Pagelable convertTranslateResultToPagelable(TranslateResult res);

	List<String> fixReplaceDeal4Html(List<String> oriList, Map<String, TranslateResult> htmlCodeMap) throws IOException;

	String gen18NTag(TranslateResult translateResult);

	List<String> fixReplaceDeal(List<String> oriList) throws IOException;

	/**
	 * 剩余部分翻译
	 * @param oriList
	 * @return
	 * @throws IOException
	 */
	List<String> translateDeal(List<String> oriList) throws IOException;

	/**
	 * 产生翻译结果对象集合
	 * @param lineToTranslate
	 * @return
	 */
	List<TranslateResult> generateTranslateResult(Set<String> lineToTranslate);

	/**
	 * 获得需要翻译的文本
	 * @param line
	 * @return
	 */
	Set<String> getTranslateLine(String line);

	/**
	 * 正则表达式获得匹配内容
	 * @param sourceString
	 * @param regex
	 * @return
	 */
	List<String> getMatchString(String sourceString, String regex);

	/**
	 * 固定替换处理
	 * @param inLine
	 * @return
	 */
	String autoReplaceDeal(String inLine);

	boolean needAutoReplace(String inLine);

	/**
	 * 在固定情况下，获取中文，field/name的键值对
	 * 
	 * @param path
	 * @throws IOException
	 */
	Map<String, String> jsoupGetCnWordFromHtml(String path) throws IOException;

	String toStrictJson(String options);

}