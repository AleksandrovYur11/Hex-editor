package service.impl;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class ByteServiceImpl {
    private static final int BUFFER_SIZE = 8192; // Размер буфера чтения

//    public byte[] readFileToByteArray(File file) throws IOException {
//        FileInputStream inputFile = new FileInputStream(file);
//        byte[] fileBytes = new byte[(int) file.length()];
//        inputFile.read(fileBytes);
//        inputFile.close();
//        return fileBytes;
//    }

    public byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream inputFile = new FileInputStream(file);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = inputFile.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        }
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

//    public static void saveByteArrayToFile(byte[] fileBytes, String filePath) throws IOException {
//        Path path = Paths.get(filePath);
//        Files.write(path, fileBytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
//    }


    public static void saveByteArrayToFile(byte[] fileBytes, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        int offset = 0;
        int bufferSize = 8192;

        try (WritableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            while (offset < fileBytes.length) {
                int bytesToWrite = Math.min(bufferSize, fileBytes.length - offset);
                ByteBuffer buffer = ByteBuffer.wrap(fileBytes, offset, bytesToWrite);
                channel.write(buffer);

                offset += bytesToWrite;
            }
        }
    }

}
