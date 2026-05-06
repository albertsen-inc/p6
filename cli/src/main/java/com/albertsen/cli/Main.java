package com.albertsen.cli;

import com.albertsen.cli.commands.HelloCommand;
import picocli.CommandLine;

public class Main{
    public static void main(String[] args) {
       new CommandLine(new HelloCommand()).execute("-h");
    }
    
}



