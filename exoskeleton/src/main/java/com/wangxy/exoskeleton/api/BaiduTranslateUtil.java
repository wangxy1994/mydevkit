package com.wangxy.exoskeleton.api;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangxy.exoskeleton.api.translate.TransApi;

public class BaiduTranslateUtil {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20200518000460986";
    private static final String SECURITY_KEY = "Tqd5L9kq6vnNP1suC57L";
    static TransApi api = new TransApi(APP_ID, SECURITY_KEY);

    public static void main(String[] args) {
        //TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "高度600米";
        System.out.println(api.getTransResult(query, "auto", "en"));
    }

    public static TransApi getApi() {
        return api;
    }

    public static void setApi(TransApi api) {
        BaiduTranslateUtil.api = api;
    }
    
    public static String translate4PrdUpt(String cnWord,String sourceLang,String targetLang) {
    	String transResult = getApi().getTransResult(cnWord, sourceLang, targetLang);
		JSONObject transResultJson = JSONObject.parseObject(transResult);
		JSONArray resultArray = transResultJson.getJSONArray("trans_result");
		String targetLangWord = resultArray.getJSONObject(0).getString("dst");
		return targetLangWord;
    }

	
    
}
