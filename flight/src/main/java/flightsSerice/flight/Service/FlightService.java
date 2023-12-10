package flightsSerice.flight.Service;



import flightsSerice.flight.Entity.FlightRequest;
import flightsSerice.flight.Entity.FlightResponse;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface FlightService {

    FlightResponse createFlight(FlightRequest flightRequest);

    List<FlightResponse> getAllFlights();

    FlightResponse getFlightByNumber(String flightNumber);

}