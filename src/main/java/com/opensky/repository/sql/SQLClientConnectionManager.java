package com.opensky.repository.sql;

import com.opensky.model.Client;
import com.opensky.repository.ClientRepository;
import com.opensky.utils.Dependency;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.opensky.repository.sql.utils.SQLConnectionManager.*;


public class SQLClientConnectionManager implements ClientRepository, Dependency {

    public static SQLClientConnectionManager createInstance() {
        return new SQLClientConnectionManager();
    }

    private static final String CREATE =
            "INSERT INTO clients (id, name, age, email, phone_number) " +
            "VALUES (?, ?, ?, ?, ?)";

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
        return withConnection(conn -> {
            try (final PreparedStatement stmt = conn.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
                final String generatedId = UUID.randomUUID().toString();
                stmt.setString(1, generatedId);
                stmt.setString(2, client.getName());
                stmt.setObject(3, client.getAge(), Types.INTEGER);
                stmt.setString(4, client.getEmail());
                stmt.setString(5, client.getPhoneNumber());
                stmt.executeUpdate();
                return client.toBuilder().id(generatedId).build();
            }
        }, "Error creating client: " + client.getName());
    }

    @Override
    public Client update(Client client) {
        return withConnection(conn -> {
            final PreparedStatement stmt = conn.prepareStatement(UPDATE);
            stmt.setString(1, client.getName());
            stmt.setObject(2, client.getAge(), Types.INTEGER);
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhoneNumber());
            stmt.setString(5, client.getId());
            stmt.executeUpdate();
            return client;
        }, "Error updating client with ID: " + client.getId());
    }

    @Override
    public Optional<Client> read(String id) {
        return withConnection(conn -> {
            final PreparedStatement stmt = conn.prepareStatement(READ);
            stmt.setString(1, id);
            try (final ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(
                            Client
                                .builder()
                                .id(rs.getString("id"))
                                .name(rs.getString("name"))
                                .age(rs.getInt("age"))
                                .email(rs.getString("email"))
                                .phoneNumber(rs.getString("phone_number"))
                                .build());
                }
                return Optional.empty();
            }
        }, "Error reading client with id: " + id);

    }

    @Override
    public void deleteById(String id) {
        withConnection(conn -> {
            final PreparedStatement stmt = conn.prepareStatement(DELETE);

            stmt.setString(1, id);
            final int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) throw new SQLException("No client found with id: " + id);
        }, "Error deleting client with ID: " + id);
    }


    @Override
    public List<Client> findAll() {
        return withConnection(conn -> {
            final PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
            final ResultSet rs = stmt.executeQuery();
            final List<Client> clients = new ArrayList<>();
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
            return clients;
        }, "Error finding all clients");
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return withConnection(conn -> {
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
                return Optional.empty();
            }
        }, "Error finding client by email: " + email);
    }
}
