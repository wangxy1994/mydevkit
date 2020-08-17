package com.wangxy.exoskeleton.api.tushare.vo;

/**
 * 股票列表
 * https://tushare.pro/document/2?doc_id=25
 * @author user3
 *
 */
public class StockBasic {

    private String ts_code;
    private String symbol;
    private String name;
    private String area;
    private String industry;
    private String fullname;
    private String enname;
    private String market;
    private String exchange;
    private String curr_type;
    private String list_status;
    private String list_date;
    private String delist_date;
    private String is_hs;
	public String getTs_code() {
		return ts_code;
	}
	public void setTs_code(String ts_code) {
		this.ts_code = ts_code;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getEnname() {
		return enname;
	}
	public void setEnname(String enname) {
		this.enname = enname;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getCurr_type() {
		return curr_type;
	}
	public void setCurr_type(String curr_type) {
		this.curr_type = curr_type;
	}
	public String getList_status() {
		return list_status;
	}
	public void setList_status(String list_status) {
		this.list_status = list_status;
	}
	public String getList_date() {
		return list_date;
	}
	public void setList_date(String list_date) {
		this.list_date = list_date;
	}
	public String getDelist_date() {
		return delist_date;
	}
	public void setDelist_date(String delist_date) {
		this.delist_date = delist_date;
	}
	public String getIs_hs() {
		return is_hs;
	}
	public void setIs_hs(String is_hs) {
		this.is_hs = is_hs;
	}
	@Override
	public String toString() {
		return "StockBasic [ts_code=" + ts_code + ", symbol=" + symbol + ", name=" + name + ", area=" + area + ", industry=" + industry + ", fullname=" + fullname + ", enname="
				+ enname + ", market=" + market + ", exchange=" + exchange + ", curr_type=" + curr_type + ", list_status=" + list_status + ", list_date=" + list_date
				+ ", delist_date=" + delist_date + ", is_hs=" + is_hs + "]";
	}
    
    

}