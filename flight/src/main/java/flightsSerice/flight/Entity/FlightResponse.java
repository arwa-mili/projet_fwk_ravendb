package flightsSerice.flight.Entity;

import java.util.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightResponse {
    private Long flightId;
    private String flightNumber;
    private String origin;
    private String destination;
    private Date departureDate;
    private Date arrivalDate;
    private int totalSeats;
    private int availableSeats;
    private double amount;
}
