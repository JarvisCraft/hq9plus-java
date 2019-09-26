package ru.progrm_jarvis.lang.hq9plus;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import ru.progrm_jarvis.lang.hq9plus.ast.HQ9PlusAstNode;
import ru.progrm_jarvis.lang.hq9plus.util.Later;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.math.BigInteger;

import static org.objectweb.asm.Opcodes.*;

/**
 * An abstract implementation of {@link HQ9PlusCompiler} based on <i>ObjectWeb ASM</i>.
 *
 * @param <I> type of input source providing compilation source (normally, source code)
 * @param <O> type of output target receiving compilation result
 */
@ToString
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractAsmHQ9PlusCompiler<I, O> implements HQ9PlusCompiler<I, O> {

    /**
     * Type of {@link Object} class
     */
    protected static final Type OBJECT_TYPE = Type.getType(Object.class),
    /**
     * Type of {@link String} class
     */
    STRING_TYPE = Type.getType(String.class),
    /**
     * Type of {@link System} class
     */
    SYSTEM_TYPE = Type.getType(System.class),
    /**
     * Type of {@link PrintStream} class
     */
    PRINT_STREAM_TYPE = Type.getType(PrintStream.class),
    /**
     * Type of {@link BigInteger} class
     */
    BIG_INTEGER_TYPE = Type.getType(BigInteger.class),
    /**
     * Type of {@link Object}{@code []} class
     */
    OBJECT_ARRAY_TYPE = Type.getType(Object[].class),
    /**
     * Type of {@link String}{@code []} class
     */
    STRING_ARRAY_TYPE = Type.getType(String[].class),
    /**
     * Type of {@code void}{@code ()} method
     */
    VOID_METHOD_TYPE = Type.getMethodType(Type.VOID_TYPE),
    /**
     * Type of {@code void}{@code (}{@link String}{@code [])} method
     */
    VOID_STRING_ARRAY_METHOD_TYPE = Type.getMethodType(Type.VOID_TYPE, STRING_ARRAY_TYPE),
    /**
     * Type of {@link BigInteger}{@code (}{@link BigInteger}{@code )} method
     */
    BIG_INTEGER_BIG_INTEGER_METHOD_TYPE = Type.getMethodType(BIG_INTEGER_TYPE, BIG_INTEGER_TYPE),
    /**
     * Type of {@code void(int)} method
     */
    VOID_INT_METHOD_TYPE = Type.getMethodType(Type.VOID_TYPE, Type.INT_TYPE),
    /**
     * Type of {@code void}{@code (}{@link String}{@code )} method
     */
    VOID_STRING_METHOD_TYPE = Type.getMethodType(Type.VOID_TYPE, STRING_TYPE);

    /**
     * Name of the constructor method
     */
    protected static final String CONSTRUCTOR_METHOD_NAME = "<init>",
    /**
     * Name of the main class method
     */
    MAIN_METHOD_NAME = "main",
    /**
     * Name of {@link PrintStream#print(int)} and {@link PrintStream#print(int)} methods
     */
    PRINT_METHOD_NAME = "print",
    /**
     * Name of {@link BigInteger#add(BigInteger)} method
     */
    ADD_METHOD_NAME = "add",
    /**
     * Name of {@link PrintStream#println()} {@link PrintStream#println(String)} methods
     */
    PRINTLN_METHOD_NAME = "println",
    /**
     * Name of {@link System#out} field
     */
    OUT_FIELD_NAME = "out",
    /**
     * Name of {@link BigInteger#ONE} constant
     */
    ONE_FIELD_NAME = "ONE",
    /**
     * Internal name of {@link Object} class
     */
    OBJECT_INTERNAL_NAME = OBJECT_TYPE.getInternalName(),
    /**
     * Internal name of {@link System} class
     */
    SYSTEM_INTERNAL_NAME = SYSTEM_TYPE.getInternalName(),
    /**
     * Internal name of {@link PrintStream} class
     */
    PRINT_STREAM_INTERNAL_NAME = PRINT_STREAM_TYPE.getInternalName(),
    /**
     * Internal name of {@link BigInteger} class
     */
    BIG_INTEGER_INTERNAL_NAME = BIG_INTEGER_TYPE.getInternalName(),
    /**
     * Descriptor of {@code long}
     */
    LONG_DESCRIPTOR = Type.LONG_TYPE.getDescriptor(),
    /**
     * Descriptor of {@link Object} class
     */
    OBJECT_DESCRIPTOR = OBJECT_TYPE.getDescriptor(),
    /**
     * Descriptor of {@link System} class
     */
    SYSTEM_DESCRIPTOR = SYSTEM_TYPE.getDescriptor(),
    /**
     * Descriptor of {@link PrintStream} class
     */
    PRINT_STREAM_DESCRIPTOR = PRINT_STREAM_TYPE.getDescriptor(),
    /**
     * Descriptor of {@link Object}{@code []} class
     */
    OBJECT_ARRAY_DESCRIPTOR = OBJECT_ARRAY_TYPE.getDescriptor(),
    /**
     * Descriptor of {@link String}{@code []} class
     */
    STRING_ARRAY_DESCRIPTOR = STRING_ARRAY_TYPE.getDescriptor(),
    /**
     * Descriptor of {@link BigInteger} class
     */
    BIG_INTEGER_DESCRIPTOR = BIG_INTEGER_TYPE.getDescriptor(),
    /**
     * Descriptor of {@code void}{@code ()} method
     */
    VOID_METHOD_DESCRIPTOR = VOID_METHOD_TYPE.getDescriptor(),
    /**
     * Descriptor of {@code void(int)} method
     */
    VOID_INT_METHOD_DESCRIPTOR = VOID_INT_METHOD_TYPE.getDescriptor(),
    /**
     * Descriptor of {@code void}{@code (}{@link String}{@code [])} method
     */
    VOID_STRING_METHOD_DESCRIPTOR = VOID_STRING_METHOD_TYPE.getDescriptor(),
    /**
     * Descriptor of {@code void}{@code (}{@link String}{@code [])} method
     */
    VOID_STRING_ARRAY_METHOD_DESCRIPTOR = VOID_STRING_ARRAY_METHOD_TYPE.getDescriptor(),
    /**
     * Descriptor of {@link BigInteger}{@code (}{@link BigInteger}{@code )} method
     */
    BIG_INTEGER_BIG_INTEGER_METHOD_DESCRIPTOR = BIG_INTEGER_BIG_INTEGER_METHOD_TYPE.getDescriptor();

    /**
     * {@link Object}-array with its only value being {@link org.objectweb.asm.Opcodes#INTEGER}
     */
    private static final Object[] FRAME_ITEM_INTEGER_OBJECT_ARRAY = new Object[]{INTEGER},
    /**
     * {@link Object}-array with its only value being {@link #PRINT_STREAM_INTERNAL_NAME}
     */
    PRINT_STREAM_INTERNAL_NAME_OBJECT_ARRAY = {PRINT_STREAM_INTERNAL_NAME};

    /**
     * Pushes the {@code int} value onto the stack effectively.
     *
     * @param method method in whose frame the {@code int}-value should be pushed onto the stack
     * @param value value to push onto the stack
     */
    protected static void pushInt(final MethodVisitor method, final int value) {
        switch (value) {
            case -1: {
                method.visitInsn(ICONST_M1);
                break;
            }
            case 0: {
                method.visitInsn(ICONST_0);
                break;
            }
            case 1: {
                method.visitInsn(ICONST_1);
                break;
            }
            case 2: {
                method.visitInsn(ICONST_2);
                break;
            }
            case 3: {
                method.visitInsn(ICONST_3);
                break;
            }
            case 4: {
                method.visitInsn(ICONST_4);
                break;
            }
            case 5: {
                method.visitInsn(ICONST_5);
                break;
            }
            default: {
                if (value <= Byte.MAX_VALUE) method.visitIntInsn(BIPUSH, value);
                else if (value <= Short.MAX_VALUE) method.visitIntInsn(SIPUSH, value);
                else method.visitLdcInsn(value);
            }
        }
    }

    /**
     * Implements the method printing the given text.
     *
     * @param classWriter class-writer used to implement the method
     * @param methodName name of the generated method
     * @param text text which should be printed
     */
    protected static void implementTextOutputMethod(@NonNull final ClassWriter classWriter,
                                                    @NonNull final String methodName,
                                                    @NonNull final String text) {
        val method = classWriter.visitMethod(
                ACC_PROTECTED | ACC_STATIC | ACC_SYNTHETIC, methodName,
                VOID_METHOD_DESCRIPTOR, null /* no generics */, null /* no exceptions */
        );

        method.visitCode();
        method.visitFieldInsn(
                GETSTATIC, SYSTEM_INTERNAL_NAME, OUT_FIELD_NAME, PRINT_STREAM_DESCRIPTOR
        );
        method.visitLdcInsn(text);
        method.visitMethodInsn(
                INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                PRINTLN_METHOD_NAME, VOID_STRING_METHOD_DESCRIPTOR, false
        );
        method.visitInsn(RETURN);

        method.visitMaxs(2, 0);
        method.visitEnd();
    }

    /**
     * Implements the method printing <i><b>N</b>-bottles of beer</i> song.
     *
     * @param classWriter class-writer used to implement the method
     * @param methodName name of the generated method
     * @param initialBottles initial amount of bottles of beer
     */
    @SuppressWarnings("Duplicates") // those are just similar bytecode instructions generated
    protected static void implementNBottlesOfBeerMethod(@NonNull final ClassWriter classWriter,
                                                        @NonNull final String methodName,
                                                        final int initialBottles) {
        if (initialBottles < 1) throw new IllegalArgumentException(
                "There is no need to sing about bottles of beer if there isn't enough of those (" + initialBottles + ')'
        );

        val method = classWriter.visitMethod(
                ACC_PUBLIC | ACC_STATIC | ACC_SYNTHETIC, methodName,
                VOID_METHOD_DESCRIPTOR, null /* no generics */, null /* no exceptions */
        );
        method.visitCode();

        // push the `System#out` onto the stack
        method.visitFieldInsn(
                GETSTATIC, SYSTEM_INTERNAL_NAME, OUT_FIELD_NAME, PRINT_STREAM_DESCRIPTOR
        );
        if (initialBottles > 1) {
            // loop to print those happy messages about multiple bottles

            // push the initial counter value ...
            pushInt(method, initialBottles);
            // ... and store it in the local variable
            method.visitVarInsn(ISTORE, 0);

            val loopBeginLabel = new Label();
            method.visitLabel(loopBeginLabel);
            method.visitFrame(F_FULL, 1, FRAME_ITEM_INTEGER_OBJECT_ARRAY, 1, PRINT_STREAM_INTERNAL_NAME_OBJECT_ARRAY);

            method.visitVarInsn(ILOAD, 0);
            method.visitInsn(ICONST_1);

            val loopEndLabel = new Label();
            // loop check to end iteration once `1` is reached
            method.visitJumpInsn(IF_ICMPEQ, loopEndLabel);

            { // the very method body
                method.visitVarInsn(ILOAD, 0);
                // `out`, counter -> `out`, counter, `out`, counter
                method.visitInsn(DUP2);
                method.visitMethodInsn(
                        INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                        PRINT_METHOD_NAME, VOID_INT_METHOD_DESCRIPTOR, false
                );

                // `out`, counter -> counter, `out`
                method.visitInsn(SWAP);
                // counter, `out` -> counter, `out`, `out`
                method.visitInsn(DUP);
                method.visitLdcInsn(" bottles of beer on the wall, ");
                method.visitMethodInsn(
                        INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                        PRINT_METHOD_NAME, VOID_STRING_METHOD_DESCRIPTOR, false
                );

                // counter, `out` -> `out`, counter, `out`
                method.visitInsn(DUP_X1);
                // `out`, counter, `out` -> `out`, `out`, counter
                method.visitInsn(SWAP);
                method.visitMethodInsn(
                        INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                        PRINT_METHOD_NAME, VOID_INT_METHOD_DESCRIPTOR, false
                );

                // `out` -> `out`, `out`
                method.visitInsn(DUP);
                method.visitLdcInsn(" bottles of beer.");
                method.visitMethodInsn(
                        INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                        PRINTLN_METHOD_NAME, VOID_STRING_METHOD_DESCRIPTOR, false
                );

                // `out` -> `out`, `out`
                method.visitInsn(DUP);
                method.visitLdcInsn("Take one down and pass it around, ");
                method.visitMethodInsn(
                        INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                        PRINT_METHOD_NAME, VOID_STRING_METHOD_DESCRIPTOR, false
                );

                // `out` -> `out`, `out`
                method.visitInsn(DUP);

                // decrement the counter
                method.visitIincInsn(0, -1);
                // get the value of the counter
                // `out`, `out` -> `out`, `out`, counter 
                method.visitVarInsn(ILOAD, 0);
                method.visitMethodInsn(
                        INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                        PRINT_METHOD_NAME, VOID_INT_METHOD_DESCRIPTOR, false
                );

                // `out` -> `out`, `out`
                method.visitInsn(DUP);
                method.visitLdcInsn(" bottles of beer on the wall.");
                method.visitMethodInsn(
                        INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                        PRINTLN_METHOD_NAME, VOID_STRING_METHOD_DESCRIPTOR, false
                );

                // `out` -> `out`, `out`
                method.visitInsn(DUP);
                method.visitMethodInsn(
                        INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                        PRINTLN_METHOD_NAME, VOID_METHOD_DESCRIPTOR, false
                );

                method.visitJumpInsn(GOTO, loopBeginLabel);
            }

            method.visitLabel(loopEndLabel);
            method.visitFrame(F_SAME1, 0, null, 1, PRINT_STREAM_INTERNAL_NAME_OBJECT_ARRAY);
        }

        // write ending lines

        // write text line #1
        method.visitInsn(DUP);
        method.visitLdcInsn("1 bottle of beer on the wall, 1 bottle of beer.");
        method.visitMethodInsn(
                INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                PRINTLN_METHOD_NAME, VOID_STRING_METHOD_DESCRIPTOR, false
        );
        // write text line #2
        method.visitInsn(DUP);
        method.visitLdcInsn("Take one down and pass it around, no more bottles of beer on the wall.");
        method.visitMethodInsn(
                INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                PRINTLN_METHOD_NAME, VOID_STRING_METHOD_DESCRIPTOR, false
        );
        // write new line
        method.visitInsn(DUP);
        method.visitMethodInsn(
                INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                PRINTLN_METHOD_NAME, VOID_METHOD_DESCRIPTOR, false
        );
        // write text line #3
        method.visitInsn(DUP);
        method.visitLdcInsn("No more bottles of beer on the wall, no more bottles of beer.");
        method.visitMethodInsn(
                INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                PRINTLN_METHOD_NAME, VOID_STRING_METHOD_DESCRIPTOR, false
        );
        // write text line #4 (prefix)
        method.visitInsn(DUP);
        method.visitLdcInsn("Go to the store and buy some more, ");
        method.visitMethodInsn(
                INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                PRINT_METHOD_NAME, VOID_STRING_METHOD_DESCRIPTOR, false
        );

        // write text line #4 (initial amount of bottles)
        method.visitInsn(DUP);
        pushInt(method, initialBottles);
        method.visitMethodInsn(
                INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                PRINT_METHOD_NAME, VOID_INT_METHOD_DESCRIPTOR, false
        );

        // write text line #4 (suffix)
        // no dup as the last use of `out`
        method.visitLdcInsn(" bottles of beer on the wall.");
        method.visitMethodInsn(
                INVOKEVIRTUAL, PRINT_STREAM_INTERNAL_NAME,
                PRINTLN_METHOD_NAME, VOID_STRING_METHOD_DESCRIPTOR, false
        );

        // return from the method
        method.visitInsn(RETURN);

        method.visitMaxs(4, 1 /* counter */);
        method.visitEnd();
    }

    /**
     * Implements the counter-incrementing method using the given class-writer.
     *
     * @param classWriter class-writer used to implement the method
     * @param counterFieldOwnerInternalName internal name of the class containing the static field whose value gets incremented
     * @param incrementMethodName name of the generated method
     * @param counterFieldName name of the generated counter field
     * @param allowNumericOverflow flag indicating whether it is fine to generate code causing numeric overflow
     */
    protected static void implementIncrementerMethod(@NonNull final ClassWriter classWriter,
                                                     @NonNull final String counterFieldOwnerInternalName,
                                                     @NonNull final String incrementMethodName,
                                                     @NonNull final String counterFieldName,
                                                     final boolean allowNumericOverflow) {
        val method = classWriter.visitMethod(
                ACC_PROTECTED | ACC_STATIC | ACC_SYNTHETIC, incrementMethodName,
                VOID_METHOD_DESCRIPTOR, null /* no generics */, null /* no exceptions */
        );
        method.visitCode();

        if (allowNumericOverflow) {
            classWriter.visitField(
                    /* according to spec the field should be invisible */
                    ACC_PRIVATE | ACC_STATIC | ACC_SYNTHETIC,
                    counterFieldName, LONG_DESCRIPTOR, null /* no generics */, 0L /* explicit long type */
            ).visitEnd();

            // -> counter_least, counter_most
            method.visitFieldInsn(
                    GETSTATIC, counterFieldOwnerInternalName, counterFieldName, LONG_DESCRIPTOR
            );
            // counter_least, counter_most -> counter_least, counter_most, 1L
            method.visitInsn(LCONST_1);
            method.visitInsn(LADD);
            method.visitFieldInsn(
                    PUTSTATIC, counterFieldOwnerInternalName, counterFieldName, LONG_DESCRIPTOR
            );

            method.visitInsn(RETURN);
            method.visitMaxs(2 * 2 /* = 4, LONGS ARE FAT :( (that's why they are called longs) */, 0);
            method.visitEnd();
        } else {
            classWriter.visitField(
                    /* according to spec the field should be invisible */
                    ACC_PRIVATE | ACC_STATIC | ACC_SYNTHETIC,
                    counterFieldName, BIG_INTEGER_DESCRIPTOR, null /* no generics */, null /* null by default */
            ).visitEnd();

            method.visitFieldInsn(GETSTATIC, counterFieldOwnerInternalName, counterFieldName, BIG_INTEGER_DESCRIPTOR);

            val ifEndLabel = new Label();
            method.visitInsn(DUP); // will be more efficient in all cases instead of the first call
            method.visitJumpInsn(IFNONNULL, ifEndLabel);
            // initialize the field with `BigInteger#ONE` ...
            method.visitInsn(POP); // get rid of current field's value reference
            method.visitFieldInsn(GETSTATIC, BIG_INTEGER_INTERNAL_NAME, ONE_FIELD_NAME, BIG_INTEGER_DESCRIPTOR);
            method.visitFieldInsn(PUTSTATIC, counterFieldOwnerInternalName, counterFieldName, BIG_INTEGER_DESCRIPTOR);
            method.visitInsn(RETURN);
            method.visitLabel(ifEndLabel);
            // ... or else increment it
            method.visitFieldInsn(GETSTATIC, BIG_INTEGER_INTERNAL_NAME, ONE_FIELD_NAME, BIG_INTEGER_DESCRIPTOR);
            method.visitMethodInsn(
                    INVOKEVIRTUAL, BIG_INTEGER_INTERNAL_NAME,
                    ADD_METHOD_NAME, BIG_INTEGER_BIG_INTEGER_METHOD_DESCRIPTOR, false
            );
            method.visitFieldInsn(PUTSTATIC, counterFieldOwnerInternalName, counterFieldName, BIG_INTEGER_DESCRIPTOR);

            method.visitFrame(F_SAME, 0, null, 0, null);
            method.visitInsn(RETURN);
            method.visitMaxs(2, 0);
            method.visitEnd();
        }
    }

    /**
     * Implements the {@link HQ9PlusAstNode#H H} method using the given class-writer.
     *
     * @param classWriter class-writer used to implement the method
     * @param options compiler options
     * @return name of the generated method and its creator
     */
    protected static Later<String, Void, Void> implementHMethod(@NonNull final ClassWriter classWriter,
                                                                @NonNull final Options options) {
        val helloWorldText = options.getHelloWorldText();

        return Later.of(options.getHMethodName(), (methodName, sourceCode) -> {
            implementTextOutputMethod(classWriter, methodName, helloWorldText);
            return null;
        });
    }

    /**
     * Implements the {@link HQ9PlusAstNode#Q Q} method using the given class-writer.
     *
     * @param classWriter class-writer used to implement the method
     * @param options compiler options
     * @return name of the generated method and its creator accepting source-code printed
     */
    protected static Later<String, String, Void> implementQMethod(@NonNull final ClassWriter classWriter,
                                                                @NonNull final Options options) {
        return Later.of(options.getQMethodName(), (methodName, sourceCode) -> {
            implementTextOutputMethod(classWriter, methodName, sourceCode);
            return null;
        });
    }

    /**
     * Implements the {@link HQ9PlusAstNode#NINE 9} method using the given class-writer.
     *
     * @param classWriter class-writer used to implement the method
     * @param options compiler options
     * @return name of the generated method and its creator
     */
    protected static Later<String, Void, Void> implementNineMethod(@NonNull final ClassWriter classWriter,
                                                                   @NonNull final Options options) {
        val bottlesOfBeer = options.getBottlesOfBeer();

        return Later.of(options.getNineMethodName(), (methodName, aVoid) -> {
            implementNBottlesOfBeerMethod(classWriter, methodName, bottlesOfBeer);
            return null;
        });
    }

    /**
     * Implements the {@link HQ9PlusAstNode#PLUS +} method using the given class-writer.
     *
     * @param classWriter class-writer used to implement the method
     * @param internalClassName internal name of the class containing the static field whose value gets incremented
     * @param options compiler options
     * @return name of the generated method and its creator
     */
    protected static Later<String, Void, Void> implementPlusMethod(@NonNull final ClassWriter classWriter,
                                                                   @NonNull final String internalClassName,
                                                                   @NonNull final Options options) {
        val fieldName = options.getCounterFieldName();
        val allowNumericOverflow = options.isAllowNumericOverflow();

        return Later.of(options.getPlusMethodName(), (methodName, aVoid) -> {
            implementIncrementerMethod(classWriter, internalClassName, methodName, fieldName, allowNumericOverflow);
            return null;
        });
    }

    /**
     * Generates the class of the given name reading source code using the given reader.
     *
     * @param reader reader providing the source code
     * @param options compiler options
     * @return bytecode of the generated class
     * @throws IOException if an exception occurs while reading source code
     */
    protected byte[] generateClass(@NonNull final Reader reader,
                                   @NonNull final Options options) throws IOException {
        val internalClassName = options.getClassName().replace('.', '/');

        val clazz = new ClassWriter(0);
        clazz.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalClassName, null, OBJECT_INTERNAL_NAME, null);

        @Nullable Later<String, Void, Void> hMethodCreator = null, nineMethodCreator = null, plusMethodCreator = null;
        @Nullable Later<String, String, Void> qMethodCreator = null;

        // reused method local variable
        MethodVisitor method;
        {
            // `void main(String[])` method
            method = clazz.visitMethod(
                    ACC_PUBLIC | ACC_STATIC, MAIN_METHOD_NAME,
                    VOID_STRING_ARRAY_METHOD_DESCRIPTOR, null /* no generics */, null /* no exceptions */
            );
            method.visitCode();

            val sourceCode = new StringBuilder();
            int characterCode;

            val respectCase = options.isRespectCase();
            while ((characterCode = reader.read()) != -1) {
                val character = (char) characterCode;
                val currentNode = HQ9PlusAstNode.match(character, respectCase);

                sourceCode.append(character);
                switch (currentNode) {
                    case H: {
                        if (hMethodCreator == null) hMethodCreator = implementHMethod(clazz, options);

                        method.visitMethodInsn(
                                INVOKESTATIC, internalClassName,
                                hMethodCreator.getComputedValue(), VOID_METHOD_DESCRIPTOR, false
                        );

                        break;
                    }
                    case Q: {
                        // simply mark as using Q as the whole source code should be read before implementing it
                        if (qMethodCreator == null) qMethodCreator = implementQMethod(clazz, options);

                        method.visitMethodInsn(
                                INVOKESTATIC, internalClassName,
                                qMethodCreator.getComputedValue(), VOID_METHOD_DESCRIPTOR, false
                        );

                        break;
                    }
                    case NINE: {
                        if (nineMethodCreator == null) nineMethodCreator = implementNineMethod(clazz, options);

                        method.visitMethodInsn(
                                INVOKESTATIC, internalClassName,
                                nineMethodCreator.getComputedValue(), VOID_METHOD_DESCRIPTOR, false
                        );

                        break;
                    }
                    case PLUS: {
                        if (plusMethodCreator == null) plusMethodCreator
                                = implementPlusMethod(clazz, internalClassName, options);

                        method.visitMethodInsn(
                                INVOKESTATIC, internalClassName,
                                plusMethodCreator.getComputedValue(), VOID_METHOD_DESCRIPTOR, false
                        );

                        break;
                    }
                }
            }

            if (hMethodCreator != null) hMethodCreator.apply(null);
            if (qMethodCreator != null) qMethodCreator.apply(sourceCode.toString());
            if (nineMethodCreator != null) nineMethodCreator.apply(null);
            if (plusMethodCreator != null) plusMethodCreator.apply(null);

            method.visitInsn(RETURN);

            method.visitMaxs(0 /* nothing pushed */, 1 /* CLI-arguments */);
            method.visitEnd();
        }

        // add super-constructor
        method = clazz.visitMethod(
                ACC_PUBLIC, CONSTRUCTOR_METHOD_NAME, VOID_METHOD_DESCRIPTOR,
                null /* no generics in signature as there are no parameters */, null /* no exceptions declared */
        );
        method.visitCode();

        // push `this` onto the stack
        method.visitIntInsn(ALOAD, 0);
        // invoke constructor of the super-class
        method.visitMethodInsn(
                INVOKESPECIAL, OBJECT_INTERNAL_NAME, CONSTRUCTOR_METHOD_NAME, VOID_METHOD_DESCRIPTOR, false
        );
        method.visitInsn(RETURN);

        method.visitMaxs(1, 1);

        clazz.visitEnd();
        return clazz.toByteArray();
    }

    /**
     * Writes the bytes to the given output.t
     *
     * @param bytes bytes to get written to the output
     * @param output output to whom the bytes should be written
     * @throws IOException if an exception occurs while writing to the output
     */
    protected abstract void write(@NotNull byte[] bytes, @NotNull O output) throws IOException;

    /**
     * Transforms the given input into a {@link Reader}.
     *
     * @param input input to convert to reader
     * @return reader created from the input
     */
    protected abstract Reader toReader(@NotNull I input);

    @Override
    public void compile(@NonNull final I input, @NotNull final O output, @NonNull final Options options)
            throws IOException {
        write(generateClass(toReader(input), options), output);
    }
}
