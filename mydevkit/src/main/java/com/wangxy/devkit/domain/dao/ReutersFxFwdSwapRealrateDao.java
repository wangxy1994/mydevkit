package com.wangxy.devkit.domain.dao;

import com.wangxy.devkit.domain.bean.ReutersFxFwdSwapRealrate;

public interface ReutersFxFwdSwapRealrateDao {
    /**
     * 向数据库中保存一条实时行情
     *
     * @param fxForwardSwapRealrate 要保存的实时行情对象
     * @return 是否增加成功
     */
    Boolean add(ReutersFxFwdSwapRealrate fxForwardSwapRealrate);

    /**
     * 更新数据库中的一条实时行情
     *
     * @param fxForwardSwapRealrate 要更新的实时行情对象
     * @return 是否更新成功
     */
    Boolean update(ReutersFxFwdSwapRealrate fxForwardSwapRealrate);

    /**
     * 删除一条指定的实时行情
     * @param fxForwardSwapRealrate 要更新的实时行情对象
     * @return
     */
    boolean delete(ReutersFxFwdSwapRealrate fxForwardSwapRealrate);

    /**
     * 精确查询一条指定的实时行情
     * @param fxForwardSwapRealrate 要更新的实时行情对象	
     * @return
     */
    ReutersFxFwdSwapRealrate locate(ReutersFxFwdSwapRealrate fxForwardSwapRealrate);

}