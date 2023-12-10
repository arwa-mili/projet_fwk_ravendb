package BookingService.Bookingticket.entity;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class FlightBooking extends Booking {

    private String flightNumber;
    private String destination;
}