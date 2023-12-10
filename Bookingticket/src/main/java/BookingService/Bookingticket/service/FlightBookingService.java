package BookingService.Bookingticket.service;


import BookingService.Bookingticket.entity.Flight;
import BookingService.Bookingticket.entity.FlightBooking;
import BookingService.Bookingticket.enums.BookingStatus;
import BookingService.Bookingticket.event.BookingCompletedEvent;
import BookingService.Bookingticket.event.FlightBookingCompleted;
import BookingService.Bookingticket.model.BookingRequest;
import BookingService.Bookingticket.model.BookingResponse;
import BookingService.Bookingticket.model.FlightBookingRequest;
import BookingService.Bookingticket.model.FlightBookingResponse;
import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.session.DocumentSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
@Qualifier("flightBookingService")
public class FlightBookingService implements BookingService {


    private final WebClient webClient;

    @Autowired
    private DocumentStore documentStore;

    @Autowired
    private KafkaTemplate<String, FlightBookingCompleted> kafkaTemplate;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {

        if (!(bookingRequest instanceof FlightBookingRequest)) {
            throw new IllegalArgumentException("Invalid booking type");
        }

        FlightBooking flightBooking2 = mapToFlightBooking(bookingRequest);

        try (DocumentSession session = (DocumentSession) documentStore.openSession()) {
            FlightBooking flightBooking = new FlightBooking();
            flightBooking.setId(flightBooking2.getId());
            flightBooking.setBookingDate(flightBooking2.getBookingDate());
            flightBooking.setFlightNumber(flightBooking2.getFlightNumber());
            flightBooking.setPassengerName(flightBooking2.getPassengerName());
            session.store(flightBooking);
            session.saveChanges();
            FlightBookingResponse flightBookingResponse = new FlightBookingResponse();
            BeanUtils.copyProperties(flightBooking, flightBookingResponse);
            FlightBookingCompleted flightBookingCompleted = new FlightBookingCompleted(
                    flightBooking.getBookingNumber().toString());
            log.info("Sending event to notificationTopic with event {}",flightBookingCompleted);
            kafkaTemplate.send("booking-notificationTopic", flightBookingCompleted);

            return flightBookingResponse;
        }







    }

    private FlightBooking mapToFlightBooking(BookingRequest bookingRequest) {

        FlightBookingRequest flightBookingRequest = (FlightBookingRequest) bookingRequest;

        log.info("flight n - {}",flightBookingRequest.getFlightNumber());
        Flight flightNumberN = webClient
                .get()
                .uri("http://localhost:8080/v1/api/search/{flightNumber}",
                        flightBookingRequest.getFlightNumber())
                .retrieve()
                .bodyToMono(Flight.class)
                .block();
        log.info("{}",flightNumberN);

        FlightBooking flightBooking = new FlightBooking();
        try {
            if (flightNumberN.getAvailableSeats()>0){
                flightBooking.setBookingNumber(UUID.randomUUID().toString());
                flightBooking.setFlightNumber(flightNumberN.getFlightNumber());
                flightBooking.setDestination(flightNumberN.getDestination());

                flightBooking.setBookingDate(LocalDate.now());
                flightBooking.setStatus(BookingStatus.CREATED.name());
                flightBooking.setPassengerName(flightBookingRequest.getPassengerName());
                flightBooking.setAmount(flightBookingRequest.getAmount());
                flightBooking.setPaymentMode(flightBookingRequest.getPaymentMode().name());





                String updateSeatsUrl = "http://localhost:8080/v1/api/search/updateSeats/" +flightNumberN.getFlightNumber();
                webClient.put()
                        .uri(updateSeatsUrl)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();}







        } catch (Exception e) {

            log.info("{}",flightNumberN);

        }



        return flightBooking;


    };}
