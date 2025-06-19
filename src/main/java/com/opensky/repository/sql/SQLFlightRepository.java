package com.opensky.repository.sql;

import com.opensky.model.Flight;
import com.opensky.repository.FlightRepository;
import com.opensky.utils.Dependency;

import java.sql.*;
import java.util.*;

import static com.opensky.repository.sql.utils.SQLConnectionManager.withConnection;

public class SQLFlightRepository implements FlightRepository, Dependency  {

    public static SQLFlightRepository createInstance() {
        return new SQLFlightRepository();
    }

    private static final String CREATE_FLIGHT =
            "INSERT INTO flights (id, flight_number, origin, destination, departure_time, arrival_time, available_seats) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

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

    private static final String FIND_ALL_FLIGHTS_BY_BOOKING_ID =
            "SELECT f.id, f.flight_number, f.origin, f.destination, f.departure_time, f.arrival_time, f.available_seats, bf.number_of_seats " +
            "FROM flights f JOIN booking_flights bf " +
            "ON f.id = bf.flight_id " +
            "WHERE bf.booking_id = ?";

    private SQLFlightRepository() {}

    @Override
    public Flight create(Flight flight) {
        return withConnection(conn ->{
            try (final PreparedStatement stmt = conn.prepareStatement(CREATE_FLIGHT, Statement.RETURN_GENERATED_KEYS)) {
                final String generatedId = UUID.randomUUID().toString();
                stmt.setString(1, generatedId);
                stmt.setString(2, flight.getFlightNumber());
                stmt.setString(3, flight.getOrigin());
                stmt.setString(4, flight.getDestination());
                stmt.setTimestamp(5, Timestamp.valueOf(flight.getDepartureTime()));
                stmt.setTimestamp(6, Timestamp.valueOf(flight.getArrivalTime()));
                stmt.setInt(7, flight.getAvailableSeats());
                stmt.executeUpdate();
                return flight.toBuilder().id(generatedId).build();
            }
        }, "Error creating flight with ID: " + flight.getId());
    }

    @Override
    public Flight update(Flight flight) {
        return withConnection(conn -> {
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
        }, "Error updating flight with ID: " + flight.getId());
    }

    @Override
    public Optional<Flight> read(String id) {
        return withConnection(conn -> {
            try (final PreparedStatement stmt = conn.prepareStatement(READ_FLIGHT)) {
                stmt.setString(1, id);
                return getFlight(stmt);
            }
        }, "Error reading flight with ID: " + id);
    }

    @Override
    public void deleteById(String id) {
        withConnection(conn -> {
            try (final PreparedStatement stmt = conn.prepareStatement(DELETE_FLIGHT)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
        }, "Error deleting flight with ID: " + id);
    }

    @Override
    public List<Flight> findAll() {
        return withConnection(conn -> {
            final List<Flight> flights = new ArrayList<>();
            try (final PreparedStatement stmt = conn.prepareStatement(FIND_ALL_FLIGHTS);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    final Flight flight = buildFlightFromResultSet(rs);
                    flights.add(flight);
                }
            }
            return flights;
        }, "Error finding all flights");
    }

    @Override
    public Map<Flight, Integer> getAllFlightsByBookingId(String bookingId) {
        return withConnection(conn -> {
            final Map<Flight, Integer> flights = new HashMap<>();
            try (PreparedStatement stmt = conn.prepareStatement(FIND_ALL_FLIGHTS_BY_BOOKING_ID)) {
                stmt.setString(1, bookingId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Flight flight = buildFlightFromResultSet(rs);
                        flights.put(flight, rs.getInt("number_of_seats"));
                    }
                }
            }
            return Collections.unmodifiableMap(flights);
        }, "Error finding flights by booking ID: " + bookingId);
    }

    private Optional<Flight> getFlight(PreparedStatement stmt) throws SQLException {
        try (final ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                final Flight flight = buildFlightFromResultSet(rs);
                return Optional.of(flight);
            }
        }
        return Optional.empty();
    }

    private static Flight buildFlightFromResultSet(ResultSet rs) throws SQLException {
        return Flight.builder()
                .id(rs.getString("id"))
                .flightNumber(rs.getString("flight_number"))
                .origin(rs.getString("origin"))
                .destination(rs.getString("destination"))
                .departureTime(rs.getTimestamp("departure_time").toLocalDateTime())
                .arrivalTime(rs.getTimestamp("arrival_time").toLocalDateTime())
                .availableSeats(rs.getInt("available_seats"))
                .build();
    }


}
