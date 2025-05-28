package com.opensky.service;

import com.opensky.model.Booking;
import com.opensky.model.Client;
import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.repository.BookingRepository;
import com.opensky.repository.ClientRepository;
import com.opensky.repository.sql.SQLBookingConnectionManager;
import com.opensky.repository.sql.SQLClientConnectionManager;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

import java.util.List;

public class DefaultClientService implements ClientService, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    private final ClientRepository repo;
    private final BookingRepository bookingRepository;
    private final Printer printer;

    public DefaultClientService(ClientRepository repo, BookingRepository bookingRepository, Printer printer) {
        this.repo = repo;
        this.bookingRepository = bookingRepository;
        this.printer = printer;
    }

    public static DefaultClientService createInstance() {
        return new DefaultClientService(
                di.getDependency(SQLClientConnectionManager.class),
                di.getDependency(SQLBookingConnectionManager.class),
                di.getDependency(ConsolePrinter.class)
        );
    }

    @Override
    public void createClient(String name, Integer age, String email, String phone) {
        repo.findByEmail(email)
                .ifPresent(_ -> printer.print("Client with email " + email + " already exists."));
        repo.create(Client
                .builder()
                .name(name)
                .age(age)
                .email(email)
                .phoneNumber(phone)
                .build());
    }

    @Override
    public void showItinerary(String id) {
        final List<Booking> bookings = bookingRepository.findBokingsByClientId(id);
        printer.print("Client with id " + id + " has the following bookings:" + bookings);
    }

}
