package service.impl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class ByteServiceImpl {
    public byte[] readFileToByteArray(File file) throws IOException {
        FileInputStream inputFile = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        inputFile.read(fileBytes);
        inputFile.close();
        return fileBytes;
    }

//    public void saveByteArrayToFile(byte[] fileBytes, String filePath) throws IOException {
//        try (FileOutputStream fos = new FileOutputStream(filePath)) {
//            for (byte b : fileBytes) {
//                if (b == 0) {
//                    fos.write(' '); // Заменяем нулевые байты на пробелы
//                } else {
//                    fos.write(b);
//                }
//            }
//        }
//    }

    public static void saveByteArrayToFile(byte[] fileBytes, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, fileBytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

}
