package com.opensky;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class Database {

    private static final String PROPERTIES_FILE = "db.yml";

    public static Connection getConnection() throws SQLException {
        try (InputStream input = Database.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            Properties props = new Properties();
            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new SQLException("Error loading database configuration", e);
        }
    }
}