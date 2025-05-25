package com.opensky.repository;

import com.opensky.Database;
import com.opensky.model.Flight;
import com.opensky.utils.Dependency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLFlightRepositoryImpl implements FlightRepository, Dependency {

    public static SQLFlightRepositoryImpl createInstance() {
        return new SQLFlightRepositoryImpl();
    }

    private static final String CREATE_FLIGHT =
            "INSERT INTO flights (flight_number, origin, destination, departure_time, arrival_time, available_seats) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_FLIGHT =
            "UPDATE flights " +
            "SET flight_number = ?, origin = ?, destination = ?, departure_time = ?, arrival_time = ?, available_seats = ? " +
            "WHERE id = ?";

    private static final String READ_FLIGHT =
            "SELECT * " +
            "FROM flights " +
            "WHERE id = ?";

    private static final String DELETE_FLIGHT =
            "DELETE FROM flights " +
            "WHERE id = ?";

    private static final String FIND_ALL_FLIGHTS =
            "SELECT * " +
            "FROM flights";


    @Override
    public Flight create(Flight flight) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            try (final PreparedStatement stmt = conn.prepareStatement(CREATE_FLIGHT, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, flight.getFlightNumber());
                stmt.setString(2, flight.getOrigin());
                stmt.setString(3, flight.getDestination());
                stmt.setTimestamp(4, Timestamp.valueOf(flight.getDepartureTime()));
                stmt.setTimestamp(5, Timestamp.valueOf(flight.getArrivalTime()));
                stmt.setInt(6, flight.getAvailableSeats());
                stmt.executeUpdate();

                try (final ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        String generatedId = keys.getString(1);
                        return flight.toBuilder().id(generatedId).build();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating flight", e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
        throw new RuntimeException("Failed to retrieve generated ID for flight");
    }

    @Override
    public Flight update(Flight flight) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_FLIGHT)) {
                stmt.setString(1, flight.getFlightNumber());
                stmt.setString(2, flight.getOrigin());
                stmt.setString(3, flight.getDestination());
                stmt.setTimestamp(4, Timestamp.valueOf(flight.getDepartureTime()));
                stmt.setTimestamp(5, Timestamp.valueOf(flight.getArrivalTime()));
                stmt.setInt(6, flight.getAvailableSeats());
                stmt.setString(7, flight.getId());
                stmt.executeUpdate();
                return flight;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating flight", e);
        } finally {
            if (conn != null) {
                Database.releaseConnection(conn);
            }
        }
    }

    @Override
    public Optional<Flight> read(String id) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            try (final PreparedStatement stmt = conn.prepareStatement(READ_FLIGHT)) {
                stmt.setString(1, id);
                try (final ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        final Flight flight = Flight.builder()
                                .id(rs.getString("id"))
                                .flightNumber(rs.getString("flight_number"))
                                .origin(rs.getString("origin"))
                                .destination(rs.getString("destination"))
                                .departureTime(rs.getTimestamp("departure_time").toLocalDateTime())
                                .arrivalTime(rs.getTimestamp("arrival_time").toLocalDateTime())
                                .availableSeats(rs.getInt("available_seats"))
                                .build();
                        return Optional.of(flight);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading flight", e);
        } finally {
            if (conn != null) {
                Database.releaseConnection(conn);
            }
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(String id) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            try (final PreparedStatement stmt = conn.prepareStatement(DELETE_FLIGHT)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting flight", e);
        } finally {
            if (conn != null) {
                Database.releaseConnection(conn);
            }
        }
    }

    @Override
    public List<Flight> findAll() {
        final List<Flight> flights = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            try (final PreparedStatement stmt = conn.prepareStatement(FIND_ALL_FLIGHTS);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    final Flight flight = Flight.builder()
                            .id(rs.getString("id"))
                            .flightNumber(rs.getString("flight_number"))
                            .origin(rs.getString("origin"))
                            .destination(rs.getString("destination"))
                            .departureTime(rs.getTimestamp("departure_time").toLocalDateTime())
                            .arrivalTime(rs.getTimestamp("arrival_time").toLocalDateTime())
                            .availableSeats(rs.getInt("available_seats"))
                            .build();
                    flights.add(flight);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all flights", e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
        return flights;
    }
}
