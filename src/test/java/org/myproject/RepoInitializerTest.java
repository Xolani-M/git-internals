package org.myproject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class RepoInitializerTest {

    private File testDir;

    @BeforeEach
    public void setUp() {
        // Create a temporary directory for testing
        testDir = new File("testRepo");
        if (!testDir.exists() && !testDir.mkdirs()) {
            throw new RuntimeException("Failed to create test directory.");
        }
    }

    @AfterEach
    public void tearDown() {
        // Clean up the temporary directory after each test
        if (testDir.exists()) {
            deleteDirectory(testDir);
        }
    }

    @Test
    public void testCreatesGitDirectory() throws IOException {
        RepoInitializer.initGitRepo(testDir);

        File gitDir = new File(testDir, ".git");
        assertTrue(gitDir.exists() && gitDir.isDirectory(), ".git directory should be created");
    }

    @Test
    public void testCreatesObjectsDirectory() throws IOException {
        RepoInitializer.initGitRepo(testDir);

        File objectsDir = new File(testDir, ".git/objects");
        assertTrue(objectsDir.exists() && objectsDir.isDirectory(), "objects directory should be created");
    }

    @Test
    public void testCreatesRefsDirectory() throws IOException {
        RepoInitializer.initGitRepo(testDir);

        File refsDir = new File(testDir, ".git/refs");
        assertTrue(refsDir.exists() && refsDir.isDirectory(), "refs directory should be created");
    }

    @Test
    public void testCreatesHeadFile() throws IOException {
        RepoInitializer.initGitRepo(testDir);

        File headFile = new File(testDir, ".git/HEAD");
        assertTrue(headFile.exists(), "HEAD file should be created");

        String expectedContent = "ref: refs/heads/main\n";
        String actualContent = Files.readString(headFile.toPath());
        assertEquals(expectedContent, actualContent, "HEAD file content should match");
    }

    private void deleteDirectory(File file) {
        if (file.isDirectory()) {
            for (File child : Objects.requireNonNull(file.listFiles())) {
                deleteDirectory(child);
            }
        }
        boolean deleted = file.delete();
        if (!deleted) {
            System.err.println("Failed to delete: " + file.getAbsolutePath());
        }
    }
}