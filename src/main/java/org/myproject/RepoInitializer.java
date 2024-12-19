package org.myproject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RepoInitializer {

    /**
     * Initializes a new Git repository in the specified base directory.
     * If a .git directory already exists, it will not proceed.
     *
     * @param baseDir The base directory where the repository should be initialized.
     * @throws IOException If an error occurs during initialization.
     */
    public static void initGitRepo(File baseDir) throws IOException {
        if (baseDir == null || !baseDir.isDirectory()) {
            throw new IllegalArgumentException("Base directory must be a valid directory.");
        }

        File gitDir = new File(baseDir, ".git");

        // Check if .git directory already exists
        if (gitDir.exists()) {
            System.err.println(".git directory already exists. Initialization skipped.");
            return;
        }

        createDirectory(gitDir, ".git directory");
        createDirectory(new File(gitDir, "objects"), ".git/objects directory");
        createDirectory(new File(gitDir, "refs"), ".git/refs directory");

        File headFile = new File(gitDir, "HEAD");
        if (!headFile.exists()) {
            Files.writeString(headFile.toPath(), "ref: refs/heads/main\n");
            System.out.println("Created .git/HEAD file pointing to refs/heads/main.");
        }
    }

    /**
     * Creates a directory and logs the action. Throws an exception if creation fails.
     *
     * @param dir         The directory to create.
     * @param description A description of the directory (used for logging).
     * @throws IOException If the directory cannot be created.
     */
    private static void createDirectory(File dir, String description) throws IOException {
        if (!dir.exists() && !dir.mkdir()) {
            throw new IOException("Failed to create " + description + ".");
        }
        System.out.println("Created " + description + ".");
    }
}
