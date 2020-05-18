package com.wangxy.exoskeleton.mapper;

import com.wangxy.exoskeleton.entity.Pagelable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

public interface PagelableMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TSYS_PAGELABLE
     *
     * @mbggenerated Mon May 18 01:19:17 CST 2020
     */
    int deleteByPrimaryKey(@Param("pageId") String pageId, @Param("lableId") String lableId, @Param("lang") String lang);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TSYS_PAGELABLE
     *
     * @mbggenerated Mon May 18 01:19:17 CST 2020
     */
    int insert(Pagelable record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TSYS_PAGELABLE
     *
     * @mbggenerated Mon May 18 01:19:17 CST 2020
     */
    int insertSelective(Pagelable record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TSYS_PAGELABLE
     *
     * @mbggenerated Mon May 18 01:19:17 CST 2020
     */
    Pagelable selectByPrimaryKey(@Param("pageId") String pageId, @Param("lableId") String lableId, @Param("lang") String lang);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TSYS_PAGELABLE
     *
     * @mbggenerated Mon May 18 01:19:17 CST 2020
     */
    int updateByPrimaryKeySelective(Pagelable record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TSYS_PAGELABLE
     *
     * @mbggenerated Mon May 18 01:19:17 CST 2020
     */
    int updateByPrimaryKey(Pagelable record);
}