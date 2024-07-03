package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Menu;
import com.wms.entity.User;
import com.wms.service.MenuService;
import com.wms.service.UserService;
import freemarker.template.utility.StringUtil;
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
 * @since 2023-02-21
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List<User> list(){
       return userService.list();
    }

    @PostMapping("/save")
    public Result save(@RequestBody User user){
        return userService.save(user) ? Result.successs():Result.fail();
    }

    @PostMapping("/mod")
    public boolean mod(@RequestBody User user){
        return userService.updateById(user);
    }

    @PostMapping("/saveOrmod")
    public boolean saveOrmod(@RequestBody User user){
        return userService.saveOrUpdate(user);
    }

    @GetMapping("/delete")
    public boolean delete(Integer id){
        return userService.removeById(id);
    }

    @PostMapping("/listP")
    public Result list(@RequestBody User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(user.getName())){
            lambdaQueryWrapper.like(User::getName,user.getName());
        }
      return Result.successs(userService.list(lambdaQueryWrapper));
    }

    @PostMapping("/listPage")
    public List<User> listP(@RequestBody QueryPageParam queryPageParam){

        HashMap param = queryPageParam.getParam();



        Page<User> page = new Page<>();
        page.setCurrent(queryPageParam.getPageNum());
        page.setSize(queryPageParam.getPageSize());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName,(String) param.get("name"));

        IPage result = userService.page(page,lambdaQueryWrapper);
        System.out.println(result.getTotal());
        return result.getRecords();
    }

    @PostMapping("/listPageC")
    public Result listPC(@RequestBody QueryPageParam queryPageParam){

        HashMap param = queryPageParam.getParam();


        Page<User> page = new Page<>();
        page.setCurrent(queryPageParam.getPageNum());
        page.setSize(queryPageParam.getPageSize());
       LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
       if (StringUtils.isNotBlank((String) param.get("name")) && !"null".equals((String) param.get("name"))){
           lambdaQueryWrapper.like(User::getName,(String) param.get("name"));
       }
        if (StringUtils.isNotBlank((String) param.get("sex"))){
            lambdaQueryWrapper.eq(User::getSex,(String) param.get("sex"));
        }
        if (StringUtils.isNotBlank((String) param.get("roleId"))){
            lambdaQueryWrapper.eq(User::getRoleId,(String) param.get("roleId"));
        }




        IPage result = userService.pageCC(page,lambdaQueryWrapper);
        System.out.println(result.getTotal());
        return Result.successs(result.getRecords(), result.getTotal());
    }

    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no){
      List list = userService.lambdaQuery().eq(User::getNo,no).list();
      return list.size()>0?Result.successs(list):Result.fail();
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user){
        return userService.updateById(user) ? Result.successs():Result.fail();
    }

    @GetMapping("/del")
    public Result del(@RequestParam String id){

        return userService.removeById(id) ? Result.successs():Result.fail();
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        List list = userService.lambdaQuery()
                .eq(User::getNo,user.getNo())
                .eq(User::getPassword,user.getPassword()).list();

        if (list.size()>0){
           User user1 = (User)list.get(0);
            List MenuList = menuService.lambdaQuery().like(Menu::getMenuright,user1.getRoleId()).list();
            HashMap res = new HashMap();
            res.put("user",user1);
            res.put("menu",MenuList);
            return Result.successs(res);
        }
        return Result.fail();
    }
}
