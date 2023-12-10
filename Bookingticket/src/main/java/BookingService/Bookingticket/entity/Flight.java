package BookingService.Bookingticket.entity;

import lombok.Data;


import java.time.LocalDate;


@Data

public class Flight {


    Long id;
    String flightNumber;
    String origin;
    String destination;
    LocalDate departureDate;
    LocalDate arrivalDate;
    double amount;
    int totalSeats;
    int availableSeats;
}
