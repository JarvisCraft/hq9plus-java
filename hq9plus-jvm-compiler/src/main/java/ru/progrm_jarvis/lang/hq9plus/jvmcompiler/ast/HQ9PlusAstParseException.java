package ru.progrm_jarvis.lang.hq9plus.jvmcompiler.ast;

import lombok.NoArgsConstructor;

/**
 * An exception to be thrown in case the AST of <b>HQ9+ programming language</b>
 * is discovered to be corrupted while being parsed.
 */
@NoArgsConstructor
public class HQ9PlusAstParseException extends RuntimeException {

    /**
     * Instantiates a new <b>HQ9+ programming language</b> AST-parse exception.
     *
     * @param message message describing the exception
     */
    public HQ9PlusAstParseException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new <b>HQ9+ programming language</b> AST-parse exception.
     *
     * @param message message describing the exception
     * @param cause cause of the exception
     */
    public HQ9PlusAstParseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new <b>HQ9+ programming language</b> AST-parse exception.
     *
     * @param cause cause of the exception
     */
    public HQ9PlusAstParseException(final Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new <b>HQ9+ programming language</b> AST-parse exception.
     *
     * @param message message describing the exception
     * @param cause cause of the exception
     * @param enableSuppression whether or not suppression is enabled or disabled
     * @param writableStackTrace whether or not the stack trace should be writable
     */
    public HQ9PlusAstParseException(final String message, final Throwable cause, final boolean enableSuppression,
                                    final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
