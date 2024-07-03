package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Storage;
import com.wms.entity.User;
import com.wms.service.GoodsService;
import com.wms.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2023-03-02
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods){
        return goodsService.save(goods) ? Result.successs():Result.fail();
    }
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods){
        return goodsService.updateById(goods) ? Result.successs():Result.fail();
    }
    @GetMapping("/del")
    public Result del(@RequestParam String id){

        return goodsService.removeById(id) ? Result.successs():Result.fail();
    }
    @PostMapping("/listPage")
    public Result listP(@RequestBody QueryPageParam queryPageParam){
        HashMap param = queryPageParam.getParam();
        Page<Goods> page = new Page<>();
        //t
        page.setCurrent(queryPageParam.getPageNum());
        page.setSize(queryPageParam.getPageSize());
        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank((String) param.get("name")) && !"null".equals((String) param.get("name"))){
            lambdaQueryWrapper.like(Goods::getName,(String) param.get("name"));
        }
        if (StringUtils.isNotBlank((String) param.get("storage")) && !"null".equals((String) param.get("storage"))){
            lambdaQueryWrapper.eq(Goods::getStorage,(String) param.get("storage"));
        }
        if (StringUtils.isNotBlank((String) param.get("goodstype")) && !"null".equals((String) param.get("goodstype"))){
            lambdaQueryWrapper.eq(Goods::getGoodstype,(String) param.get("goodstype"));
        }

        IPage result = goodsService.pageCC(page,lambdaQueryWrapper);

        return Result.successs(result.getRecords(), result.getTotal());
    }
}
