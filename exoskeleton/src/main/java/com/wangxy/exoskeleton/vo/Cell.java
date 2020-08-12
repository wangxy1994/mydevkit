/**
  * Copyright 2020 bejson.com 
  */
package com.wangxy.exoskeleton.vo;
import java.util.Date;

/**
 * Auto-generated: 2020-08-12 9:19:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Cell {

    /**
     * 实时价格
     */
    private String price;
    /**
     * 正股涨跌幅
     */
    private String increase_rt;
    /**
     * 现价比转股价
     */
    private String pma_rt;
    /**
     * PB。实时
     */
    private String pb;
    private int rid;
    /**
     * 股票代码
     */
    private String stock_id;
    /**
     * 股票名称
     */
    private String stock_nm;
    /**
     * 债券代码
     */
    private String bond_id;
    /**
     * 债券名称
     */
    private String bond_nm;
    /**
     * 发行规模（亿元）
     */
    private String amount;
    private String b_shares;
    private String pg_shares;
    /**
     * 百元股票含权（元）
     */
    private String cb_amount;
    /**
     * 20日均价
     */
    private String ma20_price;
    /**
     * 转股价
     */
    private String convert_price;
    private Date apply_date;
    private String apply_cd;
    private String ration_cd;
    private Date list_date;
    private String status_cd;
    /**
     * 股东配售率。百分值
     */
    private String ration_rt;
    /**
     * 网上规模（亿元）
     */
    private String online_amount;
    /**
     * 中签率。百分值
     */
    private String lucky_draw_rt;
    private int individual_limit;
    /**
     * 报销比例。百分值
     */
    private String underwriter_rt;
    /**
     * 评级
     */
    private String rating_cd;
    private String offline_limit;
    /**
     * 网下户数（户）
     */
    private String offline_accounts;
    private String offline_draw;
    private String valid_apply_raw;
    /**
     * 申购建议
     */
    private String jsl_advise_text;
    /**
     * 进展名称
     * 发行流程：董事会预案 → 股东大会批准 → 证监会受理 → 发审委通过 → 证监会核准/同意注册 → 发行公告 
     * 董事会预案
     * 股东大会通过
     * 发审委通过
     * 证监会核准\/同意注册
     * 2020-08-13申购<br>申购代码754978
     * 2020-08-12上市
     */
    private String progress_nm;
    /**
     * 类型。可转债/可交换债
     */
    private String cb_type;
    /**
     * 进展公告日期
     */
    private String progress_dt;
    private String cp_flag;
    private String apply_tips;
    private String ap_flag;
    /**
     * 单账户中签（顶格）
     */
    private String single_draw;
    /**
     * 申购户数（万户）
     */
    private String valid_apply;
    public void setPrice(String price) {
         this.price = price;
     }
     public String getPrice() {
         return price;
     }

    public void setIncrease_rt(String increase_rt) {
         this.increase_rt = increase_rt;
     }
     public String getIncrease_rt() {
         return increase_rt;
     }

    public void setPma_rt(String pma_rt) {
         this.pma_rt = pma_rt;
     }
     public String getPma_rt() {
         return pma_rt;
     }

    public void setPb(String pb) {
         this.pb = pb;
     }
     public String getPb() {
         return pb;
     }

    public void setRid(int rid) {
         this.rid = rid;
     }
     public int getRid() {
         return rid;
     }

    public void setStock_id(String stock_id) {
         this.stock_id = stock_id;
     }
     public String getStock_id() {
         return stock_id;
     }

    public void setStock_nm(String stock_nm) {
         this.stock_nm = stock_nm;
     }
     public String getStock_nm() {
         return stock_nm;
     }

    public void setBond_id(String bond_id) {
         this.bond_id = bond_id;
     }
     public String getBond_id() {
         return bond_id;
     }

    public void setBond_nm(String bond_nm) {
         this.bond_nm = bond_nm;
     }
     public String getBond_nm() {
         return bond_nm;
     }

    public void setAmount(String amount) {
         this.amount = amount;
     }
     public String getAmount() {
         return amount;
     }

    public void setB_shares(String b_shares) {
         this.b_shares = b_shares;
     }
     public String getB_shares() {
         return b_shares;
     }

    public void setPg_shares(String pg_shares) {
         this.pg_shares = pg_shares;
     }
     public String getPg_shares() {
         return pg_shares;
     }

    public void setCb_amount(String cb_amount) {
         this.cb_amount = cb_amount;
     }
     public String getCb_amount() {
         return cb_amount;
     }

    public void setMa20_price(String ma20_price) {
         this.ma20_price = ma20_price;
     }
     public String getMa20_price() {
         return ma20_price;
     }

    public void setConvert_price(String convert_price) {
         this.convert_price = convert_price;
     }
     public String getConvert_price() {
         return convert_price;
     }

    public void setApply_date(Date apply_date) {
         this.apply_date = apply_date;
     }
     public Date getApply_date() {
         return apply_date;
     }

    public void setApply_cd(String apply_cd) {
         this.apply_cd = apply_cd;
     }
     public String getApply_cd() {
         return apply_cd;
     }

    public void setRation_cd(String ration_cd) {
         this.ration_cd = ration_cd;
     }
     public String getRation_cd() {
         return ration_cd;
     }

    public void setList_date(Date list_date) {
         this.list_date = list_date;
     }
     public Date getList_date() {
         return list_date;
     }

    public void setStatus_cd(String status_cd) {
         this.status_cd = status_cd;
     }
     public String getStatus_cd() {
         return status_cd;
     }

    public void setRation_rt(String ration_rt) {
         this.ration_rt = ration_rt;
     }
     public String getRation_rt() {
         return ration_rt;
     }

    public void setOnline_amount(String online_amount) {
         this.online_amount = online_amount;
     }
     public String getOnline_amount() {
         return online_amount;
     }

    public void setLucky_draw_rt(String lucky_draw_rt) {
         this.lucky_draw_rt = lucky_draw_rt;
     }
     public String getLucky_draw_rt() {
         return lucky_draw_rt;
     }

    public void setIndividual_limit(int individual_limit) {
         this.individual_limit = individual_limit;
     }
     public int getIndividual_limit() {
         return individual_limit;
     }

    public void setUnderwriter_rt(String underwriter_rt) {
         this.underwriter_rt = underwriter_rt;
     }
     public String getUnderwriter_rt() {
         return underwriter_rt;
     }

    public void setRating_cd(String rating_cd) {
         this.rating_cd = rating_cd;
     }
     public String getRating_cd() {
         return rating_cd;
     }

    public void setOffline_limit(String offline_limit) {
         this.offline_limit = offline_limit;
     }
     public String getOffline_limit() {
         return offline_limit;
     }

    public void setOffline_accounts(String offline_accounts) {
         this.offline_accounts = offline_accounts;
     }
     public String getOffline_accounts() {
         return offline_accounts;
     }

    public void setOffline_draw(String offline_draw) {
         this.offline_draw = offline_draw;
     }
     public String getOffline_draw() {
         return offline_draw;
     }

    public void setValid_apply_raw(String valid_apply_raw) {
         this.valid_apply_raw = valid_apply_raw;
     }
     public String getValid_apply_raw() {
         return valid_apply_raw;
     }

    public void setJsl_advise_text(String jsl_advise_text) {
         this.jsl_advise_text = jsl_advise_text;
     }
     public String getJsl_advise_text() {
         return jsl_advise_text;
     }

    public void setProgress_nm(String progress_nm) {
         this.progress_nm = progress_nm;
     }
     public String getProgress_nm() {
         return progress_nm;
     }

    public void setCb_type(String cb_type) {
         this.cb_type = cb_type;
     }
     public String getCb_type() {
         return cb_type;
     }

    public void setProgress_dt(String progress_dt) {
         this.progress_dt = progress_dt;
     }
     public String getProgress_dt() {
         return progress_dt;
     }

    public void setCp_flag(String cp_flag) {
         this.cp_flag = cp_flag;
     }
     public String getCp_flag() {
         return cp_flag;
     }

    public void setApply_tips(String apply_tips) {
         this.apply_tips = apply_tips;
     }
     public String getApply_tips() {
         return apply_tips;
     }

    public void setAp_flag(String ap_flag) {
         this.ap_flag = ap_flag;
     }
     public String getAp_flag() {
         return ap_flag;
     }

    public void setSingle_draw(String single_draw) {
         this.single_draw = single_draw;
     }
     public String getSingle_draw() {
         return single_draw;
     }

    public void setValid_apply(String valid_apply) {
         this.valid_apply = valid_apply;
     }
     public String getValid_apply() {
         return valid_apply;
     }
	@Override
	public String toString() {
		return "PreCB [price=" + price + ", increase_rt=" + increase_rt + ", pma_rt=" + pma_rt + ", pb=" + pb + ", rid=" + rid + ", stock_id=" + stock_id + ", stock_nm=" + stock_nm
				+ ", bond_id=" + bond_id + ", bond_nm=" + bond_nm + ", amount=" + amount + ", b_shares=" + b_shares + ", pg_shares=" + pg_shares + ", cb_amount=" + cb_amount
				+ ", ma20_price=" + ma20_price + ", convert_price=" + convert_price + ", apply_date=" + apply_date + ", apply_cd=" + apply_cd + ", ration_cd=" + ration_cd
				+ ", list_date=" + list_date + ", status_cd=" + status_cd + ", ration_rt=" + ration_rt + ", online_amount=" + online_amount + ", lucky_draw_rt=" + lucky_draw_rt
				+ ", individual_limit=" + individual_limit + ", underwriter_rt=" + underwriter_rt + ", rating_cd=" + rating_cd + ", offline_limit=" + offline_limit
				+ ", offline_accounts=" + offline_accounts + ", offline_draw=" + offline_draw + ", valid_apply_raw=" + valid_apply_raw + ", jsl_advise_text=" + jsl_advise_text
				+ ", progress_nm=" + progress_nm + ", cb_type=" + cb_type + ", progress_dt=" + progress_dt + ", cp_flag=" + cp_flag + ", apply_tips=" + apply_tips + ", ap_flag="
				+ ap_flag + ", single_draw=" + single_draw + ", valid_apply=" + valid_apply + "]";
	}
     
     

}