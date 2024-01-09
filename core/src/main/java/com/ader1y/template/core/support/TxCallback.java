package com.ader1y.template.core.support;

import org.springframework.transaction.support.TransactionSynchronization;

public class TxCallback implements TransactionSynchronization {

    private final Runnable runnable;

    public TxCallback(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void afterCommit() {
        this.runnable.run();

    }

}
