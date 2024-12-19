package org.myproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

public class BlobReader {

    /**
     * Reads and returns the content of a Git blob object.
     *
     * @param baseDir The base directory of the Git repository.
     * @param blobSha The SHA-1 hash of the blob object.
     * @return The content of the blob as a string.
     * @throws IOException If an error occurs while reading the blob.
     */
    public static String readBlob(File baseDir, String blobSha) throws IOException {
        // Validate the SHA-1 hash format
        if (blobSha == null || blobSha.length() != 40 || !blobSha.matches("[a-fA-F0-9]+")) {
            throw new IllegalArgumentException("Invalid SHA-1 hash: " + blobSha);
        }

        // Construct the blob file path
        String objectPath = ".git/objects/" + blobSha.substring(0, 2) + "/" + blobSha.substring(2);
        File blobFile = new File(baseDir, objectPath);

        // Check if the blob file exists
        if (!blobFile.exists()) {
            throw new FileNotFoundException("Blob object not found: " + blobSha);
        }

        // Read and decompress the blob file
        try (FileInputStream fileInput = new FileInputStream(blobFile);
             InflaterInputStream inflate = new InflaterInputStream(fileInput)) {

            // Read the decompressed data
            byte[] decompressedData = inflate.readAllBytes();

            // Locate the null separator to split header and content
            int nullIndex = 0;
            while (nullIndex < decompressedData.length && decompressedData[nullIndex] != 0) {
                nullIndex++;
            }
            if (nullIndex >= decompressedData.length) {
                throw new IOException("Invalid blob format: Missing null separator");
            }

            // Extract the content starting after the null byte
            int offset = nullIndex + 1;
            int length = decompressedData.length - offset;
            return new String(decompressedData, offset, length);
        }
    }
}
