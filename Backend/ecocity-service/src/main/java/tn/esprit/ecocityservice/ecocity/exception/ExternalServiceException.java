package tn.esprit.ecocityservice.ecocity.exception;

/** Another microservice could not be reached. Mapped to HTTP 503 Service Unavailable. */
public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message);
    }
}
