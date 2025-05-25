package com.opensky.repository;

import com.opensky.Database;
import com.opensky.model.Client;
import com.opensky.utils.Dependency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepositoryImpl implements ClientRepository, Dependency {

    public static ClientRepositoryImpl createInstance() {
        return new ClientRepositoryImpl();
    }

    private static final String CREATE =
            "INSERT INTO clients (id, name, age, email, phone_number) " +
            "VALUES (default, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE clients SET name = ?, age = ?, email = ?, phone_number = ? " +
            "WHERE id = ?";

    private static final String READ = "SELECT id, name, age, email, phone_number " +
            "FROM clients " +
            "WHERE id = ?";

    private static final String DELETE = "DELETE FROM clients" +
            " WHERE id = ?";

    private static final String FIND_ALL = "SELECT id, name, age, email, phone_number " +
            "FROM clients";

    private static final String FIND_BY_EMAIL = "SELECT id, name, age, email, phone_number " +
            "FROM clients " +
            "WHERE email = ?";


    @Override
    public Client create(Client client) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            try (final PreparedStatement stmt = conn.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, client.getName());
                stmt.setObject(2, client.getAge(), Types.INTEGER);
                stmt.setString(3, client.getEmail());
                stmt.setString(4, client.getPhoneNumber());
                stmt.executeUpdate();

                try (final ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        String generatedId = keys.getString(1);
                        return client.toBuilder().id(generatedId).build();
                    }
                }
            }
            throw new RuntimeException("Error while retrieving client generated ID");
        } catch (SQLException e) {
            throw new RuntimeException("Error saving client", e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
    }

    @Override
    public Client update(Client client) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            final PreparedStatement stmt = conn.prepareStatement(UPDATE);

            stmt.setString(1, client.getName());
            stmt.setObject(2, client.getAge(), Types.INTEGER);
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhoneNumber());
            stmt.setString(5, client.getId());
            stmt.executeUpdate();
            return client;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating client", e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
    }

    @Override
    public Optional<Client> read(String id) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            final PreparedStatement stmt = conn.prepareStatement(READ);
            stmt.setString(1, id);
            try (final ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    final Client client = Client
                            .builder()
                            .id(rs.getString("id"))
                            .name(rs.getString("name"))
                            .age(rs.getInt("age"))
                            .email(rs.getString("email"))
                            .phoneNumber(rs.getString("phone_number"))
                            .build();
                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading client with id " + id, e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
        Database.releaseConnection(conn);
        return Optional.empty();
    }

    @Override
    public void deleteById(String id) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            final PreparedStatement stmt = conn.prepareStatement(DELETE);

            stmt.setString(1, id);
            final int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) throw new SQLException("No client found with id: " + id);

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting client with id " + id, e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
    }


    @Override
    public List<Client> findAll() {
        final List<Client> clients = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            final PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
            final ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                final Client client = Client.builder()
                        .id(rs.getString("id"))
                        .name(rs.getString("name"))
                        .age(rs.getInt("age"))
                        .email(rs.getString("email"))
                        .phoneNumber(rs.getString("phone_number"))
                        .build();
                clients.add(client);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all clients", e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
        return clients;
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            final PreparedStatement stmt = conn.prepareStatement(FIND_BY_EMAIL);
            stmt.setString(1, email);
            try (final ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = Client.builder()
                            .id(rs.getString("id"))
                            .name(rs.getString("name"))
                            .age(rs.getInt("age"))
                            .email(rs.getString("email"))
                            .phoneNumber(rs.getString("phone_number"))
                            .build();
                    return Optional.of(client);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding client with email " + email, e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
        return Optional.empty();
    }
}
