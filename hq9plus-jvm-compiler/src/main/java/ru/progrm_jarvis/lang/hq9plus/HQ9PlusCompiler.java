package ru.progrm_jarvis.lang.hq9plus;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Compiler for <b>HQ9+ programming language</b>.
 *
 * @param <I> type of input source providing compilation source (normally, source code)
 * @param <O> type of output target receiving compilation result
 */
public interface HQ9PlusCompiler<I, O> {

    /**
     * Compiles the source from the input passing the result into output.
     *
     * @param className name of the compiled class
     * @param input input source providing compilation source (normally, source code)
     * @param output output target receiving compilation result
     *
     * @throws IOException if an exception occurs while reading or writing data
     * @throws ru.progrm_jarvis.lang.hq9plus.ast.HQ9PlusAstParseException if an exception occurs while parsing AST
     */
    void compile(@NonNull String className, @NonNull I input, @NotNull O output) throws IOException;
}
