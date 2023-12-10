package BookingService.Bookingticket.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelBooking extends Booking {
    private String hotelName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}