package com.beinet.firstpg.fileDemo;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class FileHelper {
    private static String defaultEncoding = "UTF-8";

    // <editor-fold desc="读方法">

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
     * 读取文件内容返回
     *
     * @param fileName 文件路径
     * @param encoding 文件内容编码，为空表示UTF-8
     * @return 文件内容
     * @throws IOException 异常
     */
    public static String read(String fileName, String encoding) throws IOException {
        // 可以参考下： org.apache.commons.io.FileUtils.readFileToString(fileName, encoding);
        if (fileName == null || fileName.length() == 0)
            throw new IllegalArgumentException("fileName can't be empty.");
        try (FileInputStream fileStream = new FileInputStream(fileName)) {
            return read(fileStream, encoding);
        }
    }


    /**
     * 读取流的UTF-8内容返回
     *
     * @param is 流
     * @return 流的内容
     * @throws IOException 异常
     */
    public static String read(InputStream is) throws IOException {
        return read(is, defaultEncoding);
    }

    /**
     * 读取流的内容返回
     *
     * @param is       流
     * @param encoding 内容编码，为空表示UTF-8
     * @return 流的内容
     * @throws IOException 异常
     */
    public static String read(InputStream is, String encoding) throws IOException {
        if (encoding == null || encoding.length() == 0)
            encoding = defaultEncoding;
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(is, encoding)) {
            // readLine不合适，无法确定换行符是 \n还是\r\n
            // BufferedReader bufferedReader = new BufferedReader(reader)
            // while ((line = bufferedReader.readLine()) != null)
            int c;
            while ((c = reader.read()) != -1)
                sb.append((char) c);
        }
        return sb.toString();
    }

    // </editor-fold>

    // <editor-fold desc="写方法">

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

        // 如果是新建文件，则先写入临时文件，再进行移动，避免写入慢，影响其它线程读取操作
        String tmpFile = fileName;
        if (!append) {
            tmpFile += LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        }

        try (FileOutputStream fileStream = new FileOutputStream(tmpFile, append)) {
            write(fileStream, content, encoding);
        }
        if (!append) {
            File oldFile = new File(tmpFile);
            File newFile = new File(fileName);
            newFile.delete();
            oldFile.renameTo(newFile);
        }
    }


    /**
     * 把字符串写入流
     *
     * @param os       流
     * @param content  要写入的内容
     * @param encoding 要写入的编码格式，为空表示UTF-8
     * @throws IOException 异常
     */
    public static void write(OutputStream os, String content, String encoding) throws IOException {
        if (content == null)
            content = ""; // throw new IllegalArgumentException("content can't be null, but can be empty.");
        if (encoding == null || encoding.length() == 0)
            encoding = defaultEncoding;

        try (OutputStreamWriter writer = new OutputStreamWriter(os, encoding)) {
            writer.write(content);
            writer.flush();
        }
    }

    // </editor-fold>

    /**
     * 删除指定文件
     *
     * @param fileName 文件路径
     */
    public static void delete(String fileName) {
        new File(fileName).delete();
    }
}