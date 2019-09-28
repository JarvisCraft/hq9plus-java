package ru.progrm_jarvis.lang.hq9plus.jvmcompilercli;

import lombok.extern.java.Log;
import lombok.val;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import ru.progrm_jarvis.lang.hq9plus.jvmcompiler.compiler.AsmHQ9PlusCompilers;
import ru.progrm_jarvis.lang.hq9plus.jvmcompiler.compiler.HQ9PlusCompiler;
import ru.progrm_jarvis.lang.hq9plus.jvmcompilercli.option.NoRequiredOptionPresentException;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;

import static ru.progrm_jarvis.lang.hq9plus.jvmcompilercli.HQ9PlusCompilerCli.*;

/**
 * Simple implementation of {@link HQ9PlusCompilerCli HQ9+ compiler Command-Line Interface}.
 */
@Log(topic = "HQ9+ Compiler")
public class SimpleHQ9PlusCompilerCli implements HQ9PlusCompilerCli {

    /**
     * Main method of this Command-Line Interface of HQ9+ compiler. Entry point of the command-line APP.
     *
     * @param args command line arguments passed to the program
     */
    public static void main(final String... args) {
        final HQ9PlusCompiler.Options options;
        final String className;
        final File sourceFile, targetDirectory;
        {
            final HQ9PlusCompiler.Options.OptionsBuilder optionsBuilder;
            try {
                val commandLine = new DefaultParser().parse(standardOptions(), args);

                {
                    val sourceAndTarget = parseSourceAndTarget(commandLine);
                    sourceFile = sourceAndTarget.getSourceFile();
                    targetDirectory = sourceAndTarget.getTargetDirectory();
                }

                optionsBuilder = parseOptions(commandLine, HQ9PlusCompiler.Options.builder());
            } catch (final NoRequiredOptionPresentException e) {
                log.log(Level.SEVERE, "Missing a needed parameter", e);
                System.exit(1);
                return;
            } catch (final ParseException e) {
                log.log(Level.SEVERE, "Given parameters cannot be parsed", e);
                System.exit(1);
                return;
            } catch (final IllegalArgumentException e) {
                log.log(Level.SEVERE, "Given parameter is not valid", e);
                System.exit(1);
                return;
            }

            // configure the class name depending on source file name
            val sourceFileName = sourceFile.getName();
            val dotIndex = sourceFileName.indexOf('.');

            options = optionsBuilder
                    .className(className = dotIndex == -1 ? sourceFileName : sourceFileName.substring(0, dotIndex))
                    .build();
        }
        if (options.isVerbose()) {
            log.setLevel(Level.ALL);
            log.info("Using verbose output");
        }

        log.log(Level.CONFIG, () -> "Using compiler-options: " + options);
        val targetFile = new File(targetDirectory, className + ".class");
        log.log(
                Level.CONFIG,
                () -> "Compiling from:\n  " + sourceFile.getAbsolutePath()
                        + "\n  to" + targetDirectory.getAbsolutePath()
        );

        if (!targetDirectory.isDirectory()) try {
            Files.createDirectories(targetDirectory.toPath());
        } catch (final IOException e) {
            log.log(
                    Level.SEVERE,
                    "An IO-exception occurred while trying to create target-directory " + targetDirectory + ':', e
            );
            System.exit(1);
            return;
        }

        // Lombok's val dies here
        try (
                final InputStream inputStream = new FileInputStream(sourceFile);
                final OutputStream outputStream = new FileOutputStream(targetFile)
        ) {
            log.fine("Starting compilation...");
            AsmHQ9PlusCompilers.streamBased().compile(inputStream, outputStream, options);
            log.fine("Compiled");
        } catch (final IOException e) {
            log.log(
                    Level.SEVERE,
                    "An IO-exception occurred while trying to compile class " + sourceFile + " into directory", e
            );
        }
    }
}
