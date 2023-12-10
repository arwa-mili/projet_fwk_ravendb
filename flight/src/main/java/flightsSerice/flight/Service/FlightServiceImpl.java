package flightsSerice.flight.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import flightsSerice.flight.Entity.Flight;
import flightsSerice.flight.Entity.FlightRequest;
import flightsSerice.flight.Entity.FlightResponse;
import flightsSerice.flight.Event.NewFlightEvent;
import flightsSerice.flight.Exception.FlightServiceCustomException;
import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.session.DocumentSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {


    private final KafkaTemplate<String, NewFlightEvent> kafkaTemplate;

    @Autowired
    private DocumentStore documentStore;



    @Override
    public FlightResponse createFlight(FlightRequest flightRequest) {


        try (DocumentSession session = (DocumentSession) documentStore.openSession()) {
            Flight flight = new Flight();
            flight.setFlightId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
            flight.setFlightNumber(flightRequest.flightNumber());
            flight.setOrigin(flightRequest.origin());
            flight.setDestination(flightRequest.destination());
            flight.setDepartureDate(flightRequest.departureDate());
            flight.setArrivalDate(flightRequest.arrivalDate());
            flight.setTotalSeats(flightRequest.totalSeats());
            flight.setAvailableSeats(flightRequest.availableSeats());
            flight.setAmount(flightRequest.amount());
            session.store(flight);
            session.saveChanges();
            FlightResponse flightResponse = new FlightResponse();
            BeanUtils.copyProperties(flight, flightResponse);


            kafkaTemplate.send("notificationTopic",new NewFlightEvent(flight.getFlightId(),flight.getFlightNumber(),flight.getOrigin(),flight.getDestination(),
                    flight.getDepartureDate(),flight.getArrivalDate(),flight.getAmount(),flight.getTotalSeats(),flight.getAvailableSeats())
            );
            log.info("Flight Created {}", flightResponse.getFlightId());
            return flightResponse;
        }


    }




    @Override
    public List<FlightResponse> getAllFlights() {
        try (DocumentSession session = (DocumentSession) documentStore.openSession()) {
            List<FlightResponse> flightResponseList =  session.query(Flight.class).toList()
                    .stream()
                    .map(this::mapToFlightResponse)
                    .collect(Collectors.toList());;


        return flightResponseList;
    }}

    @Override
    public FlightResponse getFlightByNumber(String flightNumber) {
        log.info("Get the flight for flight Number: {}", flightNumber);

        try (DocumentSession session = (DocumentSession) documentStore.openSession()) {
            Flight optionalFlight = session.query(Flight.class)
                    .whereEquals("flightNumber", flightNumber)
                    .firstOrDefault();

            if (optionalFlight == null) {
                throw new FlightServiceCustomException("Flight with given number not found", "FLIGHT_NOT_FOUND");
            }

            FlightResponse flightResponse = new FlightResponse();
            BeanUtils.copyProperties(optionalFlight, flightResponse);

            return flightResponse;
        }

    }

    private FlightResponse mapToFlightResponse(Flight flight) {
        FlightResponse flightResponse = new FlightResponse();
        BeanUtils.copyProperties(flight, flightResponse);
        return flightResponse;

    }
}
