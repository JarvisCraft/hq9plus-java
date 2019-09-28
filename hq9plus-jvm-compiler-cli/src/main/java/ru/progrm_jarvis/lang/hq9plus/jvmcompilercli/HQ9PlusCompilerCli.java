package ru.progrm_jarvis.lang.hq9plus.jvmcompilercli;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.progrm_jarvis.lang.hq9plus.jvmcompilercli.option.CliOptions;
import ru.progrm_jarvis.lang.hq9plus.jvmcompilercli.option.NoRequiredOptionPresentException;

import java.io.File;

import static ru.progrm_jarvis.lang.hq9plus.jvmcompiler.compiler.HQ9PlusCompiler.Options.OptionsBuilder;

/**
 * Command-line interface for {@link ru.progrm_jarvis.lang.hq9plus.jvmcompiler.compiler.HQ9PlusCompiler HQ9+ compiler}.
 */
public interface HQ9PlusCompilerCli {

    /**
     * Gets standard command-line options as a new object so that it can be modified.
     *
     * @return new instance of standard command line options
     */
    @Contract("-> new")
    @NotNull
    static Options standardOptions() {
        return new Options()
                .addOption(Option
                        .builder("in")
                        .longOpt("source-file")
                        // not required, defaults to program argument
                        .hasArg()
                        .type(File.class)
                        .desc("Path to source-code compiled")
                        .build()
                )
                .addOption(Option
                        .builder("out")
                        .longOpt("target-file")
                        // not required, defaults to current directory
                        .hasArg()
                        .type(File.class)
                        .desc("Path to the file in which the compiled class should be saved")
                        .build()
                )
                // compiler-specific options
                .addOption("v", "verbose", false, "Flag enabling verbose output")
                // additional flags
                .addOption("drc", "disrespect-case", false, "Flag disabling case-respect for the source code")
                .addOption("apc", "allow-pre-computation", false, "Flag allowing possible pre-computations")
                .addOption("ano", "allow-numeric-overflow", false, "Flag allowing numeric overflow of the counter")
                // standard but overridable values
                .addOption("hwt", "hello-world-text", true, "Text to get outputted for `H`-instruction")
                .addOption("bbc", "bottles-of-beer", true, "Initial count of `9`-instruction's bottles of beer")
                // names of synthetic elements
                .addOption(
                        "hmn", "h-method-name", true,
                        "Name used for generated method performing `H`-instruction (if any)"
                )
                .addOption(
                        "qmn", "q-method-name", true,
                        "Name used for generated method performing `Q`-instruction (if any)"
                )
                .addOption(
                        "nmn", "nine-method-name", true,
                        "Name used for generated method performing `(`-instruction (if any)"
                )
                .addOption(
                        "pmn", "plus-method-name", true,
                        "Name used for generated method performing `+`-instruction (if any)"
                )
                .addOption("cfn", "counter-field-name", true, "Name used for generated counter field (if any)");
    }

    /**
     * Parses {@link FileSourceAndTarget source and target} of from the given command line.
     *
     * @param commandLine command-line whose contents should be parsed
     * @return source and target found in the command line
     * @throws ParseException if an exception occurs while trying to parse the value of specific type
     * @throws IllegalArgumentException if some option is of valid type but does not follow some criteria
     */
    static FileSourceAndTarget parseSourceAndTarget(@NonNull final CommandLine commandLine) throws ParseException {
        val sourceFile = CliOptions.getOption(commandLine, "in", (final File option) -> {
            if (!option.isFile()) throw new IllegalArgumentException(
                    "Source file should be an existing file (given: \"" + option.getAbsolutePath() + "\")"
            );
            return option;
        }, () -> {
            // list is used instead of array as it is faster in default implementation
            val commandLineArguments = commandLine.getArgList();
            if (commandLineArguments.isEmpty()) throw new NoRequiredOptionPresentException(
                    "Source file should be specified explicitly or via -in"
            );
            return new File(commandLineArguments.get(0));
        });

        return CliOptions.getOption(commandLine, "out", (final File option) -> {
            if (!option.isDirectory()) throw new IllegalArgumentException(
                    "Target directory should be a directory (given: \"" + option.getAbsolutePath() + "\")"
            );
            return FileSourceAndTarget.of(sourceFile, option);
        }, () -> FileSourceAndTarget.ofSource(sourceFile));
    }

    /**
     * Parses command-line arguments into the given {@link OptionsBuilder}.
     *
     * @param commandLine command-line whose contents should be parsed
     * @param compilerOptions compiler options in form of builder
     * @return parsed compiler options as a builder so that they may be modified
     * @throws ParseException if an exception occurs while parsing the options
     */
    @Contract("_, _ -> param2")
    static OptionsBuilder parseOptions(@NonNull final CommandLine commandLine,
                                       @NonNull final OptionsBuilder compilerOptions)
            throws ParseException {
        // compiler-specific options
        if (commandLine.hasOption('v')) compilerOptions.verbose(true);
        // additional flags
        if (commandLine.hasOption("apc")) compilerOptions.allowValuePreComputation(true);
        if (commandLine.hasOption("ano")) compilerOptions.allowNumericOverflow(true);
        if (commandLine.hasOption("ano")) compilerOptions.respectCase(false);
        // standard but overridable values
        CliOptions.<String>getOptionalOption(commandLine, "hwt").ifPresent(compilerOptions::helloWorldText);
        CliOptions.<Integer>getOptionalOption(commandLine, "bbc").ifPresent(compilerOptions::bottlesOfBeer);
        // names of synthetic elements
        CliOptions.<String>getOptionalOption(commandLine, "hmn").ifPresent(compilerOptions::hMethodName);
        CliOptions.<String>getOptionalOption(commandLine, "qmn").ifPresent(compilerOptions::qMethodName);
        CliOptions.<String>getOptionalOption(commandLine, "9mn").ifPresent(compilerOptions::nineMethodName);
        CliOptions.<String>getOptionalOption(commandLine, "+mn").ifPresent(compilerOptions::plusMethodName);
        CliOptions.<String>getOptionalOption(commandLine, "cfn").ifPresent(compilerOptions::counterFieldName);

        return compilerOptions;
    }

    /**
     * Container for source file and target file.
     */
    @Value
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    class FileSourceAndTarget {
        /**
         * Source file (the one with source code)
         */
        @NonNull final File sourceFile,
        /**
         * Target directory (the one in which compiled source will be)
         */
        targetDirectory;

        /**
         * Creates a new source-and-target for the given source file and target directory.
         *
         * @param sourceFile source file (the one with source code)
         * @param targetDirectory directory (the one in which compiled source will be)
         * @return created source-and-target object
         */
        public static FileSourceAndTarget of(@NonNull final File sourceFile, @NonNull final File targetDirectory) {
            return new FileSourceAndTarget(sourceFile.getAbsoluteFile(), targetDirectory.getAbsoluteFile());
        }

        /**
         * Creates a new source-and-target for the given source file using its directory as target.
         *
         * @param sourceFile source file (the one with source code)
         * @return created source-and-target object with target directory being the parent of this file
         */
        public static FileSourceAndTarget ofSource(@NonNull final File sourceFile) {
            val absoluteSourceFile = sourceFile.getAbsoluteFile();
            return of(absoluteSourceFile, absoluteSourceFile.getParentFile());
        }
    }
}
