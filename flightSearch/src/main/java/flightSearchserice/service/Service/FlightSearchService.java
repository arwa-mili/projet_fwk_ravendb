package flightSearchserice.service.Service;


import flightSearchserice.service.Entity.Flight;
import flightSearchserice.service.Entity.FlightSearchRequest;
import flightSearchserice.service.Entity.FlightSearchResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public interface FlightSearchService {

    public List<FlightSearchResponse> searchFlights(String origin, String destination, LocalDate departureDate, int passengers);



    public Flight getFlightByFlightNumber(String flightNumber);

    public List<Flight> searchAllFlights();

    void updateAvailableSeats(String flightNumber);

    //void handleNewFlightNotification(FlightResponse flightResponse);


    //void addFlight(FlightSearchRequest flightSearchRequest);



}