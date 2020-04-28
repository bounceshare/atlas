package com.bounce.atlas.utils;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseConnector {

    private static final int dbPoolSize = 1; //TODO : Move to ZK config
    private static List<DatabaseConnector> databaseConnectorsList;
    private final static Logger logger = Log.getLogger(DatabaseConnector.class.getName());

    private Map<String, DatabaseConnectionPool> dbPoolMap = new ConcurrentHashMap<>();

    public void init() throws Exception {
//        RealtimeData.get().getScheduledExecutorService().scheduleAtFixedRate(new DatabaseConnector.StaleDatabaseConnectionCheckTask(), 5, 5, TimeUnit.MINUTES);
    }

    public void init(String url, String username, String password) throws Exception {
        DSLContext context = getConnector(url, username, password);
        if(context == null) {
            throw new Exception("Can't initilize the database : " + url);
        }
    }

    protected DatabaseConnector() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.logError(e);
        }
    }

    public synchronized static DatabaseConnector getDb() {
        if (databaseConnectorsList == null || databaseConnectorsList.isEmpty()) {
            databaseConnectorsList = new ArrayList<>();
            for (int i = 0; i < dbPoolSize; i++) {
                DatabaseConnector instance;
                instance = new DatabaseConnector();
                databaseConnectorsList.add(instance);
            }
        }
        return getRandomElementFromList(databaseConnectorsList);
    }

    public DSLContext getConnector(String url, String username, String password) {
        DSLContext dslContext;
        DatabaseConnectionPool dbPool;
        dbPool = dbPoolMap.get(url);
        try {
            if(dbPool == null) {
                dbPool = new DatabaseConnectionPool(url, username, password);
                dbPoolMap.put(url, dbPool);
            }
            Connection conn = dbPool.getConnection();
            dslContext = DSL.using(conn, SQLDialect.POSTGRES_9_5);
            if (!conn.isClosed()) {
                return dslContext;
            }
        } catch (SQLException e) {
            Utils.logError(e);
            e.printStackTrace();
        }
        return null;
    }

    static void destroy() {
        try {
            for (DatabaseConnector databaseConnector : databaseConnectorsList) {
                for (Map.Entry<String, DatabaseConnectionPool> entry  : databaseConnector.dbPoolMap.entrySet()) {
                    logger.info("destroy() : " + entry.getKey());
                    entry.getValue().releaseConnections();
                }
            }
            databaseConnectorsList = null;
        } catch (Exception e) {
            Utils.logError(e);
            e.printStackTrace();
        }
    }

    private static DatabaseConnector getRandomElementFromList(List<DatabaseConnector> arrayList) {
        if (!arrayList.isEmpty()) {
            Random rand = new Random();
            return arrayList.get(rand.nextInt(arrayList.size()));
        }
        return getDb();
    }

    public Map<String, Integer> getDbState() {
        Map<String, Integer> states = new HashMap<>();
        for (Map.Entry<String, DatabaseConnectionPool> entry  : dbPoolMap.entrySet()) {
            states.put(entry.getKey(), entry.getValue().getConnectionPoolSize());
        }
        return states;
    }

}
