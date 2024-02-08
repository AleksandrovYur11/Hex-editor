package service.impl;

import lombok.Getter;
import lombok.Setter;

import java.io.*;

@Getter
@Setter

public class ByteServiceImpl {
    private File file;
    private static final int BUFFER_SIZE = 1024; // Размер буфера чтения
    private long bytePosition = 0;


    public byte[] readFileToByteArray(File filePath) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            byte[] buffer = new byte[BUFFER_SIZE];
            raf.seek(bytePosition);
            int bytesRead = raf.read(buffer);
            if (bytesRead != -1) {
                bytePosition += bytesRead;
                return buffer;
            } else {
                return null;
            }
        }
    }

    public void saveByteArrayToFile(byte[] fileBytes, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileBytes);
        }
    }
}
