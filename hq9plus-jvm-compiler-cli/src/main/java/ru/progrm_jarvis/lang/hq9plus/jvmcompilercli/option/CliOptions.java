package ru.progrm_jarvis.lang.hq9plus.jvmcompilercli.option;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility for accessing CLI-options.
 */
@UtilityClass
public class CliOptions {

    /**
     * Gets the option from command-line throwing an exception if it is not present.
     *
     * @param commandLine command-line which may contain the option
     * @param optionName name of the option got
     * @param <T> type of the option got
     * @return the option got from the command-line
     *
     * @throws NoRequiredOptionPresentException if the option is not present
     * @throws ParseException if an exception occurs while trying to get typed option value
     */
    @SuppressWarnings("unchecked")
    public <T> T getRequiredOption(@NonNull final CommandLine commandLine, @NonNull final String optionName)
            throws ParseException {
        if (commandLine.hasOption(optionName)) return (T) commandLine.getParsedOptionValue(optionName);
        throw new NoRequiredOptionPresentException("Option `" + optionName + "` is not present");
    }

    /*
     * Gets the option from command-line wrapping it into an {@link Optional optional}
     * or throwing an exception if it is not present.
     *
     * @param commandLine command-line which may contain the option
     * @param optionName name of the option got
     * @param <T> type of the option got
     * @return the option wrapped in optional got from the command-line
     * @throws NoRequiredOptionPresentException if the option is not present
     * @throws ParseException if an exception occurs while trying to get typed option value
    public <T> Optional<T> getRequiredOptionalOption(@NonNull final CommandLine commandLine,
                                                     @NonNull final String optionName) throws ParseException {
        return Optional.ofNullable(getParsedOptionValue(commandLine, optionName));
    }
     */

    /**
     * Gets the option from command-line mapping it and using default value if the option is not present.
     *
     * @param commandLine command-line which may contain the option
     * @param optionName name of the option got
     * @param valueMapper function used to map and/or verify the value of the property if it is present
     * @param defaultValueSupplier supplier of the default option value used
     * whenever the one is not present in command-line
     * @param <T> type of the option got
     * @param <R> type of the value after the option being mapped
     * @return the option got from the command-line
     *
     * @throws ParseException if an exception occurs while trying to get typed option value
     */
    @SuppressWarnings("unchecked")
    public <T, R> R getOption(@NonNull final CommandLine commandLine, @NonNull final String optionName,
                              @NonNull final Function<T, R> valueMapper, @NonNull Supplier<R> defaultValueSupplier)
            throws ParseException {
        return commandLine.hasOption(optionName)
                ? valueMapper.apply((T) commandLine.getParsedOptionValue(optionName)) : defaultValueSupplier.get();
    }

    /**
     * Gets the option from command-line using default value if the option is not present.
     *
     * @param commandLine command-line which may contain the option
     * @param optionName name of the option got
     * @param valueMapper function used to map and/or verify the value of the property if it is present
     * @param <T> type of the option got
     * @param <R> type of the value after the option being mapped
     * @return the option got from the command-line
     *
     * @throws ParseException if an exception occurs while trying to get typed option value
     */
    @SuppressWarnings("unchecked")
    public <T, R> R getOption(@NonNull final CommandLine commandLine, @NonNull final String optionName,
                              @NonNull final Function<T, R> valueMapper) throws ParseException {
        return valueMapper.apply((T) commandLine.getParsedOptionValue(optionName));
    }

    /**
     * Gets the option from command-line using default value if the option is not present.
     *
     * @param commandLine command-line which may contain the option
     * @param optionName name of the option got
     * @param defaultValueSupplier supplier of the default option value used whenever the one is not present in
     * command-line
     * @param <T> type of the option got
     * @return the option got from the command-line
     *
     * @throws ParseException if an exception occurs while trying to get typed option value
     */
    @SuppressWarnings("unchecked")
    public <T> T getOption(@NonNull final CommandLine commandLine, @NonNull final String optionName,
                           @NonNull Supplier<T> defaultValueSupplier) throws ParseException {
        return commandLine.hasOption(optionName)
                ? (T) commandLine.getParsedOptionValue(optionName) : defaultValueSupplier.get();
    }

    /**
     * Gets the option from command-line.
     *
     * @param commandLine command-line which may contain the option
     * @param optionName name of the option got
     * @param <T> type of the option got
     * @return the option got from the command-line
     *
     * @throws ParseException if an exception occurs while trying to get typed option value
     */
    @SuppressWarnings("unchecked")
    public <T> T getOption(@NonNull final CommandLine commandLine, @NonNull final String optionName)
            throws ParseException {
        return (T) commandLine.getParsedOptionValue(optionName);
    }

    /**
     * Gets the option from command-line wrapping it into an {@link Optional optional}.
     *
     * @param commandLine command-line which may contain the option
     * @param optionName name of the option got
     * @param <T> type of the option got
     * @return the option got from the command-line
     *
     * @throws ParseException if an exception occurs while trying to get typed option value
     */
    public <T> Optional<T> getOptionalOption(@NonNull final CommandLine commandLine,
                                             @NonNull final String optionName) throws ParseException {
        return Optional.ofNullable(getOption(commandLine, optionName));
    }
}
