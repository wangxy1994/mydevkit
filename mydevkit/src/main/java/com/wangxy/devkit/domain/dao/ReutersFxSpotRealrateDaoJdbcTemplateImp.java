package com.wangxy.devkit.domain.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wangxy.devkit.domain.bean.ReutersFxSpotRealrate;

@Repository
public class ReutersFxSpotRealrateDaoJdbcTemplateImp implements ReutersFxSpotRealrateDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReutersFxSpotRealrateDaoJdbcTemplateImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Boolean add(ReutersFxSpotRealrate obj) {
        String sql = "insert into reuters_fx_spot_realrate(cypairname,updatedatetime,countnum,quotetype,productname,basepoint,trademark,quotettl,bid,ask,mid,sourcebid,sourceask,sourcemid,runstate,valuedate,pricetype,feedcode,datasourceid,priceid,ticksize,quoteunit,costbid,costask,costmid,exchbid,exchask,exchmid,cashbid,cashask,cashmid,impdate,imptime)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, obj.getCypairname(),obj.getUpdatedatetime(),obj.getCountnum(),obj.getQuotetype(),
        		obj.getProductname(),obj.getBasepoint(),obj.getTrademark(),obj.getQuotettl(),
        		obj.getBid(),obj.getAsk(),obj.getMid(),obj.getSourcebid(),
        		obj.getSourceask(),obj.getSourcemid(),obj.getRunstate(),obj.getValuedate(),
        		obj.getPricetype(),obj.getFeedcode(),obj.getDatasourceid(),obj.getPriceid(),
        		obj.getTicksize(),obj.getQuoteunit(),obj.getCostbid(),obj.getCostask(),
        		obj.getCostmid(),obj.getExchbid(),obj.getExchask(),obj.getExchmid(),
        		obj.getCashbid(),obj.getCashask(),obj.getCashmid(),obj.getImpdate(),
        		obj.getImptime()) > 0;
    }

    @Override
    public Boolean update(ReutersFxSpotRealrate obj) {
        String sql = "update reuters_fx_spot_realrate set countnum=?,quotetype=?,productname=?,basepoint=?,trademark=?,quotettl=?,bid=?,ask=?,mid=?,sourcebid=?,sourceask=?,sourcemid=?,runstate=?,valuedate=?,pricetype=?,feedcode=?,datasourceid=?,priceid=?,ticksize=?,quoteunit=?,costbid=?,costask=?,costmid=?,exchbid=?,exchask=?,exchmid=?,cashbid=?,cashask=?,cashmid=?,impdate=?,imptime=? where cypairname=? and updatedatetime=? ";
        return jdbcTemplate.update(sql,
        		obj.getCountnum(),obj.getQuotetype(),obj.getProductname(),obj.getBasepoint(),
        		obj.getTrademark(),obj.getQuotettl(),obj.getBid(),obj.getAsk(),
        		obj.getMid(),obj.getSourcebid(),obj.getSourceask(),obj.getSourcemid(),
        		obj.getRunstate(),obj.getValuedate(),obj.getPricetype(),obj.getFeedcode(),
        		obj.getDatasourceid(),obj.getPriceid(),obj.getTicksize(),obj.getQuoteunit(),
        		obj.getCostbid(),obj.getCostask(),obj.getCostmid(),obj.getExchbid(),
        		obj.getExchask(),obj.getExchmid(),obj.getCashbid(),obj.getCashask(),
        		obj.getCashmid(),obj.getImpdate(),obj.getImptime(),obj.getCypairname(),
        		obj.getUpdatedatetime()) > 0;
    }

    @Override
    public boolean delete(ReutersFxSpotRealrate obj) {
        String sql = "delete from reuters_fx_spot_realrate where cypairname=? and updatedatetime=? ";
        return jdbcTemplate.update(sql, obj.getCypairname(),obj.getUpdatedatetime()) > 0;

    }

    @Override
    public ReutersFxSpotRealrate locate(ReutersFxSpotRealrate obj) {
        String sql = "select * from reuters_fx_spot_realrate where cypairname=? and updatedatetime=? ";
        List<ReutersFxSpotRealrate> result = jdbcTemplate.query(sql, new Object[] { obj.getCypairname(),obj.getUpdatedatetime()},new BeanPropertyRowMapper<ReutersFxSpotRealrate>(ReutersFxSpotRealrate.class));
        if (result!=null&&result.size()>0) {
        	return result.get(0);
		}else {
			return null;
		}
    }
}
