package com.opensky.repository;

import com.opensky.Database;
import com.opensky.model.Booking;
import com.opensky.model.Client;
import com.opensky.model.Flight;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SQLBookingRepository implements BookingRepository, Dependency {

    private final static DependencyInjector di = DependencyInjector.getDefaultImplementation();

    private final ClientRepository clientRepo;
    private final FlightRepository flightRepo;

    public static SQLBookingRepository createInstance() {
        return new SQLBookingRepository(
                di.getDependency(SQLClientRepository.class),
                di.getDependency(SQLFlightRepository.class)
        );
    }

    private SQLBookingRepository(ClientRepository clientRepo, FlightRepository flightRepo) {
        this.clientRepo = clientRepo;
        this.flightRepo = flightRepo;
    }

    private static final String CREATE_BOOKING =
            "INSERT INTO bookings (id, client_id, flight_id, number_of_seats) " +
            "VALUES (?, ?, ?, ?)";

    private static final String UPDATE_BOOKING =
            "UPDATE bookings " +
            "SET client_id = ?, flight_id = ?, number_of_seats = ?" +
            " WHERE id = ?";
    
    private static final String READ_BOOKING =
            "SELECT id, client_id, flight_id, number_of_seats " +
            "FROM bookings " +
            "WHERE id = ?";

    private static final String DELETE_BOOKING =
            "DELETE FROM bookings" +
            " WHERE id = ?";
    
    private static final String FIND_ALL_BOOKINGS =
            "SELECT id, client_id, flight_id, number_of_seats " +
            "FROM bookings";



    @Override
    public Booking create(Booking booking) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            try (final PreparedStatement stmt = conn.prepareStatement(CREATE_BOOKING, PreparedStatement.RETURN_GENERATED_KEYS)) {

                final String generatedId = UUID.randomUUID().toString();
                stmt.setString(1, generatedId);
                stmt.setString(2, booking.getClient().getId());
                stmt.setString(3, booking.getFlight().getId());
                stmt.setInt(4, booking.getNumberOfSeats());
                stmt.executeUpdate();
                return booking.toBuilder().id(generatedId).build();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating booking", e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
    }

    @Override
    public Booking update(Booking booking) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            try (final PreparedStatement stmt = conn.prepareStatement(UPDATE_BOOKING)) {
                stmt.setString(1, booking.getClient().getId());
                stmt.setString(2, booking.getFlight().getId());
                stmt.setInt(3, booking.getNumberOfSeats());
                stmt.setString(4, booking.getId());

                int updated = stmt.executeUpdate();
                if (updated == 0) {
                    throw new RuntimeException("Booking not found for update: " + booking.getId());
                }

                return booking;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating booking", e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
    }

    @Override
    public Optional<Booking> read(String id) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            try (final PreparedStatement stmt = conn.prepareStatement(READ_BOOKING)) {
                stmt.setString(1, id);
                try (final ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        final String bookingId = rs.getString("id");
                        final String clientId = rs.getString("client_id");
                        final String flightId = rs.getString("flight_id");
                        final int seats = rs.getInt("number_of_seats");

                        final Client client = getFullClient(clientId, bookingId);
                        final Flight flight = getFullFlight(flightId, bookingId);

                        final Booking booking = Booking.builder()
                                .id(bookingId)
                                .client(client)
                                .flight(flight)
                                .numberOfSeats(seats)
                                .build();

                        return Optional.of(booking);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading booking", e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
    }

    @Override
    public void deleteById(String id) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            try (final PreparedStatement stmt = conn.prepareStatement(DELETE_BOOKING)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting booking with id: " + id, e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }
    }

    @Override
    public List<Booking> findAll() {
        final List<Booking> bookings = new ArrayList<>();
        Connection conn = null;

        try {
            conn = Database.getConnection();
            try (final PreparedStatement stmt = conn.prepareStatement(FIND_ALL_BOOKINGS);
                 final ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    final String id = rs.getString("id");
                    final String clientId = rs.getString("client_id");
                    final String flightId = rs.getString("flight_id");
                    final int numberOfSeats = rs.getInt("number_of_seats");

                    final Client client = getFullClient(clientId, id);
                    final Flight flight = getFullFlight(flightId, id);

                    final Booking booking = Booking.builder()
                            .id(id)
                            .client(client)
                            .flight(flight)
                            .numberOfSeats(numberOfSeats)
                            .build();

                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all bookings", e);
        } finally {
            if (conn != null) Database.releaseConnection(conn);
        }

        return bookings;
    }

    private Flight getFullFlight(String flightId, String id) {
        return flightRepo.read(flightId).orElseThrow(
                ()-> new RuntimeException("Flight not found for booking: " + id + " with flight ID: " + flightId)
        );
    }

    private Client getFullClient(String clientId, String id) {
        return clientRepo.read(clientId).orElseThrow(
                () -> new RuntimeException("Client not found for booking: " + id + " with client ID: " + clientId)
        );
    }
}
