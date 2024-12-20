package com.russell.flowlog;

import javax.annotation.Nonnull;

/**
 * The root for all exceptions originated in the app
 */
public class FlowLogDomainException extends RuntimeException {

    public FlowLogDomainException(final String message) {
        super(message);
    }

    public FlowLogDomainException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
