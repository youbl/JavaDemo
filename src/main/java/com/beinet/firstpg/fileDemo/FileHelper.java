package com.beinet.firstpg.fileDemo;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class FileHelper {
    private static String defaultEncoding = "UTF-8";

    /**
     * 读取UTF-8格式的文件内容返回
     *
     * @param fileName 文件路径
     * @return 文件内容
     * @throws IOException 异常
     */
    public static String read(String fileName) throws IOException {
        return read(fileName, defaultEncoding);
    }

    /**
     * 读取的文件内容返回
     *
     * @param fileName 文件路径
     * @param encoding 文件内容编码，为空表示UTF-8
     * @return 文件内容
     * @throws IOException 异常
     */
    public static String read(String fileName, String encoding) throws IOException {
        if (fileName == null || fileName.length() == 0)
            throw new IllegalArgumentException("fileName can't be empty.");
        if (encoding == null || encoding.length() == 0)
            encoding = defaultEncoding;
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fileStream = new FileInputStream(fileName);
             InputStreamReader reader = new InputStreamReader(fileStream, encoding);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    /**
     * 写入文件，如果存在则覆盖
     *
     * @param fileName 文件路径
     * @param content  要写入的文件内容
     * @throws IOException 异常
     */
    public static void write(String fileName, String content) throws IOException {
        write(fileName, content, defaultEncoding, false);
    }

    /**
     * 写入文件，如果存在则追加在末尾
     *
     * @param fileName 文件路径
     * @param content  要写入的文件内容
     * @throws IOException 异常
     */
    public static void append(String fileName, String content) throws IOException {
        write(fileName, content, defaultEncoding, true);
    }

    /**
     * 写入文件
     *
     * @param fileName 文件路径
     * @param content  要写入的文件内容
     * @param encoding 要写入的编码格式，为空表示UTF-8
     * @param append   是追加到文件末尾，还是覆盖文件
     * @throws IOException 异常
     */
    public static void write(String fileName, String content, String encoding, boolean append) throws IOException {
        if (fileName == null || fileName.length() == 0)
            throw new IllegalArgumentException("fileName can't be empty.");
        if (content == null)
            content = ""; // throw new IllegalArgumentException("content can't be null, but can be empty.");
        if (encoding == null || encoding.length() == 0)
            encoding = defaultEncoding;

        // 如果是新建文件，则先写入临时文件，再进行移动，避免写入慢，影响其它线程读取操作
        String tmpFile = fileName;
        if (!append) {
            tmpFile += LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        }

        try (FileOutputStream fileStream = new FileOutputStream(tmpFile, append);
             OutputStreamWriter writer = new OutputStreamWriter(fileStream, encoding)) {
            writer.write(content);
            writer.flush();
        }
        if (!append) {
            File oldFile = new File(tmpFile);
            File newFile = new File(fileName);
            newFile.delete();
            oldFile.renameTo(newFile);
        }
    }

    public static void delete(String fileName) {
        new File(fileName).delete();
    }
}
