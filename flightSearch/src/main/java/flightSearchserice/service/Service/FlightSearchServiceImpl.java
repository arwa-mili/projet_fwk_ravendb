package flightSearchserice.service.Service;


import flightSearchserice.service.Entity.Flight;
import flightSearchserice.service.Entity.FlightSearchRequest;
import flightSearchserice.service.Entity.FlightSearchResponse;

import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.session.DocumentSession;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.management.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightSearchServiceImpl implements FlightSearchService {

    @Autowired
    private DocumentStore documentStore;
    @Override
    public List<FlightSearchResponse> searchFlights(String origin, String destination, LocalDate departureDate, int passengers) {
        try (DocumentSession session = (DocumentSession) documentStore.openSession()) {
            List<Flight> flights = session.query(Flight.class)
                    .whereEquals("origin", origin)
                    .andAlso()
                    .whereEquals("destination", destination)
                    .andAlso()
                    .whereGreaterThanOrEqual("departureDate", departureDate)
                    .andAlso()
                    .whereGreaterThanOrEqual("availableSeats", passengers)
                    .toList();

            return flights.stream()
                    .map(this::mapToFlightSearchResponse)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Flight getFlightByFlightNumber(String flightNumber) {
        try (DocumentSession session = (DocumentSession) documentStore.openSession()) {
            return session.query(Flight.class)
                    .whereEquals("flightNumber", flightNumber)
                    .firstOrDefault();
        }
    }

    @Override
    public List<Flight> searchAllFlights() {
        try (DocumentSession session = (DocumentSession) documentStore.openSession()) {
            return session.query(Flight.class).toList();
        }
    }

    @Override
    public void updateAvailableSeats(String flightNumber) {
        try (DocumentSession session = (DocumentSession) documentStore.openSession()) {
            Flight flight = session.query(Flight.class)
                    .whereEquals("flightNumber", flightNumber)
                    .firstOrDefault();

            if (flight != null) {
                flight = flight.withAvailableSeats(flight.availableSeats() - 1);
                session.saveChanges();
            }
        }
    }

    private FlightSearchResponse mapToFlightSearchResponse(Flight flight) {
        FlightSearchResponse flightSearchResponse = new FlightSearchResponse();
        BeanUtils.copyProperties(flight, flightSearchResponse);
        return flightSearchResponse;
    }

}
