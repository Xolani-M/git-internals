package org.myproject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public class BaseTest {

    protected File testDir;

    @BeforeEach
    public void setUp() {
        // Try to create a temporary directory for testing
        try {
            testDir = Files.createTempDirectory("TestRepo").toFile();  // This will create a unique temp directory
            if (!testDir.exists()) {
                fail("Failed to create test directory");
            }
        } catch (IOException e) {
            fail("IOException occurred while creating test directory: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown(){
        //Clean up the temp dir after each test
        if (testDir.exists()){
            deleteDirectory(testDir);
        }
    }

    protected void deleteDirectory(File testDir) {
        if (testDir.isDirectory()){
            for (File child : Objects.requireNonNull(testDir.listFiles())){
                deleteDirectory(child);
            }
        }
        boolean deleted = testDir.delete();
        if (!deleted) {
            System.err.println("Failed to delete: " + testDir.getAbsolutePath());
        }
    }
}
