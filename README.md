# git-vcs-clone

git-vcs-clone is a lightweight Git-like version control system implemented in Java. It allows you to interact with repositories, read Git objects, and perform basic operations similar to Git commands.

## Features
- Initialize a new Git repository.
- Read and decompress blob objects.
- Simulate Git commands like `cat-file`.

## Prerequisites
- Java 17 or higher.
- Maven installed.

## Setting Up the Project

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd git-vcs-clone
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

## Usage

### Creating a Test Blob File
1. Create a file (e.g., `example.txt`) with content:
   ```bash
   echo "Hello World" > example.txt
   ```

2. Add the file to Git's object database:
   ```bash
   git hash-object -w example.txt
   ```
   This will output the SHA-1 hash of the blob, e.g., `557db03de997c86a4a028e1ebd3a1ceb225be238`.

### Running Commands

#### Initialize a Repository
To initialize a Git repository:
```bash
mvn exec:java -Dexec.args="init"
```
This will create a `.git` directory with subdirectories like `objects` and `refs`.

#### Read a Blob Object
To read the content of a blob:
```bash
mvn exec:java -Dexec.args="cat-file -p <blob_sha>"
```
Replace `<blob_sha>` with the SHA-1 hash of the blob (e.g., `557db03de997c86a4a028e1ebd3a1ceb225be238`).

Example:
```bash
mvn exec:java -Dexec.args="cat-file -p 557db03de997c86a4a028e1ebd3a1ceb225be238"
```
This will output:
```text
Hello World
```

## Tests

To run tests:
```bash
mvn test
```

### BlobReader Test
The `BlobReaderTest` verifies the ability to read blob objects from the `.git/objects` directory and extract their content.

### RepoInitializer Test
The `RepoInitializerTest` ensures that:
- The `.git` directory is created.
- Subdirectories like `objects` and `refs` exist.
- The `HEAD` file is initialized correctly.

## Code Structure

- `Main`: Entry point for executing commands.
- `RepoInitializer`: Handles repository initialization.
- `BlobReader`: Reads and decompresses blob objects.

## Notes
- Ensure the `.git` directory exists before running commands that interact with it.
- The `cat-file` command currently supports reading blob objects.

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License
This project is licensed under the MIT License.
