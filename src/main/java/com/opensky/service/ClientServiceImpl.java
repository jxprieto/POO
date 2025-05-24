package com.opensky.service;

import com.opensky.model.Client;
import com.opensky.utils.Dependency;

public class ClientServiceImpl implements ClientService, Dependency {

    public static ClientServiceImpl createInstance() {
        return new ClientServiceImpl();
    }

    @Override
    public void createClient(Client client) {
        System.out.println("Client created: " + client);
    }

}
