package com.netty.websocket.main.dao;

import com.netty.websocket.common.dao.BaseDao;
import com.netty.websocket.main.entity.BirdMsg;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BirdMsgMapper extends BaseDao<BirdMsg> {
    int deleteByPrimaryKey(String id);

    int insert(BirdMsg record);

    int insertSelective(BirdMsg record);

    BirdMsg selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BirdMsg record);

    int updateByPrimaryKey(BirdMsg record);
}