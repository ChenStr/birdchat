package com.netty.websocket.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netty.websocket.common.dto.CommonReturn;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 浮桥Service层 (方法基本围绕DTO)
 *
 * @author DNYY
 *
 * T为Entity类型
 *
 * D为DTO类型
 */
public interface BaseService<T,D> extends DeepService<T> {

    public void insert(D dto);

    public D selectById (Serializable id);

    public List<D> selectByIds(List<Serializable> ids);

    public void edit(D dto);

    public Boolean deleteById(Serializable id);

    public Boolean deleteByIds(List<Serializable> ids);

    public List<D> select(QueryWrapper<T> wrapper);

    public List<D> selectPage(Object page,Object pageSize, QueryWrapper<T> queryWrapper);

    public D selectOne(QueryWrapper<T> wrapper);

    /**
     * 文件上传
     * @param files 文件
     * @param userId 用户id
     * @param white 白名单
     * @param address 存放地址
     * @return
     */
    CommonReturn upLoad(MultipartFile[] files, String userId, String[] white, String address);

    /**
     * 批量文件删除
     * @param address
     * @return
     */
    CommonReturn deleteFile(List<String> address);
}
