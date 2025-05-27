package com.opensky.repository;

import com.opensky.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLRepository {

    @FunctionalInterface
    public interface SQLFunction<T> {
        T apply(Connection conn) throws SQLException;
    }

    static <T> T withConnection(SQLFunction<T> action, String errorMessage) {
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

    @FunctionalInterface
    public interface VoidSQLFunction {
        void apply(Connection conn) throws SQLException;
    }

    static void withConnection(VoidSQLFunction action, String errorMessage) {
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
}
