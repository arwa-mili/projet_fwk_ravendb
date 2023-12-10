package flightSearchserice.service.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightSearchRequest2 {

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
