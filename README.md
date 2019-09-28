# HQ9+ for JVM

###### Tools for HQ9+ programming language targeting JVM-runtime

## HQ9+ JVM-compiler

###### Compiler for HQ9+ programming language targeting JVM-runtime

This project provides a configurable compiler for HQ9+ with source available [here](/hq9plus-jvm-compiler).

### Using Compiler API

In order to use the Compiler API you first need to obtain your instance of `HQ9PlusCompiler<I, O>` (`I` and `O` stand for input-source and compilation target respectively), for example via factory `AsmHQ9PlusCompilers`. After this you need to call its `compile(I, O, Options)` providing it with a needed configuration. Note that `Options` passed should be non-null as these contain data required by the compiler such as class-name.

## HQ9+ JVM-compiler CLI

###### Command-line interface for JVM-targeting compiler of HQ9+ programming language

This project provides a CLI for the compiler of HQ9+ with source available [here](/hq9plus-jvm-compiler-cli).

### Compiling an HQ9+ class

Compiler CLI is provided as a Java application. In order to compile your source file, simply use `java -jar hq9plus-jvm -compiler.jar Example.hq9p` where `hq9plus-jvm-compiler.jar` is the path to the compiler-JAR and `Example.hq9p` is the path to the compiled class.

> Note: you don't need JDK in order to compile the file, but JRE is still needed in order to use the CLI (and the very compiler)

#### Compiler configuration

While this form is enough for a general compilation, there are multiple options to configure the compiler:

| Name                     | Alias | Type of argument                | Description                                                  |
| ------------------------ | ----- | ------------------------------- | ------------------------------------------------------------ |
| `source-file`            | `in`  | File path                       | Path to source-code compiled                                 |
| `target-file`            | `out` | File path                       | Path to the file in which the compiled class should be saved |
| `disrespect-case`        | `drc` | *None*                          | Flag disabling case-respect for the source code              |
| `allow-pre-computation`  | `apc` | *None*                          | Flag allowing possible pre-computations                      |
| `allow-numeric-overflow` | `ano` | *None*                          | Flag allowing numeric overflow of the counter"               |
| `hello-world-text`       | `hwt` | String                          | Text to get outputted for `H`-instruction                    |
| `bottles-of-beer`        | `bbc` | Positive number (up to `2³¹-1`) | Initial count of `9`-instruction's bottles of beer           |
| `h-method-name`          | `hmn` | String                          | Name used for generated method performing `H`-instruction (if any) |
| `q-method-name`          | `qmn` | String                          | Name used for generated method performing `Q`-instruction (if any) |
| `nine-method-name`       | `nmn` | String                          | Name used for generated method performing `9`-instruction (if any) |
| `plus-method-name`       | `pmn` | String                          | Name used for generated method performing `+`-instruction (if any) |
| `counter-field-name`     | `cfn` | String                          | Name used for generated counter field (if any)               |
