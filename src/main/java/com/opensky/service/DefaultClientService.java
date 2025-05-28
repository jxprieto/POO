package com.opensky.service;

import com.opensky.exception.DuplicatedFieldException;
import com.opensky.model.Booking;
import com.opensky.model.Client;
import com.opensky.printer.ConsolePrinter;
import com.opensky.printer.Printer;
import com.opensky.repository.BookingRepository;
import com.opensky.repository.ClientRepository;
import com.opensky.repository.sql.SQLBookingRepository;
import com.opensky.repository.sql.SQLClientRepository;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

import java.util.List;

public class DefaultClientService implements ClientService, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static DefaultClientService createInstance() {
        return new DefaultClientService(
                di.getDependency(SQLClientRepository.class),
                di.getDependency(SQLBookingRepository.class),
                di.getDependency(ConsolePrinter.class)
        );
    }

    private final ClientRepository repo;
    private final BookingRepository bookingRepository;
    private final Printer printer;

    private DefaultClientService(ClientRepository repo, BookingRepository bookingRepository, Printer printer) {
        this.repo = repo;
        this.bookingRepository = bookingRepository;
        this.printer = printer;
    }

    @Override
    public Client createClient(String name, Integer age, String email, String phone) {
        repo.findByEmail(email)
                .ifPresent(_ -> { throw new DuplicatedFieldException("email" , email); });
        repo.findByNumber(phone)
                .ifPresent(_ -> { throw new DuplicatedFieldException("phone number" , phone); });
        return repo.create(Client
                .builder()
                .name(name)
                .age(age)
                .email(email)
                .phoneNumber(phone)
                .build());
    }

    @Override
    public List<Booking> getAllClientBookings(String id) {
        return bookingRepository.findBokingsByClientId(id);
    }

    @Override
    public List<Client> getAllClients() {
        return repo.findAll();
    }

}
