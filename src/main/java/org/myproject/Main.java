package org.myproject;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.err.println("Program logs: ");

        if (args.length < 1) {
            System.out.println("Usage: <command> <args>");
            return;
        }

        final String command = args[0];

        switch (command){
            case "init" :
                File baseDir = new File(".");
                try {
                    RepoInitializer.initGitRepo(baseDir);
                } catch (Exception e){
                    System.err.println("Error initializing git repo: " + e.getMessage());
                }
            default :
                System.out.println("Unknown command");
        }
    }
}