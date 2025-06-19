package com.opensky.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {

    private static final Object lock = new Object();
    private static final String PROPERTIES_FILE = "db.properties";
    private static final int POOL_SIZE = 5;
    private static final List<Connection> pool = new ArrayList<>(POOL_SIZE);

    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            initializeDatabaseProperties();
            for (int i = 0; i < POOL_SIZE; i++)
                pool.add(createConnection());
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

    public static Connection getConnection() throws SQLException {
        synchronized (lock) {
            while (pool.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new SQLException("Thread interrupted while waiting for a connection", e);
                }
            }
            Connection connection = pool.removeLast();
            if (!connection.isValid(2)) connection = createConnection();

            return connection;
        }
    }

    public static void releaseConnection(Connection connection) {
        synchronized (lock){
            pool.add(connection);
            lock.notify();
        }
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

}