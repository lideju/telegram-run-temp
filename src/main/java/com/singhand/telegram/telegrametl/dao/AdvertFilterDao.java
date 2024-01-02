package com.singhand.telegram.telegrametl.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 广告过滤关键词(AdvertFilter)表数据库访问层
 *
 * @author lideju
 * @since 2023-06-26 11:08:25
 */
@Mapper
public interface AdvertFilterDao {

    @Update("update public_ip set ip = #{ip} where id = 1")
    void updateIp(@Param("ip") String ip);

    @Update("update public_ip set ip = #{ip} where id = 2")
    void updateIp2(@Param("ip") String ip);

}

