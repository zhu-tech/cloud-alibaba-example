package com.neyogoo.example.common.shardingsphere.seata;

import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.core.rpc.netty.RmNettyRemotingClient;
import io.seata.core.rpc.netty.TmNettyRemotingClient;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.SneakyThrows;
import org.apache.shardingsphere.infra.database.core.type.DatabaseType;
import org.apache.shardingsphere.transaction.api.TransactionType;
import org.apache.shardingsphere.transaction.spi.ShardingSphereTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SeataATShardingSphereTransactionManager implements ShardingSphereTransactionManager {

    private final Map<String, DataSource> dataSourceMap = new HashMap<>();

    @Override
    public void init(Map<String, DatabaseType> databaseTypes, Map<String, DataSource> dataSources,
                     String providerType) {
        this.dataSourceMap.putAll(dataSources);
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.BASE;
    }

    @Override
    public boolean isInTransaction() {
        return null != RootContext.getXID();
    }

    @Override
    public Connection getConnection(String databaseName, String dataSourceName) throws SQLException {
        return this.dataSourceMap.get(databaseName + "." + dataSourceName).getConnection();
    }

    @Override
    public void begin() {
        this.begin(60);
    }

    @Override
    @SneakyThrows(TransactionException.class)
    public void begin(int timeout) {
        if (timeout < 0) {
            throw new TransactionException("timeout should more than 0s");
        }
        GlobalTransaction globalTransaction = GlobalTransactionContext.getCurrentOrCreate();
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        if (transactionName != null) {
            globalTransaction.begin(timeout * 1000, transactionName);
        } else {
            globalTransaction.begin(timeout * 1000);
        }
        SeataTransactionHolder.set(globalTransaction);
    }

    @Override
    @SneakyThrows(TransactionException.class)
    public void commit(boolean rollbackOnly) {
        try {
            SeataTransactionHolder.get().commit();
        } finally {
            SeataTransactionHolder.clear();
            if (RootContext.getXID() != null) {
                RootContext.unbind();
            }
        }
    }

    @Override
    @SneakyThrows(TransactionException.class)
    public void rollback() {
        try {
            SeataTransactionHolder.get().rollback();
        } finally {
            SeataTransactionHolder.clear();
            if (RootContext.getXID() != null) {
                RootContext.unbind();
            }
        }
    }

    @Override
    public void close() {
        dataSourceMap.clear();
        SeataTransactionHolder.clear();
        RmNettyRemotingClient.getInstance().destroy();
        TmNettyRemotingClient.getInstance().destroy();
    }

    @Override
    public Object getType() {
        return "ZooKeeper";
    }
}
