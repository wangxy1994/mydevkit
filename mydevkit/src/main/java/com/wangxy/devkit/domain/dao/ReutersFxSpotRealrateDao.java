package com.wangxy.devkit.domain.dao;

import com.wangxy.devkit.domain.bean.ReutersFxSpotRealrate;

public interface ReutersFxSpotRealrateDao {
    /**
     * 向数据库中保存一条实时行情
     *
     * @param fxSpotRealrate 要保存的实时行情对象
     * @return 是否增加成功
     */
    Boolean add(ReutersFxSpotRealrate fxSpotRealrate);

    /**
     * 更新数据库中的一条实时行情
     *
     * @param fxSpotRealrate 要更新的实时行情对象
     * @return 是否更新成功
     */
    Boolean update(ReutersFxSpotRealrate fxSpotRealrate);

    /**
     * 删除一条指定的实时行情
     * @param fxSpotRealrate 要更新的实时行情对象
     * @return
     */
    boolean delete(ReutersFxSpotRealrate fxSpotRealrate);

    /**
     * 精确查询一条指定的实时行情
     * @param fxSpotRealrate 要更新的实时行情对象	
     * @return
     */
    ReutersFxSpotRealrate locate(ReutersFxSpotRealrate fxSpotRealrate);

}