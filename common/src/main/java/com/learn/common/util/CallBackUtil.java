package com.learn.common.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author 张伟
 * @projectName supplier-common
 * @title CallBack
 * @package com.xiaoniu.supplier.common.utils.transaction
 * @description 事务提交前置或者后置操作
 * @date 2020/12/10 11:08
 */
@Component
@Log4j2
public class CallBackUtil {

    /**
     * 事务提交后执行的方法
     * @param callBackFunction 需要执行的方法，lambda
     * @param async 是否需要异步执行
     */
    public void afterExecute(CallBackFunction callBackFunction, boolean async){
        //判断当前线程是否有存活的事务
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            // 注册一个事务适配器，重写事务提交后的方法
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    if (async) {
                        log.trace("事务提交过后，开始异步执行");
                        callBackFunction.asyncCallBack();
                    } else {
                        log.trace("事务提交过后，开始同步执行");
                        callBackFunction.callBackFun();
                    }

                }
            });
        }
    }
}
