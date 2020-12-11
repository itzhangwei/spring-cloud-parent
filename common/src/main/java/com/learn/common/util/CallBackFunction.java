package com.learn.common.util;

import com.xiaoniu.supplier.common.utils.Constant;

import java.util.concurrent.CompletableFuture;

/**
 * @author 张伟
 * @projectName supplier-common
 * @title CallBackFunction
 * @package com.xiaoniu.supplier.common.utils.transaction
 * @description 事务提交前后回调接口类
 * @date 2020/12/10 11:17
 *
 * FunctionalInterface 表明此类是一个函数式接口类，只用有一个抽象方法,其余方法需要用default修饰，实现方法
 */
@FunctionalInterface
public interface CallBackFunction {

    /**
     * 回调函数
     */
    void callBackFun();

    /**
     * 异步只想回调方法
     * @return 常量字符串success
     */
    default CompletableFuture<String> asyncCallBack(){
        return CompletableFuture.supplyAsync(()->{
            this.callBackFun();
            return Constant.SUCCESS;
        });
    }


}
