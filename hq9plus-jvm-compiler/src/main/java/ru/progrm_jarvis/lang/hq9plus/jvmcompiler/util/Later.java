package ru.progrm_jarvis.lang.hq9plus.jvmcompiler.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Value used to combine the value and a function.
 *
 * @param <C> type of already computed value
 * @param <P> type of the second parameter needed for the delayed computation
 * @param <L> type of the value computed later
 */
@Value(staticConstructor = "of")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Later<C, P, L> implements Function<P, L> {

    /**
     * The value already computed
     */
    @NonNull C computedValue;
    /**
     * Function used for performing computations depending the computed value
     */
    @NonNull @Getter(AccessLevel.NONE) BiFunction<C, P, L> computer;

    @Override
    public L apply(final P parameter) {
        return computer.apply(computedValue, parameter);
    }
}
