package com.albertsen.cli.commands.sub;


import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "network", 
    //aliases = {""},
    version = "1.0", 
    mixinStandardHelpOptions = true, 
    requiredOptionMarker = '*',
    description = "This is a Sub Command and is used to create a network",
    header = "Network FS Sub Command",
    optionListHeading = "%nOptions are:%n",
    footer = "%nDeveloped by Group 15"
)
public class NetworkFSCommand implements Runnable{

    @Option(names={"-cr", "--create"}, 
        description = "Provide a network name",
        required = true,
        paramLabel = "<network name>"
    )

    String networkName;

    @Override
    public void run(){
        System.out.println("[network] Network " + networkName + " created");

    }
}
