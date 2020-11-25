package ru.progrm_jarvis.lang.hq9plus.jvmcompiler;

import lombok.experimental.UtilityClass;
import ru.progrm_jarvis.lang.hq9plus.jvmcompiler.ast.HQ9PlusAstNode;

/**
 * Constants of <b>HQ9+ programming language</b>.
 */
@UtilityClass
public class HQ9PlusConst {

    /**
     * Hello world text
     */
    public final String HELLO_WORLD_TEXT = "Hello, world!";

    /**
     * Default amount of bottles of beer meant by {@link HQ9PlusAstNode#NINE}
     */
    public final int DEFAULT_BEER_BOTTLE_COUNT = 99;
}
