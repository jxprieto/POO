package com.opensky.repository;

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

public class SQLBookingRepository extends SQLRepository implements BookingRepository, Dependency {

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
            "INSERT INTO bookings (id, client_id, number_of_seats) " +
            "VALUES (?, ?, ?, ?)";

    private static final String UPDATE_BOOKING =
            "UPDATE bookings " +
            "SET client_id = ?, number_of_seats = ?" +
            " WHERE id = ?";
    
    private static final String READ_BOOKING =
            "SELECT id, client_id, flight_id, number_of_seats " +
            "FROM bookings " +
            "WHERE id = ?";

    private static final String DELETE_BOOKING_BY_ID =
            "DELETE FROM bookings" +
            " WHERE id = ?";
    
    private static final String FIND_ALL_BOOKINGS =
            "SELECT id, client_id, number_of_seats " +
            "FROM bookings";

    private static final String DELETE_FROM_BOOKING_FLIGHTS_BY_BOOKING_ID =
            "DELETE FROM booking_flights " +
            "WHERE booking_id = ?";

    private static final String INSERT_FLIGHTS_IN_BOOKING =
            "INSERT INTO booking_flights (booking_id, flight_id) " +
            "VALUES (?, ?)";


    @Override
    public Booking create(Booking booking) {
        return withConnection(conn -> {
            try (final PreparedStatement stmt = conn.prepareStatement(CREATE_BOOKING, PreparedStatement.RETURN_GENERATED_KEYS)) {
                final String generatedId = UUID.randomUUID().toString();
                stmt.setString(1, generatedId);
                stmt.setString(2, booking.getClient().getId());
                stmt.setInt(3, booking.getNumberOfSeats());
                stmt.executeUpdate();
                return booking.toBuilder().id(generatedId).build();
            }
        }, "Error creating booking with ID: " + booking.getId());
    }

    @Override
    public Booking update(Booking booking) {
        return withConnection(conn -> {
            try (final PreparedStatement stmt = conn.prepareStatement(UPDATE_BOOKING)) {
                stmt.setString(1, booking.getClient().getId());
                stmt.setInt(2, booking.getNumberOfSeats());
                stmt.setString(3, booking.getId());
                int updated = stmt.executeUpdate();
                if (updated == 0)
                    throw new RuntimeException("Booking not found for update: " + booking.getId());
            }
            deleteFlightsForBooking(booking, conn);
            addFLightsToBooking(booking, conn);
            return booking;
        }, "Error updating booking with ID: " + booking.getId());
    }

    @Override
    public Optional<Booking> read(String id) {
        return withConnection(conn -> {
            try (final PreparedStatement stmt = conn.prepareStatement(READ_BOOKING)) {
                stmt.setString(1, id);
                try (final ResultSet rs = stmt.executeQuery()) {
                    return rs.next() ? getBookingFromResultSet(rs) : Optional.empty();
                }
            }
        }, "Error reading booking with ID: " + id);
    }

    @Override
    public void deleteById(String id) {
        withConnection(conn ->{
            try (final PreparedStatement stmt = conn.prepareStatement(DELETE_BOOKING_BY_ID)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
        }, "Error deleting booking with ID: " + id);
    }

    @Override
    public List<Booking> findAll() {
        return withConnection(conn -> {
            final List<Booking> bookings = new ArrayList<>();
            try (final PreparedStatement stmt = conn.prepareStatement(FIND_ALL_BOOKINGS);
                 final ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    getBookingFromResultSet(rs)
                            .ifPresent(bookings::add);
                }
                return bookings;
            }
        }, "Error retrieving all bookings");
    }

    private Client getFullClient(String clientId, String id) {
        return clientRepo.read(clientId).orElseThrow(
                () -> new RuntimeException("Client not found for booking: " + id + " with client ID: " + clientId)
        );
    }

    private Optional<Booking> getBookingFromResultSet(ResultSet rs) throws SQLException {
        final String bookingId = rs.getString("id");
        final String clientId = rs.getString("client_id");
        final int seats = rs.getInt("number_of_seats");

        final Client client = getFullClient(clientId, bookingId);
        final List<Flight> flights = flightRepo.getAllFlightsByBookingId(bookingId);

        final Booking booking = Booking.builder()
                .id(bookingId)
                .client(client)
                .flights(flights)
                .numberOfSeats(seats)
                .build();
        return Optional.of(booking);
    }

    private static void deleteFlightsForBooking(Booking booking, Connection conn) throws SQLException {
        try (final PreparedStatement deleteStmt = conn.prepareStatement(DELETE_FROM_BOOKING_FLIGHTS_BY_BOOKING_ID)) {
            deleteStmt.setString(1, booking.getId());
            deleteStmt.executeUpdate();
        }
    }

    private static void addFLightsToBooking(Booking booking, Connection conn) throws SQLException {
        try (final PreparedStatement insertStmt = conn.prepareStatement(INSERT_FLIGHTS_IN_BOOKING)) {
            for (Flight flight : booking.getFlights()) {
                insertStmt.setString(1, booking.getId());
                insertStmt.setString(2, flight.getId());
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        }
    }

}
