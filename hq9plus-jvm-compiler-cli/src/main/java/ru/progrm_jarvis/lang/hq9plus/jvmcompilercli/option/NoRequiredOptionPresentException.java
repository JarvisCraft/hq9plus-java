package ru.progrm_jarvis.lang.hq9plus.jvmcompilercli.option;

import lombok.NoArgsConstructor;

/**
 * An exception to be thrown whenever a required {@link org.apache.commons.cli.Option option}
 * is not present in the {@link org.apache.commons.cli.CommandLine command line}.
 */
@NoArgsConstructor
public class NoRequiredOptionPresentException extends RuntimeException {

    /**
     * Instantiates a new no-required-option-present exception.
     *
     * @param message message describing the exception
     */
    public NoRequiredOptionPresentException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new no-required-option-present exception.
     *
     * @param message message describing the exception
     * @param cause cause of the exception
     */
    public NoRequiredOptionPresentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new no-required-option-present exception.
     *
     * @param cause cause of the exception
     */
    public NoRequiredOptionPresentException(final Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new no-required-option-present exception.
     *
     * @param message message describing the exception
     * @param cause cause of the exception
     * @param enableSuppression whether or not suppression is enabled or disabled
     * @param writableStackTrace whether or not the stack trace should be writable
     */
    public NoRequiredOptionPresentException(final String message, final Throwable cause, final boolean enableSuppression,
                                            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
