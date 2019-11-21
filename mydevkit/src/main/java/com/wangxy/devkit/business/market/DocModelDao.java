package com.wangxy.devkit.business.market;

import java.util.List;

public interface DocModelDao {
    /**
     * 向数据库中保存一个新用户
     *
     * @param docModel 要保存的用户对象
     * @return 是否增肌成功
     */
    Boolean add(DocModel docModel);

    /**
     * 更新数据库中的一个用户
     *
     * @param docModel 要更新的用户对象
     * @return 是否更新成功
     */
    Boolean update(DocModel docModel);

    /**
     * 删除一个指定的用户
     *
     * @param id 要删除的用户的标识
     * @return 是否删除成功
     */
    boolean delete(Long id);

    /**
     * 精确查询一个指定的用户
     *
     * @param id 要查询的用户的标识
     * @return 如果能够查询到，返回用户信息，否则返回 null
     */
    DocModel locate(Long id);

    /**
     * 通过名称模糊查询用户
     *
     * @param name 要模糊查询的名称
     * @return 查询到的用户列表
     */
    List<DocModel> matchName(String name);
}