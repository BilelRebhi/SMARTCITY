package com.campusverse.ecocity.exception;

/** A business rule was broken (duplicate, invalid status change, still in use...). Mapped to HTTP 409 Conflict. */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
