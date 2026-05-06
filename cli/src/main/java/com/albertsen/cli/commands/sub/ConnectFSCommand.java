package com.albertsen.cli.commands.sub;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "connect", 
    //aliases = {""},
    version = "1.0", 
    mixinStandardHelpOptions = true, 
    requiredOptionMarker = '*',
    description = "This is a Sub Command and is used to connect to a network",
    header = "Connect FS Sub Command",
    optionListHeading = "%nOptions are:%n",
    footer = "%nDeveloped by Group 15"
)
public class ConnectFSCommand implements Runnable {
    
    @Option(names={"-co", "--connect"}, 
        description = "Provide a device name",
        required = true,
        paramLabel = "<device name>"
    )

    String device;

    @Override
    public void run(){
        System.out.println("[connect] Device " + device + " connected to network");

    }
}
