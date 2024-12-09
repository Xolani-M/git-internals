package org.myproject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RepoInitializer {

    public static void initGitRepo(File baseDir) throws IOException {
        if (baseDir == null || !baseDir.isDirectory()) {
            throw new IllegalArgumentException("Base directory must be a valid directory.");
        }

        createDirectory(new File(baseDir, ".git"), ".git directory");
        createDirectory(new File(baseDir, ".git/objects"), ".git/objects directory");
        createDirectory(new File(baseDir, ".git/refs"), ".git/refs directory");

        File headFile = new File(baseDir, ".git/HEAD");
        if (!headFile.exists()) {
            Files.writeString(headFile.toPath(), "ref: refs/heads/main\n");
            System.out.println("Created .git/HEAD file pointing to refs/heads/main.");
        }
    }

    private static void createDirectory(File dir, String description) throws IOException {
        if (!dir.exists() && !dir.mkdir()) {
            throw new IOException("Failed to create " + description + ".");
        }
        System.out.println("Created " + description + ".");
    }
}