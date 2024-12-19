package org.myproject;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class BlobReaderTest extends BaseTest {

    @Test
    void testValidBlobFile() throws Exception {
        // Create a valid blob file
        String content = "Hello World";
        String blobSha = "557db03de997c86a4a028e1ebd3a1ceb225be238";
        File blobFile = new File(testDir, ".git/objects/55/7db03de997c86a4a028e1ebd3a1ceb225be238");
        blobFile.getParentFile().mkdirs();

        try (FileOutputStream fos = new FileOutputStream(blobFile);
             DeflaterOutputStream dos = new DeflaterOutputStream(fos)) {
            dos.write(("blob " + content.length() + "\0" + content).getBytes());
        }

        // Test reading the blob
        String result = BlobReader.readBlob(testDir, blobSha);
        assertEquals(content, result, "The blob content should match.");
    }

    @Test
    void testInvalidBlobSha() {
        // Invalid SHA-1 hash
        String invalidSha = "invalidSHA";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            BlobReader.readBlob(testDir, invalidSha);
        });

        assertTrue(exception.getMessage().contains("Invalid SHA-1 hash"));
    }

    @Test
    void testBlobNotFound() {
        // Valid-looking SHA-1 hash, but no corresponding file
        String nonExistentSha = "abcdefabcdefabcdefabcdefabcdefabcdefabcd";
        Exception exception = assertThrows(IOException.class, () -> {
            BlobReader.readBlob(testDir, nonExistentSha);
        });

        assertTrue(exception.getMessage().contains("Blob object not found"));
    }

    @Test
    void testBlobWithInvalidFormat() throws Exception {
        // Create a malformed blob file (no null separator)
        String malformedSha = "badbadbadbadbadbadbadbadbadbadbadbadbadb";
        File malformedBlobFile = new File(testDir, ".git/objects/ba/dbadbadbadbadbadbadbadbadbadbadbadbadb");
        malformedBlobFile.getParentFile().mkdirs();

        try (FileOutputStream fos = new FileOutputStream(malformedBlobFile);
             DeflaterOutputStream dos = new DeflaterOutputStream(fos)) {
            dos.write("blob 11HelloWorld".getBytes()); // No null separator
        }

        Exception exception = assertThrows(IOException.class, () -> {
            BlobReader.readBlob(testDir, malformedSha);
        });

        assertTrue(exception.getMessage().contains("Invalid blob format"));
    }
}
