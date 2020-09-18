package com.netty.websocket.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netty.websocket.common.dto.CommonReturn;
import com.netty.websocket.common.service.BaseService;
import com.netty.websocket.tools.ConvertUtils;
import com.netty.websocket.tools.my.MyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 浮桥的Service层实现类(方法围绕DTO)
 *
 * M 为 DAO 层
 *
 * T 为 Entity
 *
 * D 为 DTO
 *
 * @author DNYY
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T> , T , D> extends DeepServiceImpl<M,T> implements BaseService<T , D> {

    //获得T(Entity)的类名
    protected Class<T> currentEntityClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }

    protected Class<D> currentDtoClass(){
        return (Class<D>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }

    @Value("${update.root.path}")
    private String fileRootPath;

    /**
     * 保存方法
     * @param dto
     */
    @Override
    public void insert(D dto) {
        //保存方法前使用的方法
        beforeInsert(dto);
        T data = ConvertUtils.convert(dto,currentEntityClass());
        this.save(data);
    }

    /**
     * 通过id查找一条数据
     * @param id
     * @return
     */
    @Override
    public D selectById(Serializable id){
        T data = this.getById(id);
        D d = ConvertUtils.convert(data,currentDtoClass());
        return d;
    }

    /**
     * 根据许多id来查找多条数据
     * @param ids
     * @return
     */
    @Override
    public List<D> selectByIds(List<Serializable> ids){
        List<T> data = this.listByIds(ids);
        List<D> nmsl = new ArrayList<>();
        for (T entity : data) {
            D d = ConvertUtils.convert(entity,currentDtoClass());
            nmsl.add(d);
        }
        return nmsl;
    }

    /**
     * 修改方法
     * @param dto
     */
    @Override
    public void edit(D dto){
        //编辑方法前的前置方法
        beforEedit(dto);
        T data = ConvertUtils.convert(dto,currentEntityClass());
        this.updateById(data);
    }


    /**
     * 根据id来删除单条数据
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(Serializable id){
        Boolean flag = this.removeById(id);
        return flag;
    }

    /**
     * 根据许多id来删除多条数据
     * @param ids
     * @return
     */
    @Override
    public Boolean deleteByIds(List<Serializable> ids){
        Boolean flag = this.removeByIds(ids);
        return flag;
    }

    /**
     * 自定义查找方法
     * @param wrapper
     * @return
     */
    @Override
    public List<D> select(QueryWrapper<T> wrapper){
        List<T> list = this.list(wrapper);
        List<D> dList = ConvertUtils.convert(list, currentDtoClass());
        return dList;
    }

    /**
     * mybatisPlus分页方法
     * @param page
     * @return
     */
    @Override
    public List<D> selectPage(Object page,Object pageSize, QueryWrapper<T> queryWrapper){
        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        int Page,PageSize;
        try{
            Page = (int) page;
            PageSize = (int) pageSize;
        }catch (Exception e){
            page = 1;
            pageSize = 10;
        }
        IPage<T> Ipage = new Page<>((int)page, (int)pageSize);
        //获取页数
        baseMapper.selectPage(Ipage,queryWrapper);
        List<T> pages = Ipage.getRecords();
        List<D> date = ConvertUtils.convert(pages,currentDtoClass());
        return date;
    }

    @Override
    public D selectOne(QueryWrapper<T> wrapper) {
        //单条查询，如果查询到多条数据不抛出错误 true：抛出错误 false：不抛出错误
        T data = this.getOne(wrapper,false);
        D d = ConvertUtils.convert(data,currentDtoClass());
        return d;
    }

    /**
     * 在添加方法之前执行的方法
     * @param dto
     * @return dto
     */
    public abstract void beforeInsert(D dto);

    /**
     * 在编辑之前执行的方法
     * @param dto
     */
    public abstract void beforEedit(D dto);

    /**
     * 公共方法直接拖走
     * @param files 文件
     * @param userId 用户id
     * @param white 白名单
     * @param address 存放地址
     * @return
     */
    @Override
    public CommonReturn upLoad(MultipartFile[] files, String userId, String[] white, String address) {
        CommonReturn result = new CommonReturn();
        //记录多张图片的上传地址
        List<String> addresss = new ArrayList<>();
        //判断有没有传入white(白名单)
        List<String> list = null;
        if (white==null || white.length==0){
            list = null;
        }else{
            list = Arrays.asList(white);
        }
        //判断参数是否为空
        if(userId==null){
            result.setAll(10001,null,"参数错误");
            return result;
        }
        String lastName = "";
        String originalFilename = "";
        //多文件上传
        for(MultipartFile file : files){
            //获取文件类型  "image/jpeg"  "image/png"
//            String type = file.getContentType();
            //拿取文件的后缀名
            lastName = MyUtils.getlastName(file.getOriginalFilename(),".");
            if (lastName==null){
                result.setAll(10001,null,"文件名异常");
                return result;
            }
            if (list==null || list.size()==0 || list.contains(lastName)){
                //设置文件名
                originalFilename = userId + System.currentTimeMillis();
                //存储路径  fileRootPath 服务器前缀  address 具体文件夹名  originalFilename文件名   lastName文件后缀
                String filePath = new StringBuilder(fileRootPath).append(address).append(originalFilename).append(".").append(lastName).toString();
                //保存文件
                try {
                    file.transferTo(new File(filePath));
                }catch (IOException e){
                    e.printStackTrace();
                    result.setAll(40000,null,"上传失败");
                    return result;
                }
            }else{
                result.setAll(10001,addresss,"请按照指定的格式上传文件");
                this.deleteFile(addresss);
                return result;
            }
            addresss.add(new StringBuilder("").append(address).append(originalFilename).append(".").append(lastName).toString());
        }
        result.setAll(20000,addresss,"操作成功");
        return result;
    }

    /**
     * 公共方法直接拖走
     * @param address
     * @return
     */
    @Override
    public CommonReturn deleteFile(List<String> address) {
        CommonReturn result = new CommonReturn();
        String img_path = null;
        if (address==null || address.size()==0){
            return null;
        }
        for (int i = 0 ; i < address.size() ; i++){
            if (address.get(i)!=null){
                img_path = fileRootPath + address.get(i);
                //找到文件，初始化文件
                File file = new File(img_path);
                //文件是否存在
                if (file.exists()) {
                    //存在就删了
                    if (file.delete()) {
                        address.remove(i);
                    }
                }else{
                    address.remove(i);
                }
            }
        }
        result.setAll(20000,address,"还有以下文件没有删除");
        return result;
    }

}
