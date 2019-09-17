package ru.progrm_jarvis.lang.hq9plus;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Compiler for <b>HQ9+ programming language</b>
 *
 * @param <I> type of input source providing compilation source (normally, source code)
 * @param <O> type of output target receiving compilation result
 */
public interface HQ9PlusCompiler<I, O> extends BiConsumer<I, O> {

    /**
     * Compiles the source from the input passing the result into output.
     *
     * @param input input source providing compilation source (normally, source code)
     * @param output output target receiving compilation result
     */
    void compile(@NonNull I input, @NotNull O output);

    @Override
    default void accept(@NotNull final I input, @NotNull final O output) {
        compile(input, output);
    }
}
