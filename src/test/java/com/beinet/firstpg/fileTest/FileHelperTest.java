package com.beinet.firstpg.fileTest;

import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.fileDemo.FileHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class FileHelperTest extends BaseTest {

    @Test
    public void testUTF8() throws IOException {
        String filename = "D:/test.xxx";
        // 测试前删除，确保不存在
        FileHelper.delete(filename);

        String content = "我是测试的中文文本\r\n换一个行";
        // 文件不存在时写入
        FileHelper.write(filename, content);
        String readStr = FileHelper.read(filename);
        out("读取内容： " + readStr);
        Assert.assertEquals(content, readStr);

        // 文件存在时覆盖写入
        content = "我是测试的覆盖中文文本";
        FileHelper.write(filename, content);
        readStr = FileHelper.read(filename);
        out("读取内容： " + readStr);
        Assert.assertEquals(content, readStr);

        // 删除文件
        FileHelper.delete(filename);
        Assert.assertEquals(false, new File(filename).exists());

        // 文件不存在时追加写入
        content = "我是测试的append中文文本\r\n换一个行";
        FileHelper.append(filename, content);
        readStr = FileHelper.read(filename);
        out("读取内容： " + readStr);
        Assert.assertEquals(content, readStr);

        // 文件存在时追加写入
        String content2 = "我是测试的append中文文本";
        FileHelper.append(filename, content2);
        readStr = FileHelper.read(filename);
        out("读取内容： " + readStr);
        Assert.assertEquals(content + content2, readStr);
    }


    @Test
    public void testGBK() throws IOException {
        String filename = "D:/test.xxx";
        String encoding = "GBK";
        // 测试前删除，确保不存在
        FileHelper.delete(filename);

        String content = "我是测试的中文文本\r\n换一个行";
        // 文件不存在时写入
        FileHelper.write(filename, content, encoding, false);
        String readStr = FileHelper.read(filename, encoding);
        out("读取内容： " + readStr);
        Assert.assertEquals(content, readStr);

        // 文件存在时覆盖写入
        content = "我是测试的覆盖中文文本";
        FileHelper.write(filename, content, encoding, false);
        readStr = FileHelper.read(filename, encoding);
        out("读取内容： " + readStr);
        Assert.assertEquals(content, readStr);

        // 删除文件
        FileHelper.delete(filename);
        Assert.assertEquals(false, new File(filename).exists());

        // 文件不存在时追加写入
        content = "我是测试的append中文文本\r\n换一个行";
        FileHelper.write(filename, content, encoding, true);
        readStr = FileHelper.read(filename, encoding);
        out("读取内容： " + readStr);
        Assert.assertEquals(content, readStr);

        // 文件存在时追加写入
        String content2 = "我是测试的append中文文本";
        FileHelper.write(filename, content2, encoding, true);
        readStr = FileHelper.read(filename, encoding);
        out("读取内容： " + readStr);
        Assert.assertEquals(content + content2, readStr);
    }

    // 测试不使用数组读取文件，跟使用数组读取文件的性能差异
    @Test
    public void testPerformance() throws IOException {
        String fileName = "D:/a.pdf";
        int loop = 100;

        System.out.println("内容1：");
        try (FileInputStream fileStream = new FileInputStream(fileName)) {
            String str = read1(fileStream);
            // System.out.println(str);
        }

        System.out.println("内容2：");
        try (FileInputStream fileStream = new FileInputStream(fileName)) {
            String str = read2(fileStream);
            //  System.out.println(str);
        }

        long time1 = 0;
        long time2 = 0;
        long time3 = 0;

        System.out.println("======\r\n时间对比：");
        for (int i = 0; i < loop; i++) {
            try (FileInputStream fileStream = new FileInputStream(fileName)) {
                long begin = System.currentTimeMillis();
                read1(fileStream);
                time1 += System.currentTimeMillis() - begin;
            }

            try (FileInputStream fileStream = new FileInputStream(fileName)) {
                long begin = System.currentTimeMillis();
                read2(fileStream);
                time2 += System.currentTimeMillis() - begin;
            }
            try (FileInputStream fileStream = new FileInputStream(fileName)) {
                long begin = System.currentTimeMillis();
                read3(fileStream);
                time3 += System.currentTimeMillis() - begin;
            }
        }
        // 3M文件测试结果：不用数组:14805  使用数组:6889 6919
        System.out.println(String.format("不用数组:%s  使用数组:%s %s", time1, time2, time3));
    }


    static String read1(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(is)) {
            int c;
            while ((c = reader.read()) != -1)
                sb.append((char) c);
        }
        return sb.toString();
    }


    static String read2(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] bys = new char[1024];
        try (InputStreamReader reader = new InputStreamReader(is)) {
            int c;
            while ((c = reader.read(bys)) != -1)
                sb.append(new String(bys, 0, c));
        }
        return sb.toString();
    }

    static String read3(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] bys = new char[10240];
        try (InputStreamReader reader = new InputStreamReader(is)) {
            int c;
            while ((c = reader.read(bys)) != -1)
                sb.append(new String(bys, 0, c));
        }
        return sb.toString();
    }
}
