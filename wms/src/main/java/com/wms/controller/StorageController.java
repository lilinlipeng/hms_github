package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Menu;
import com.wms.entity.Storage;
import com.wms.entity.User;
import com.wms.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2023-03-01
 */
@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;
    @PostMapping("/save")
    public Result save(@RequestBody Storage storage){
        return storageService.save(storage) ? Result.successs():Result.fail();
    }
    @PostMapping("/update")
    public Result update(@RequestBody Storage storage){
        return storageService.updateById(storage) ? Result.successs():Result.fail();
    }
    @GetMapping("/del")
    public Result del(@RequestParam String id){

        return storageService.removeById(id) ? Result.successs():Result.fail();
    }
    @PostMapping("/listPage")
    public Result listP(@RequestBody QueryPageParam queryPageParam){
        HashMap param = queryPageParam.getParam();
        String name = (String) param.get("name");
        Page<Storage> page = new Page<>();
        page.setCurrent(queryPageParam.getPageNum());
        page.setSize(queryPageParam.getPageSize());
        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Storage::getName,name);
        }

        IPage result = storageService.pageCC(page,lambdaQueryWrapper);

        return Result.successs(result.getRecords(), result.getTotal());
    }

    @GetMapping("/list")
    public Result list(){
        List list = storageService.list();
        return Result.successs(list);
    }
    //ok
    //加载应该注释
}
