package com.opensky.view;

import com.opensky.exception.FormatDataException;
import com.opensky.service.ClientService;
import com.opensky.service.DefaultClientService;
import com.opensky.utils.Dependency;
import com.opensky.utils.DependencyInjector;

public class ViewItineraryCommand implements Command, Dependency {

    private static final DependencyInjector di = DependencyInjector.getDefaultImplementation();

    public static ViewItineraryCommand createInstance() {
        return new ViewItineraryCommand(
                di.getDependency(DefaultClientService.class)
        );
    }

    private final ClientService service;

    public ViewItineraryCommand(ClientService service) {
        this.service = service;
    }

    @Override
    public void execute(String command) {
        final String[] args = command.split(ARGUMENT_SPLIT_REGEX);
        if (args.length != 2)
            throw new FormatDataException("Invalid input, expected for " + VIEW_ITINERARY_COMMAND + "is: " + VIEW_ITINERARY);
        final String id = args[1].split(COLON)[1];
        if (id == null || id.isEmpty()) throw new FormatDataException("ID cannot be null or empty");
        service.showItinerary(id);
    }
}
