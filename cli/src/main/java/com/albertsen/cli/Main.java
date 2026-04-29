package com.albertsen.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "mycli", mixinStandardHelpOptions = true, version = "1.0",
        description = "Says hello")
public class Main implements Runnable {

    @Option(names = {"-n", "--name"}, description = "Your name")
    String name = "World";

    public void run() {
        System.out.println("Hello, " + name + "!");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
