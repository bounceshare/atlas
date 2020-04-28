package com.bounce.atlas.utils;

import java.sql.Connection;

public interface ConnectionPool {

    Connection getConnection() throws Exception;
    boolean releaseConnection(Connection connection);
    void releaseConnections();
    void releaseStaleConnections();
    String getUrl();
    String getUsername();
    String getPassword();

}