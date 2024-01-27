package service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ByteServiceImpl {
    public byte[] readFileToByteArray(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        fis.read(fileBytes);
        fis.close();
        return fileBytes;
    }
}
