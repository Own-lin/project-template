package com.ader1y.template.core.support;

import org.springframework.lang.NonNull;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class SpringUtil {

    private SpringUtil(){}

    /**
     * 事务回调工具<tr/>
     * 在事务commit后执行
     */
    public static void callBackAfterCommit(@NonNull TxCallback callbackWorker){
        if (TransactionSynchronizationManager.isActualTransactionActive()){
            TransactionSynchronizationManager.registerSynchronization(callbackWorker);
        }
    }

}
