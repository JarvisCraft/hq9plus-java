package ru.progrm_jarvis.lang.hq9plus.compiler;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.progrm_jarvis.lang.hq9plus.HQ9PlusConst;
import ru.progrm_jarvis.lang.hq9plus.ast.HQ9PlusAstNode;

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
     * @param input input source providing compilation source (normally, source code)
     * @param output output target receiving compilation result
     * @param options compiler options
     *
     * @throws IOException if an exception occurs while reading or writing data
     * @throws ru.progrm_jarvis.lang.hq9plus.ast.HQ9PlusAstParseException if an exception occurs while parsing AST
     */
    void compile(@NonNull I input, @NotNull O output, @Nullable Options options) throws IOException;

    /**
     * Gets default options for the compiler.
     *
     * @param className name of the generated class
     * @return default options for the compiler
     */
    @NotNull static Options defaultOptions(@NonNull final String className) {
        return Options
                .builder()
                .className(className)
                .build();
    }

    /**
     * Options for the compiler.
     */
    @Value
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    class Options {

        /**
         * Name of the generated class
         */
        @NonNull String className;
        /**
         * Text for the hello-world message
         */
        @Builder.Default @NonNull String helloWorldText = HQ9PlusConst.HELLO_WORLD_TEXT,
        /**
         * Name used for generated method of {@link HQ9PlusAstNode#H `H` token} (if any)
         */
        hMethodName = "H",
        /**
         * Name used for generated method of {@link HQ9PlusAstNode#Q `Q` token} (if any)
         */
        qMethodName = "Q",
        /**
         * Name used for generated method of {@link HQ9PlusAstNode#NINE `9` token} (if any)
         */
        nineMethodName = "9",
        /**
         * Name used for generated method of {@link HQ9PlusAstNode#PLUS `+` token} (if any)
         */
        plusMethodName = "+",
        /**
         * Name used for generated counter field (if any)
         */
        counterFieldName = "counter";

        /**
         * {@code true} if the case of source code should be respected and {@code false} otherwise
         *
         * @see HQ9PlusAstNode#match(char, boolean) used to resolve AST nodes depending on this flag
         * @see HQ9PlusAstNode#matchOptionally(char, boolean) used to resolve AST nodes depending on this flag
         */
        @Builder.Default boolean respectCase = true,
        /**
         * {@code true} if the values should be pre-computed when possible and {@code false} otherwise
         *
         * @apiNote this <i>permits</i> the compiler to perform pre-computations but it is not obliged to do it
         */
        allowValuePreComputation = true,
        /**
         * {@code true} if it is fine to allow generation of {@link HQ9PlusAstNode#PLUS `+` code}
         * causing numeric overflow and {@code false} otherwise
         */
        allowNumericOverflow = true;

        /**
         * Amount of bottles of beer initially
         */
        @Builder.Default int bottlesOfBeer = HQ9PlusConst.DEFAULT_BEER_BOTTLE_COUNT;
    }
}
