package BookingService.Bookingticket.entity;

import java.time.LocalDate;

import lombok.*;




@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    private Long id;

    private String bookingNumber;
    private String passengerName;
    private String status;
    private double amount;
    private String paymentMode;
    private LocalDate bookingDate;
}