/**
 * FileName: CommonResult
 * Author:   jason
 * Date:     2021/1/6 21:53
 * Description:
 */
package com.zhangxujie.springcloud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //注在类上，提供类的get、set、equals、hashCode、canEqual、toString方法
@AllArgsConstructor //生成全参构造器
@NoArgsConstructor //生成无参构造器；
public class CommonResult<T> {

    //404, 200
    private Integer code;

    //success
    private String message;

    //返回数据
    private T data;

    public CommonResult(Integer code, String message){
        this(code, message, null);
    }

}
