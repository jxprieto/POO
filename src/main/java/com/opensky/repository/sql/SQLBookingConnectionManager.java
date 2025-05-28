package com.opensky.repository.sql;

import com.opensky.exception.EntityNotFoundException;
import com.opensky.model.Booking;
import com.opensky.model.Client;
import com.opensky.model.Flight;
import com.opensky.repository.BookingRepository;
import com.opensky.repository.ClientRepository;
import com.opensky.repository.FlightRepository;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import static com.opensky.repository.sql.utils.SQLConnectionManager.*;


public class SQLBookingConnectionManager implements BookingRepository, Dependency {

    private final static DependencyInjector di = DependencyInjector.getDefaultImplementation();

    private final ClientRepository clientRepo;
    private final FlightRepository flightRepo;

    public static SQLBookingConnectionManager createInstance() {
        return new SQLBookingConnectionManager(
                di.getDependency(SQLClientConnectionManager.class),
                di.getDependency(SQLFlightConnectionManager.class)
        );
    }

    private SQLBookingConnectionManager(ClientRepository clientRepo, FlightRepository flightRepo) {
        this.clientRepo = clientRepo;
        this.flightRepo = flightRepo;
    }

    private static final String CREATE_BOOKING =
            "INSERT INTO bookings (id, client_id, booking_date) " +
            "VALUES (?, ?, ?)";

    private static final String UPDATE_BOOKING =
            "UPDATE bookings " +
            "SET client_id = ?, booking_date = ?" +
            " WHERE id = ?";
    
    private static final String READ_BOOKING =
            "SELECT id, client_id, booking_date" +
            "FROM bookings " +
            "WHERE id = ?";

    private static final String DELETE_BOOKING_BY_ID =
            "DELETE FROM bookings" +
            " WHERE id = ?";
    
    private static final String FIND_ALL_BOOKINGS =
            "SELECT id, client_id, booking_date" +
            "FROM bookings";

    private static final String DELETE_FROM_BOOKING_FLIGHTS_BY_BOOKING_ID =
            "DELETE FROM booking_flights " +
            "WHERE booking_id = ?";

    private static final String INSERT_FLIGHTS_IN_BOOKING =
            "INSERT INTO booking_flights (booking_id, flight_id, number_of_seats) " +
            "VALUES (?, ?, ?)";

    private static final String FIND_ALL_BOOKINGS_BY_CLIENT_ID =
            "SELECT id, client_id, booking_date" +
            "FROM bookings " +
            "WHERE client_id = ?";

    private static final String UPDATE_FLIGHT_ADD_NUM_OF_SEATS =
            "UPDATE booking_flights " +
            "SET num_of_seats = (num_of_seats + ?) " +
            "WHERE booking_id = ? and flight_id = ?";


    @Override
    public Booking create(Booking booking) {
        return withConnection(conn -> {
            try (final PreparedStatement stmt = conn.prepareStatement(CREATE_BOOKING, PreparedStatement.RETURN_GENERATED_KEYS)) {
                final String generatedId = UUID.randomUUID().toString();
                stmt.setString(1, generatedId);
                stmt.setString(2, booking.getClient().getId());
                stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                stmt.executeUpdate();
                return booking.toBuilder().id(generatedId).build();
            }
        }, "Error creating booking with ID: " + booking.getId());
    }

    @Override
    public Booking update(Booking booking) {
        return withConnection(conn -> {
            try (final PreparedStatement stmt = conn.prepareStatement(UPDATE_BOOKING)) {
                stmt.setString(3, booking.getId());
                stmt.setString(1, booking.getClient().getId());
                stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                int updated = stmt.executeUpdate();
                if (updated == 0) throw new EntityNotFoundException("Booking not found for update: " + booking.getId());
            }
            deleteFlightsForBooking(booking, conn);
            addFlightsToBooking(booking, conn);
            return booking;
        }, "Error updating booking with ID: " + booking.getId());
    }

    @Override
    public Optional<Booking> read(String id) {
        return withConnection(conn -> {
            try (final PreparedStatement stmt = conn.prepareStatement(READ_BOOKING)) {
                stmt.setString(1, id);
                try (final ResultSet rs = stmt.executeQuery()) {
                    return rs.next() ? Optional.of(getBookingFromResultSet(rs)) : Optional.empty();
                }
            }
        }, "Error reading booking with ID: " + id);
    }

