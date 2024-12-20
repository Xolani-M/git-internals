package org.myproject;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class RequestHandler {

    private final File baseDir = new File(".");

    public void init() throws IOException {
        // Initialize the Git repository
        RepoInitializer.initGitRepo(baseDir);
        System.out.println("Initialized empty Git repository in " + baseDir.getAbsolutePath() + "/.git/");
    }

    public void catFile(String blobSha) throws IOException {
        // Read the content of the specified blob
        String content = BlobReader.readBlob(baseDir, blobSha);
        System.out.print(content);
    }

    public void hashObject(String[] args) throws IOException, NoSuchAlgorithmException {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Usage: hash-object [-w] <file_path>");
            return;
        }

        boolean write = false;
        String filePath;

        if (args[1].equals("-w")) {
            write = true;
            if (args.length != 3) {
                System.out.println("Usage: hash-object [-w] <file_path>");
                return;
            }
            filePath = args[2];
        } else {
            filePath = args[1];
        }

        try {
            if (write) {
                // Create and store the blob
                String sha1Hash = GitBlobCreator.createBlob(baseDir, filePath);
                System.out.println(sha1Hash);
            } else {
                // Compute SHA-1 hash only
                String sha1Hash = GitBlobCreator.computeSha1FromFile(filePath);
                System.out.println(sha1Hash);
            }
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }
}
