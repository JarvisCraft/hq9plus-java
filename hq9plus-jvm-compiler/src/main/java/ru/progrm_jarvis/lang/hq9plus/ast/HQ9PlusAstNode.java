package ru.progrm_jarvis.lang.hq9plus.ast;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * AST-nodes of <b>HQ9+ programming language</b> AST.
 */
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum HQ9PlusAstNode {
    /**
     * {@code H} node
     */
    H('H', "Prints \"Hello, world!\""),
    /**
     * {@code Q} node
     */
    Q('Q', "Prints the entire text of the source code file."),
    /**
     * {@code 9} node
     */
    NINE('9', "Prints the complete canonical lyrics to \"99 Bottles of Beer on the Wall\""),
    /**
     * {@code +} node
     */
    PLUS('+', "Increments the accumulator.");

    /**
     * Token associated with this node
     */
    @NonNull char token;
    /**
     * Description of this node's semantics
     */
    @NonNull String description;

    /**
     * Matches the AST-node by the given token.
     *
     * @param token token by which the AST-node should be resolved
     * @param respectCase {@code true} if the case should be respected and {@code false} otherwise
     * @return AST-node found for the given token
     *
     * @throws HQ9PlusAstParseException if an unknown token gets passed
     */
    @NotNull public static HQ9PlusAstNode match(final char token, final boolean respectCase) {
        if (respectCase) switch (token) {
            case 'H': return H;
            case 'Q': return Q;
            case '9': return NINE;
            case '+': return PLUS;
            default: throw new HQ9PlusAstParseException("Unknown token: " + token);
        } else switch (token) {
            case 'H': case 'h': return H;
            case 'Q': case 'q': return Q;
            case '9': return NINE;
            case '+': return PLUS;
            default: throw new HQ9PlusAstParseException("Unknown token: " + token);
        }
    }

    /**
     * Matches the AST-node by the given token if it is possible.
     *
     * @param token token by which the AST-node should be resolved
     * @param respectCase {@code true} if the case should be respected and {@code false} otherwise
     * @return AST-node found for the given token wrapped in optional or an empty one
     * if there is no associated AST for the given token
     */
    @NotNull public static Optional<HQ9PlusAstNode> matchOptionally(final char token, final boolean respectCase) {
        if (respectCase) switch (token) {
            case 'H': return Optional.of(H);
            case 'Q': return Optional.of(Q);
            case '9': return Optional.of(NINE);
            case '+': return Optional.of(PLUS);
            default: return Optional.empty();
        } else switch (token) {
            case 'H': case 'h': return Optional.of(H);
            case 'Q': case 'q': return Optional.of(Q);
            case '9': return Optional.of(NINE);
            case '+': return Optional.of(PLUS);
            default: return Optional.empty();
        }
    }
}
