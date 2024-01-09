package com.ader1y.template.core.support;

import org.springframework.lang.NonNull;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class SpringUtil {

    private SpringUtil(){}

    /**
     * 功能：事务回调<tr/>
     * 执行顺序：beforeCommit —> beforeCompletion —> <b> !afterCommit </b> —> afterCompletion
     */
    public static void callBackAfterCommit(@NonNull TxCallback callbackWorker){
        if (TransactionSynchronizationManager.isActualTransactionActive()){
            TransactionSynchronizationManager.registerSynchronization(callbackWorker);
        }
    }

}
