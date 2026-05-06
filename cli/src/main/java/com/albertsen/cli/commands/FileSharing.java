package com.albertsen.cli.commands;

import java.util.concurrent.Callable;

import com.albertsen.cli.commands.sub.ConnectFSCommand;
import com.albertsen.cli.commands.sub.DevicesFSCommand;
import com.albertsen.cli.commands.sub.NetworkFSCommand;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "fileSharing", 
    version = "1.0", 
    mixinStandardHelpOptions = true, 
    requiredOptionMarker = '*',
    description = "This is a file sharing tool",
    header = "FileSharing CLI",
    optionListHeading = "%nOptions are:%n",
    footer = "%nDeveloped by Group 15",
    commandListHeading = "%nSubCommands: %n",
    subcommands = {
        DevicesFSCommand.class,
        NetworkFSCommand.class,
        ConnectFSCommand.class
    }
)
public class FileSharing implements Callable<Integer> {
    final Integer SUCCESS = 0;
    final Integer FAILURE = 1;

    public static void main(String[] args) {
        int exitStatus = new CommandLine(new FileSharing()).execute("connect", "--connect=hej");
        System.exit(exitStatus);
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("[FileSharing] File Sharing is activated!");
        return SUCCESS;
    }
    
}
