package BookingService.Bookingticket.service;


import BookingService.Bookingticket.entity.HotelBooking;
import BookingService.Bookingticket.enums.BookingStatus;
import BookingService.Bookingticket.model.BookingRequest;
import BookingService.Bookingticket.model.BookingResponse;
import BookingService.Bookingticket.model.HotelBookingRequest;
import BookingService.Bookingticket.model.HotelBookingResponse;

import lombok.RequiredArgsConstructor;
import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.session.DocumentSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Qualifier("hotelBookingService")
public class HotelBookingService implements BookingService {

    @Autowired
    private DocumentStore documentStore;



    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {

        if (!(bookingRequest instanceof HotelBookingRequest)) {
            throw new IllegalArgumentException("Invalid booking type");
        }

        HotelBooking hotelBooking = mapToHotelBooking(bookingRequest);

        try (DocumentSession session = (DocumentSession) documentStore.openSession()) {

            HotelBookingResponse hotelBookingResponse = new HotelBookingResponse();
            BeanUtils.copyProperties(hotelBooking, hotelBookingResponse);

            return hotelBookingResponse;


        }






    }

    private HotelBooking mapToHotelBooking(BookingRequest bookingRequest) {
        HotelBookingRequest hotelBookingRequest = (HotelBookingRequest) bookingRequest;

        HotelBooking hotelBooking = new HotelBooking();

        hotelBooking.setBookingNumber(UUID.randomUUID().toString());
        hotelBooking.setBookingDate(LocalDate.now());

        hotelBooking.setPassengerName(hotelBookingRequest.getPassengerName());
        hotelBooking.setAmount(hotelBookingRequest.getAmount());
        hotelBooking.setPaymentMode(hotelBookingRequest.getPaymentMode().name());
        hotelBooking.setStatus(BookingStatus.CREATED.name());

        hotelBooking.setHotelName(hotelBookingRequest.getHotelName());
        hotelBooking.setCheckInDate(hotelBookingRequest.getCheckInDate());
        hotelBooking.setCheckOutDate(hotelBookingRequest.getCheckOutDate());

        return hotelBooking;
    }

}
