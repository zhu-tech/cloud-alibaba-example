package com.neyogoo.example.common.shardingsphere.seata;

import io.seata.core.context.RootContext;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeataTransactionHolder {


    private static final ThreadLocal<GlobalTransaction> CONTEXT = new ThreadLocal<>();

    static void set(final GlobalTransaction transaction) {
        CONTEXT.set(transaction);
    }

    static GlobalTransaction get() {
        GlobalTransaction globalTransaction = CONTEXT.get();
        if (globalTransaction == null && RootContext.getXID() != null) {
            globalTransaction = GlobalTransactionContext.getCurrent();
            CONTEXT.set(globalTransaction);
        }
        return globalTransaction;
    }

    static void clear() {
        CONTEXT.remove();
    }
}
