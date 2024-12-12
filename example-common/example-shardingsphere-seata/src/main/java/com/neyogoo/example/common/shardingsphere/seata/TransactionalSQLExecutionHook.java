package com.neyogoo.example.common.shardingsphere.seata;

import io.seata.core.context.RootContext;
import org.apache.shardingsphere.infra.database.core.connector.ConnectionProperties;
import org.apache.shardingsphere.infra.executor.sql.hook.SQLExecutionHook;

import java.util.List;

public class TransactionalSQLExecutionHook implements SQLExecutionHook {

    private boolean seataBranch;

    @Override
    public void start(String dataSourceName, String sql, List<Object> params, ConnectionProperties connectionProps,
                      boolean isTrunkThread) {
        if (isTrunkThread) {
            if (RootContext.inGlobalTransaction()) {
                SeataXIDContext.set(RootContext.getXID());
            }
        } else if (!RootContext.inGlobalTransaction() && !SeataXIDContext.isEmpty()) {
            RootContext.bind(SeataXIDContext.get());
            seataBranch = true;
        }


        if (isTrunkThread) {
            if (RootContext.inGlobalTransaction()) {
                SeataXIDContext.set(RootContext.getXID());
            }
        } else if (!RootContext.inGlobalTransaction() && !SeataXIDContext.isEmpty()) {
            RootContext.bind(SeataXIDContext.get());
            seataBranch = true;
        }
    }

    @Override
    public void finishSuccess() {
        if (seataBranch) {
            RootContext.unbind();
        }
    }

    @Override
    public void finishFailure(final Exception cause) {
        if (seataBranch) {
            RootContext.unbind();
        }
    }
}
