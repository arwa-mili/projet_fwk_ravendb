package flightsSerice.flight.Entity;

import java.util.Date;
import java.time.LocalDate;


import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {

    private Long flightId;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private int totalSeats;
    private int availableSeats;
    private double amount;
}




/*{

        "flightId":1,
        "flightNumber":12,
        "origin": "Monastir",
        "destination" :"Tunis",
        "departureDate" : 5,
        "arrivalDate" : 6,
        "totalSeats" : 15,
        "availableSeats" : 8,
        "amount": 45
        }
 */