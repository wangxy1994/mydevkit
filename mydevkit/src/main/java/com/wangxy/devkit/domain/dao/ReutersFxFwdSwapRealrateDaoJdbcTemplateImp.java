package com.wangxy.devkit.domain.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wangxy.devkit.domain.bean.ReutersFxFwdSwapRealrate;

@Repository
public class ReutersFxFwdSwapRealrateDaoJdbcTemplateImp implements ReutersFxFwdSwapRealrateDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReutersFxFwdSwapRealrateDaoJdbcTemplateImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Boolean add(ReutersFxFwdSwapRealrate obj) {
        String sql = "insert into reuters_fx_fwd_swap_realrate(cypairname,periodcode,updatedatetime,countnum,quotetype,productname,basepoint,trademark,quotettl,bid,ask,mid,sourcebid,sourceask,sourcemid,runstate,valuedate,maturitydate,spotbid,spotask,spotmid,spottrademark,pricetype,feedcode,datasourceid,priceid,spotticksize,fwdticksize,swapticksize,quoteunit,costbid,costask,costmid,exchbid,exchask,exchmid,cashbid,cashask,cashmid,impdate,imptime)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,obj.getCypairname(),obj.getPeriodcode(),obj.getUpdatedatetime(),obj.getCountnum(),
        		obj.getQuotetype(),obj.getProductname(),obj.getBasepoint(),obj.getTrademark(),
        		obj.getQuotettl(),obj.getBid(),obj.getAsk(),obj.getMid(),
        		obj.getSourcebid(),obj.getSourceask(),obj.getSourcemid(),obj.getRunstate(),
        		obj.getValuedate(),obj.getMaturitydate(),obj.getSpotbid(),obj.getSpotask(),
        		obj.getSpotmid(),obj.getSpottrademark(),obj.getPricetype(),obj.getFeedcode(),
        		obj.getDatasourceid(),obj.getPriceid(),obj.getSpotticksize(),obj.getFwdticksize(),
        		obj.getSwapticksize(),obj.getQuoteunit(),obj.getCostbid(),obj.getCostask(),
        		obj.getCostmid(),obj.getExchbid(),obj.getExchask(),obj.getExchmid(),
        		obj.getCashbid(),obj.getCashask(),obj.getCashmid(),obj.getImpdate(),
        		obj.getImptime()) > 0;
    }

    @Override
    public Boolean update(ReutersFxFwdSwapRealrate obj) {
        String sql = "update reuters_fx_fwd_swap_realrate set countnum=?,quotetype=?,productname=?,basepoint=?,trademark=?,quotettl=?,bid=?,ask=?,mid=?,sourcebid=?,sourceask=?,sourcemid=?,runstate=?,valuedate=?,maturitydate=?,spotbid=?,spotask=?,spotmid=?,spottrademark=?,pricetype=?,feedcode=?,datasourceid=?,priceid=?,spotticksize=?,fwdticksize=?,swapticksize=?,quoteunit=?,costbid=?,costask=?,costmid=?,exchbid=?,exchask=?,exchmid=?,cashbid=?,cashask=?,cashmid=?,impdate=?,imptime=? where cypairname=? and periodcode=? and updatedatetime=? ";
        return jdbcTemplate.update(sql, obj.getCountnum(),obj.getQuotetype(),obj.getProductname(),obj.getBasepoint(),
        		obj.getTrademark(),obj.getQuotettl(),obj.getBid(),obj.getAsk(),
        		obj.getMid(),obj.getSourcebid(),obj.getSourceask(),obj.getSourcemid(),
        		obj.getRunstate(),obj.getValuedate(),obj.getMaturitydate(),obj.getSpotbid(),
        		obj.getSpotask(),obj.getSpotmid(),obj.getSpottrademark(),obj.getPricetype(),
        		obj.getFeedcode(),obj.getDatasourceid(),obj.getPriceid(),obj.getSpotticksize(),
        		obj.getFwdticksize(),obj.getSwapticksize(),obj.getQuoteunit(),obj.getCostbid(),
        		obj.getCostask(),obj.getCostmid(),obj.getExchbid(),obj.getExchask(),
        		obj.getExchmid(),obj.getCashbid(),obj.getCashask(),obj.getCashmid(),
        		obj.getImpdate(),obj.getImptime(),obj.getCypairname(),obj.getPeriodcode(),
        		obj.getUpdatedatetime()) > 0;
    }

    @Override
    public boolean delete(ReutersFxFwdSwapRealrate obj) {
        String sql = "delete from reuters_fx_fwd_swap_realrate where cypairname=? and periodcode=? and updatedatetime=? ";
        return jdbcTemplate.update(sql, obj.getCypairname(),obj.getPeriodcode(),obj.getUpdatedatetime()) > 0;

    }

    @Override
    public ReutersFxFwdSwapRealrate locate(ReutersFxFwdSwapRealrate obj) {
        String sql = "select * from reuters_fx_fwd_swap_realrate where cypairname=? and periodcode=? and updatedatetime=? ";
        List<ReutersFxFwdSwapRealrate> result = jdbcTemplate.query(sql, new Object[] { obj.getCypairname(),obj.getPeriodcode(),obj.getUpdatedatetime()},new BeanPropertyRowMapper<ReutersFxFwdSwapRealrate>(ReutersFxFwdSwapRealrate.class));
        if (result!=null&&result.size()>0) {
        	return result.get(0);
		}else {
			return null;
		}
    }
}
