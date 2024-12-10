package org.myproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

public class BlobReader {

    public static void readBlob(File baseDir, String sha) throws IOException {
        if (sha == null || sha.length() != 40){
            throw new IllegalArgumentException("Invalid SHA-1 hash.");
        }

        //Locate the blob file
        String dirName = sha.substring(0,2);
        String fileName = sha.substring(2);
        File blobFile = new File(baseDir,".git/objects/" + dirName + "/" + fileName);

        //Read and decompress the blob file
        try (FileInputStream fileInputStream = new FileInputStream(blobFile);
             InflaterInputStream inflaterStream = new InflaterInputStream(fileInputStream)) {

            byte[] buffer = inflaterStream.readAllBytes();
            String decompressedData = new String(buffer);

            //Separate header and content
            int nullIndex = decompressedData.indexOf("\0");
            if (nullIndex == -1){
                throw new IOException("Invalid blob format.");
            }
            String reader = decompressedData.substring(0,nullIndex);
            String content = decompressedData.substring(nullIndex+1);

            System.out.println("Blob Content: \n" + content);
        }

    }
}
