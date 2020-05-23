package com.wangxy.exoskeleton.entity;

public class TranslateResult {

    private String pageId;

    private String labelId;

    private String type;

    private String source;

    private String translateResult;
    
    /**
     * jsMsg国际化文件中的key
     */
    private String jsonKey;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTranslateResult() {
        return translateResult;
    }

    public void setTranslateResult(String translateResult) {
        this.translateResult = translateResult;
    }

	public String getJsonKey() {
		return jsonKey;
	}

	public void setJsonKey(String jsonKey) {
		this.jsonKey = jsonKey;
	}
    
    
}