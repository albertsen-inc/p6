package com.albertsen.cli.commands.sub;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(name = "devices", 
    //aliases = {""},
    version = "1.0", 
    mixinStandardHelpOptions = true, 
    requiredOptionMarker = '*',
    description = "This is a Sub Command and displays currently available devices",
    header = "Devices FS Sub Command",
    optionListHeading = "%nOptions are:%n",
    footer = "%nDeveloped by Group 15"
)
public class DevicesFSCommand implements Callable<Integer>{

    

    @Override
    public Integer call() throws Exception {
        System.out.println("[Devices] Devices available:");
        return 0;
    }
}
