package com.netty.websocket.main.dao;

import com.netty.websocket.common.dao.BaseDao;
import com.netty.websocket.main.entity.BirdUsers;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BirdUsersMapper extends BaseDao<BirdUsers> {
    int deleteByPrimaryKey(String id);

    int insert(BirdUsers record);

    int insertSelective(BirdUsers record);

    BirdUsers selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BirdUsers record);

    int updateByPrimaryKey(BirdUsers record);
}