package tn.esprit.ecocityservice.ecocity.exception;

/** Mapped to HTTP 404 Not Found. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
