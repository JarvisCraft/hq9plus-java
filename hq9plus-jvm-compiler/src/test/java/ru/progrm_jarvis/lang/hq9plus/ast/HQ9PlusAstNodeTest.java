package ru.progrm_jarvis.lang.hq9plus.ast;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class HQ9PlusAstNodeTest {

    static Stream<Arguments> provideTestMatchArguments() {
        return Stream.of(
                // respect-case
                arguments('H', true, HQ9PlusAstNode.H),
                arguments('Q', true, HQ9PlusAstNode.Q),
                arguments('9', true, HQ9PlusAstNode.NINE),
                arguments('+', true, HQ9PlusAstNode.PLUS),
                arguments('h', true, null),
                arguments('q', true, null),
                arguments('a', true, null),
                arguments('z', true, null),
                arguments('?', true, null),
                // disrespect-case
                arguments('H', false, HQ9PlusAstNode.H),
                arguments('Q', false, HQ9PlusAstNode.Q),
                arguments('9', false, HQ9PlusAstNode.NINE),
                arguments('+', false, HQ9PlusAstNode.PLUS),
                arguments('h', false, HQ9PlusAstNode.H),
                arguments('q', false, HQ9PlusAstNode.Q),
                arguments('a', false, null),
                arguments('z', false, null),
                arguments('?', false, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestMatchArguments")
    void testMatch(final char token, final boolean respectCase, final HQ9PlusAstNode node) {
        if (node == null) assertThrows(HQ9PlusAstParseException.class, () -> HQ9PlusAstNode.match(token, respectCase));
        else assertEquals(node, HQ9PlusAstNode.match(token, respectCase));
    }

    @ParameterizedTest
    @MethodSource("provideTestMatchArguments")
    void testMatchOptionally(final char token, final boolean respectCase, final HQ9PlusAstNode node) {
        assertEquals(node, HQ9PlusAstNode.matchOptionally(token, respectCase).orElse(null));
    }
}