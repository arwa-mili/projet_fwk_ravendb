package flightsSerice.flight.Exception;

import lombok.Data;

@Data
public class FlightServiceCustomException extends RuntimeException {

    private String errorCode;

    public FlightServiceCustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
