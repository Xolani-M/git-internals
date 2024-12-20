package org.myproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DeflaterOutputStream;

public class BlobCreator {

    /**
     * Creates a Git blob object and writes it to the .git/objects directory.
     *
     * @param baseDir The base directory of the Git repository.
     * @param filePath The path to the file to be turned into a blob.
     * @return The SHA-1 hash of the created blob object.
     * @throws IOException If an error occurs while reading or writing files.
     */
    public static String createBlob(File baseDir, String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        // Read the file content
        byte[] content = Files.readAllBytes(file.toPath());

        // Create the blob header
        String header = "blob " + content.length + "\0";
        byte[] headerBytes = header.getBytes();

        // Concatenate header and content
        byte[] blobData = new byte[headerBytes.length + content.length];
        System.arraycopy(headerBytes, 0, blobData, 0, headerBytes.length);
        System.arraycopy(content, 0, blobData, headerBytes.length, content.length);

        // Compute SHA-1 hash
        String sha1Hash = computeSha1(blobData);

        // Determine blob storage path
        String objectDir = ".git/objects/" + sha1Hash.substring(0, 2);
        File objectDirectory = new File(baseDir, objectDir);
        if (!objectDirectory.exists() && !objectDirectory.mkdirs()) {
            throw new IOException("Failed to create directories for blob storage: " + objectDir);
        }

        File blobFile = new File(objectDirectory, sha1Hash.substring(2));
        if (blobFile.exists()) {
            return sha1Hash; // Blob already exists
        }

        // Write compressed blob data to the file
        try (FileOutputStream fileOutput = new FileOutputStream(blobFile);
             DeflaterOutputStream deflaterOutput = new DeflaterOutputStream(fileOutput)) {
            deflaterOutput.write(blobData);
        }

        return sha1Hash;
    }

    private static String computeSha1(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = digest.digest(data);
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not available", e);
        }
    }
}
