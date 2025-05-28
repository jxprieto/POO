package com.opensky.repository.sql.utils;

import com.opensky.utils.Database;

import java.sql.Connection;
import java.sql.SQLException;

public final class SQLConnectionManager {

    @FunctionalInterface
    public interface SQLFunction<T> {
        T apply(Connection conn) throws SQLException;
    }

    @FunctionalInterface
    public interface VoidSQLFunction {
        void apply(Connection conn) throws SQLException;
    }

    public static <T> T withConnection(SQLFunction<T> action, String errorMessage) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            return action.apply(conn);
        } catch (SQLException e) {
            throw new RuntimeException(errorMessage, e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
    }


    public static void withConnection(VoidSQLFunction action, String errorMessage) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            action.apply(conn);
        } catch (SQLException e) {
            throw new RuntimeException(errorMessage, e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
    }

    public static void withConnectionTransactional(VoidSQLFunction action, String errorMessage) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);
            action.apply(conn);
        } catch (SQLException e) {
            throw new RuntimeException(errorMessage, e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
    }
}
