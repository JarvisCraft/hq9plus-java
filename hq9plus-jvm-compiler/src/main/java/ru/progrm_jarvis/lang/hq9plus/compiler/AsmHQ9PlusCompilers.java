package ru.progrm_jarvis.lang.hq9plus.compiler;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * Factory of standard {@link HQ9PlusCompiler} based on {@link AbstractAsmHQ9PlusCompiler} for specific IO-types.
 */
@UtilityClass
public class AsmHQ9PlusCompilers {

    /**
     * Creates a new {@link HQ9PlusCompiler} based on {@link AbstractAsmHQ9PlusCompiler} using
     * {@link InputStream} and {@link OutputStream} as its input and output respectively.
     *
     * @return specific compiler
     */
    public HQ9PlusCompiler<InputStream, OutputStream> streamBased() {
        return new AbstractAsmHQ9PlusCompiler<InputStream, OutputStream>() {
            @Override
            protected void write(@NotNull final byte[] bytes,
                                 @NotNull final OutputStream output) throws IOException {
                try (val outputStream = new BufferedOutputStream(output)) {
                    outputStream.write(bytes);
                }
            }

            @Override
            protected Reader toReader(@NotNull final InputStream input) {
                return new BufferedReader(new InputStreamReader(input));
            }
        };
    }

    /**
     * Creates a new {@link HQ9PlusCompiler} based on {@link AbstractAsmHQ9PlusCompiler} using
     * {@link BufferedInputStream} and {@link BufferedOutputStream} as its input and output respectively.
     *
     * @return specific compiler
     */
    public HQ9PlusCompiler<BufferedInputStream, BufferedOutputStream> bufferedStreamBased() {
        return new AbstractAsmHQ9PlusCompiler<BufferedInputStream, BufferedOutputStream>() {
            @Override
            protected void write(@NotNull final byte[] bytes,
                                 @NotNull final BufferedOutputStream output) throws IOException {
                //noinspection TryFinallyCanBeTryWithResources unable to use non-local var variant with old Java ver.
                try {
                    output.write(bytes);
                } finally {
                    output.close();
                }
            }

            @Override
            protected Reader toReader(@NotNull final BufferedInputStream input) {
                return new BufferedReader(new InputStreamReader(input));
            }
        };
    }
}
