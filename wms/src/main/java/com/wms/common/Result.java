package com.wms.common;

import lombok.Data;

@Data
public class Result {
    private int code;
    private String msg;
    private Long total;
    private Object data;
    public static Result fail(){
        return result(400,"失败",0L,null);
    }
    public static Result fail(String msg){
        return result(401,msg,0L,null);
    }

    public static Result successs(){
        return result(200,"成功",0L,null);
    }
    public static Result successs(Object data){
        return result(200,"成功",0L,data);
    }
    public static Result successs(Object data,Long total){
        return result(200,"成功",total,data);
    }

    private static Result result(int code,String msg,Long total,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setTotal(total);
        result.setData(data);
        return result;
    }
}
