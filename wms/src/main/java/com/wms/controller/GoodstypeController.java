package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goodstype;
import com.wms.entity.Storage;
import com.wms.entity.User;
import com.wms.service.GoodstypeService;
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
 * @since 2023-03-02
 */
@RestController
@RequestMapping("/goodstype")
public class GoodstypeController {
    @Autowired
    private GoodstypeService goodstypeService;
    @PostMapping("/save")
    public Result save(@RequestBody Goodstype goodstype){
        return goodstypeService.save(goodstype) ? Result.successs():Result.fail();
    }
    @PostMapping("/update")
    public Result update(@RequestBody Goodstype goodstype){
        return goodstypeService.updateById(goodstype) ? Result.successs():Result.fail();
    }
    @GetMapping("/del")
    public Result del(@RequestParam String id){

        return goodstypeService.removeById(id) ? Result.successs():Result.fail();
    }
    @PostMapping("/listPage")
    public Result listP(@RequestBody QueryPageParam queryPageParam){
        HashMap param = queryPageParam.getParam();
        Page<Goodstype> page = new Page<>();
        page.setCurrent(queryPageParam.getPageNum());
        page.setSize(queryPageParam.getPageSize());
        LambdaQueryWrapper<Goodstype> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank((String) param.get("name")) && !"null".equals((String) param.get("name"))){
            lambdaQueryWrapper.like(Goodstype::getName,(String) param.get("name"));
        }

        IPage result = goodstypeService.pageCC(page,lambdaQueryWrapper);

        return Result.successs(result.getRecords(), result.getTotal());
    }
    @GetMapping("/list")
    public Result list(){
        List list = goodstypeService.list();
        return Result.successs(list);
    }
}
