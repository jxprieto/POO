CREATE TABLE clients (
                         id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
                         name VARCHAR(100) NOT NULL,
                         age INT,
                         email VARCHAR(100),
                         phone_number VARCHAR(20)
);

CREATE TABLE flights (
                         id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
                         flight_number VARCHAR(20) NOT NULL,
                         origin VARCHAR(100) NOT NULL,
                         destination VARCHAR(100) NOT NULL,
                         departure_time DATETIME NOT NULL,
                         arrival_time DATETIME NOT NULL,
                         available_seats INT NOT NULL
);

CREATE TABLE bookings (
                         id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
                         client_id VARCHAR(36) NOT NULL,
                         booking_date DATETIME NOT NULL,

                         CONSTRAINT fk_booking_client FOREIGN KEY (client_id) REFERENCES clients(id)
                             ON DELETE CASCADE
                             ON UPDATE CASCADE
);

CREATE TABLE booking_flights (
                                 booking_id VARCHAR(36) NOT NULL,
                                 flight_id VARCHAR(36) NOT NULL,
                                 number_of_seats INT NOT NULL,

                                 PRIMARY KEY (booking_id, flight_id),

                                 CONSTRAINT fk_bookingflight_booking FOREIGN KEY (booking_id) REFERENCES bookings(id)
                                     ON DELETE CASCADE
                                     ON UPDATE CASCADE,

                                 CONSTRAINT fk_bookingflight_flight FOREIGN KEY (flight_id) REFERENCES flights(id)
                                     ON DELETE CASCADE
                                     ON UPDATE CASCADE
);