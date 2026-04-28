package com.albertsen.project6.utilFunctions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logging {

    private static String path = System.getProperty("user.dir");
    private static String fileName = "logs.log";

    public static enum LogLevel {
        info, debug, warn, error
    }

    public static void log(String message, LogLevel level){
        String newMsg = format(message, level);
        System.out.println(newMsg);
        File logFile = new File(path, fileName);
        if(logFile.canWrite()){
            try(FileWriter fw = new FileWriter(logFile)){
                fw.write(newMsg + System.lineSeparator());
            }catch (IOException e){
                //Todo better error handling
                System.out.println(e);
            }
        }
    }


    private static String format(String message, LogLevel level){
        String now = LocalDateTime.now().toString();
        switch (level){
            case info: return "[ INFO ] " + "[" + now + "] " + message;
            case debug: return "[ DEBUG ] " + "[" + now + "] " + message;
            case warn: return "[ WARN ] " + "[" + now + "] " + message;
            default: return "[ ERROR ] " + "[" + now + "] " + message;
        }
    }



}
