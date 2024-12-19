package org.myproject;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        System.err.println("Program logs: ");

        if (args.length < 1) {
            System.out.println("Usage: <command> <args>");
            return;
        }

        final String command = args[0];

        switch (command) {
            case "init" -> {
                System.err.println("Executing init...");
                File baseDir = new File(".");
                try {
                    RepoInitializer.initGitRepo(baseDir);
                } catch (Exception e) {
                    System.err.println("Error initializing git repo: " + e.getMessage());
                }
            }
            case "cat-file" -> {
                System.err.println("Executing cat-file...");
                if (args.length != 3) {
                    System.out.println("Usage: cat-file -p <blob_sha>");
                    return;
                }
                String blobSha = args[2];
                try {
                    File baseDir = new File(".");
                    String content = BlobReader.readBlob(baseDir, blobSha);
                    System.out.print(content);
                } catch (IOException e) {
                    System.err.println("Error reading blob: " + e.getMessage());
                }
            }
            default ->
                System.out.println("Unknown command: " + command);

        }
    }
}
