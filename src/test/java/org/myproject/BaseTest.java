package org.myproject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public class BaseTest {

    protected File testDir;

    @BeforeEach
    public void setUp(){
        //Create temp dir for testing
        testDir = new File("TestRepo");

        if (!testDir.exists() && !testDir.isDirectory()){
            fail("Failed to create test directory");
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
