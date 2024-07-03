package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.service.GoodsService;
import com.wms.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2023-03-03
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;
    @Autowired
    private GoodsService goodsService;
    @PostMapping("/listPage")
    public Result listP(@RequestBody QueryPageParam queryPageParam){
        HashMap param = queryPageParam.getParam();
        String name = (String) param.get("name");
        String storage = (String) param.get("storage");
        String goodstype = (String) param.get("goodstype");
        //记录管理的权限控制roleId ，userId
        String roleId = (String) param.get("roleId");
        String userId = (String) param.get("userId");

        System.out.println(userId);
        Page<Record> page = new Page<>();
        page.setCurrent(queryPageParam.getPageNum());
        page.setSize(queryPageParam.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(" a.goods=b.id and b.storage=c.id and b.goodsType=d.id");
        if ("2".equals(roleId)){
           // queryWrapper.eq(Record::getUserid, userId);
            queryWrapper.apply("a.userId= "+userId);
        }

        if (StringUtils.isNotBlank(name) && !"null".equals(name)){
            queryWrapper.like("b.name",name);
        }
        if (StringUtils.isNotBlank(storage) && !"null".equals(storage)){
            queryWrapper.eq("c.id",storage);
        }
        if (StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)){
            queryWrapper.eq("d.id",goodstype);
        }

        IPage result = recordService.pageCC(page,queryWrapper);

        return Result.successs(result.getRecords(), result.getTotal());
    }
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record){
        Goods goods = goodsService.getById(record.getGoods());
        record.setCreatetime( LocalDateTime.now());//插入出入库的时间
        int n = record.getCount();
        //出库
        if("2".equals(record.getAction())){
            n = -n;
            record.setCount(n);
        }
       /* if(record.getCount() >goods.getCount() ){
            System.out.println(record.getCount());
            System.out.println(goods.getCount() );
            return Result.fail("仓库物品数量不够，请重新输入数量！");
        }*/
        if((goods.getCount() + n) < 0){
            return Result.fail("仓库物品剩余"+goods.getCount()+"件");
        }else {
            //入库
            int num = goods.getCount() + n;
            goods.setCount(num);
            goodsService.updateById(goods);
            return recordService.save(record) ? Result.successs():Result.fail();

        }
            }
}
