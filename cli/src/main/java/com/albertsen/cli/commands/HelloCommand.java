package com.albertsen.cli.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "hello", 
    version = "1.0", 
    mixinStandardHelpOptions = true, 
    requiredOptionMarker = '*',
    description = "This is a simple Hello Command",
    header = "Sample  Command",
    optionListHeading = "%nOptions are:%n"
)
public class HelloCommand implements Runnable {

    @Option(names={"-u", "--user"}, 
        description = "Provide a username",
        required = false,
        paramLabel = "<user name>"
    )
    String user;

    @Override
    public void run(){
        if(user == null || user.length() == 0){
            System.out.println("[hello] Hello World!");
        }else{
            System.out.println("[hello] Hello " + user);
        }
    }
}