    @Override
    public void deleteById(String id) {
        Booking booking = read(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found for deletion: " + id));
        withConnectionTransactional(conn ->{
            try (final PreparedStatement stmt = conn.prepareStatement(DELETE_BOOKING_BY_ID)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
            deleteFlightsForBooking(booking, conn);
            conn.commit();
        }, "Error deleting booking with ID: " + id);
    }

    @Override
    public List<Booking> findAll() {
        return withConnection(conn -> {
            final List<Booking> bookings = new ArrayList<>();
            try (final PreparedStatement stmt = conn.prepareStatement(FIND_ALL_BOOKINGS);
                 final ResultSet rs = stmt.executeQuery()) {

                while (rs.next())
                    bookings.add(getBookingFromResultSet(rs));

                return Collections.unmodifiableList(bookings);
            }
        }, "Error retrieving all bookings");
    }

    @Override
    public List<Booking> findBokingsByClientId(String id) {
        return withConnection(conn -> {
            final List<Booking> bookings = new ArrayList<>();
            try (final PreparedStatement stmt = conn.prepareStatement(FIND_ALL_BOOKINGS_BY_CLIENT_ID)) {
                stmt.setString(1, id);
                final ResultSet rs = stmt.executeQuery();

                while (rs.next())
                    bookings.add(getBookingFromResultSet(rs));

                return Collections.unmodifiableList(bookings);
            }
        }, "Error retrieving bookings for client ID: " + id);
    }

    private Client getFullClient(String clientId, String bookingId) {
        return clientRepo.read(clientId).orElseThrow(
                () -> new RuntimeException("Client not found for booking: " + bookingId + " with client ID: " + clientId)
        );
    }

    private Booking getBookingFromResultSet(ResultSet rs) throws SQLException {

        final String bookingId = rs.getString("id");
        final String clientId = rs.getString("client_id");

        final Client client = getFullClient(clientId, bookingId);

        final Map<Flight, Integer> flights = flightRepo.getAllFlightsByBookingId(bookingId);

        final List<Flight> flightList = new ArrayList<>();
        final List<Integer> countList = new ArrayList<>();

        for (Map.Entry<Flight, Integer> entry : flights.entrySet()) {
            flightList.add(entry.getKey());
            countList.add(entry.getValue());
        }

        return Booking.builder()
                .id(bookingId)
                .client(client)
                .flights(Collections.unmodifiableList(flightList))
                .numberOfSeatsPerFlight(Collections.unmodifiableList(countList))
                .build();
    }

    private static void deleteFlightsForBooking(Booking booking, Connection conn) throws SQLException {
        try (final PreparedStatement deleteStmt = conn.prepareStatement(DELETE_FROM_BOOKING_FLIGHTS_BY_BOOKING_ID)) {
            deleteStmt.setString(1, booking.getId());
            deleteStmt.executeUpdate();
        }
        try (final PreparedStatement updateAvailableSeats = conn.prepareStatement(UPDATE_FLIGHT_ADD_NUM_OF_SEATS)) {
            for (int i = 0; i < booking.getFlights().size(); ++i) {
                updateAvailableSeats.setInt(1, booking.getNumberOfSeatsPerFlight().get(i));
                updateAvailableSeats.setString(2, booking.getId());
                updateAvailableSeats.setString(3, booking.getFlights().get(i).getId());
                updateAvailableSeats.addBatch();
            }
            updateAvailableSeats.executeBatch();
        }catch (SQLException e) {
            throw new RuntimeException("Error updating available seats for flights in booking: " + booking.getId(), e);
        }
    }

    private static void addFlightsToBooking(Booking booking, Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        try (final PreparedStatement insertStmt = conn.prepareStatement(INSERT_FLIGHTS_IN_BOOKING)) {
            for (int i = 0 ; i < booking.getFlights().size(); ++i) {
                insertStmt.setString(1, booking.getId());
                insertStmt.setString(2, booking.getFlights().get(i).getId());
                insertStmt.setInt(3, booking.getNumberOfSeatsPerFlight().get(i));
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        }
        try (final PreparedStatement insertStmt = conn.prepareStatement(UPDATE_FLIGHT_ADD_NUM_OF_SEATS)) {
            for (int i = 0 ; i < booking.getFlights().size(); ++i) {
                insertStmt.setInt(1, - booking.getNumberOfSeatsPerFlight().get(i));
                insertStmt.setString(2, booking.getId());
                insertStmt.setString(3, booking.getFlights().get(i).getId());
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        }
        conn.commit();
        conn.setAutoCommit(true);
    }
}
