package BookingService.Bookingticket.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Data
public final class HotelBookingRequest extends BookingRequest {

    private String hotelName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

}
