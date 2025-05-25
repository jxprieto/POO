package com.opensky;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {

    private static final String PROPERTIES_FILE = "db.properties";
    private static final int POOL_SIZE = 5;
    private static final List<Connection> pool = new ArrayList<>(POOL_SIZE);
    private static final List<Connection> usedConnections = new ArrayList<>();

    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            initializeDatabaseProperties();
            for (int i = 0; i < POOL_SIZE; i++) {
                pool.add(createConnection());
            }
        } catch (SQLException e) {
            throw new ExceptionInInitializerError("Failed to initialize database connections: " + e.getMessage());
        }
    }

    private static void initializeDatabaseProperties() throws SQLException {
        try (InputStream input = Database.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            Properties props = new Properties();
            props.load(input);

            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");

        } catch (Exception e) {
            throw new SQLException("Error loading database configuration", e);
        }
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (pool.isEmpty()) throw new SQLException("No hay conexiones disponibles");

        Connection connection = pool.removeLast();
        if (!connection.isValid(2)) connection = createConnection();

        usedConnections.add(connection);
        return connection;
    }

    public static synchronized void releaseConnection(Connection connection) {
        usedConnections.remove(connection);
        pool.add(connection);
    }
    public static synchronized void closeAllConnections() {
        usedConnections.forEach(conn -> {
            try { conn.close(); } catch (SQLException ignored) {}
        });
        pool.forEach(conn -> {
            try { conn.close(); } catch (SQLException ignored) {}
        });
        usedConnections.clear();
        pool.clear();
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

}