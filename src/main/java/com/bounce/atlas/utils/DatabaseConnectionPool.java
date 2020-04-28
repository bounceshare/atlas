package com.bounce.atlas.utils;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class DatabaseConnectionPool implements ConnectionPool {

    private final static Logger logger = Log.getLogger(DatabaseConnectionPool.class.getName());

    private String url;
    private String username;
    private String password;
    private List<Connection> connectionPool = new CopyOnWriteArrayList<>();
    private List<Connection> connectionsToRemove =  new CopyOnWriteArrayList<>();
    public static int INITIAL_POOL_SIZE = 5;
    public static int MAX_POOL_SIZE = 5;
    public static int DB_CONNECTION_RATE_PER_CONN = 100;

    protected DatabaseConnectionPool(String url, String user, String password) throws SQLException {
        this.url = url;
        this.username = user;
        this.password = password;
        addInitialConnections();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getRandomElementFromList();
    }

    private void addInitialConnections() throws SQLException {
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            this.connectionPool.add(createConnection());
        }
    }

    private Connection getRandomElementFromList() throws SQLException {
        if (!connectionPool.isEmpty()) {
            Random rand = new Random();
            return connectionPool.get(rand.nextInt(connectionPool.size()));
        }
        addInitialConnections();
        return getConnection();
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        try {
            logger.info("Releasing connection : " + getUrl());
            connectionPool.removeIf(conn -> conn.equals(connection));
            connection.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Utils.logError(e);
        }
        return false;
    }

    @Override
    public void releaseConnections() {
        try {
            logger.info("Releasing all connections for db : " + getUrl());
            Iterator<Connection> iterator = connectionPool.iterator();
            while (iterator.hasNext()) {
                Connection connection = iterator.next();
                releaseConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.logError(e);
        }
    }

    @Override
    public void releaseStaleConnections() {
        logger.info("releaseStaleConnections()");
        try {
            Iterator<Connection> iterator = connectionsToRemove.iterator();
            for(Connection connection  : connectionsToRemove) {
                connection.close();
                connectionsToRemove.remove(connection);
            }
        } catch (Exception e) {
            Utils.logError(e);
            e.printStackTrace();
        }
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    private synchronized Connection createConnection()
            throws SQLException {
        logger.info("Adding a new connetion : " + getUrl());
        return DriverManager.getConnection(url, username, password);
    }

    public Integer getConnectionPoolSize() {
        return connectionPool.size();
    }

}
