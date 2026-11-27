package tn.esprit.ecocityservice.ecocity.exception;

/** The request data is not valid (wrong unit, bad polygon, bad period...). Mapped to HTTP 400 Bad Request. */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
