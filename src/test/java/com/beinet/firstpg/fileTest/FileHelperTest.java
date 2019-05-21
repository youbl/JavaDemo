package com.beinet.firstpg.fileTest;

import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.fileDemo.FileHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FileHelperTest extends BaseTest {

    @Test
    public void testUTF8() throws IOException {
        String filename = "D:/test.xxx";
        // 测试前删除，确保不存在
        FileHelper.delete(filename);

        String content = "我是测试的中文文本";
        // 文件不存在时写入
        FileHelper.write(filename, content, "GBK", false);
        String readStr = FileHelper.read(filename);
        Assert.assertEquals(content, readStr);

        // 文件存在时覆盖写入
        content = "我是测试的覆盖中文文本";
        FileHelper.write(filename, content);
        readStr = FileHelper.read(filename);
        Assert.assertEquals(content, readStr);

        // 删除文件
        FileHelper.delete(filename);
        Assert.assertEquals(false, new File(filename).exists());

        // 文件不存在时追加写入
        content = "我是测试的append中文文本";
        FileHelper.append(filename, content);
        readStr = FileHelper.read(filename);
        Assert.assertEquals(content, readStr);

        // 文件存在时追加写入
        String content2 = "我是测试的append中文文本";
        FileHelper.append(filename, content2);
        readStr = FileHelper.read(filename);
        Assert.assertEquals(content + content2, readStr);
    }


    @Test
    public void testGBK() throws IOException {
        String filename = "D:/test.xxx";
        String encoding = "GBK";
        // 测试前删除，确保不存在
        FileHelper.delete(filename);

        String content = "我是测试的中文文本";
        // 文件不存在时写入
        FileHelper.write(filename, content, encoding, false);
        String readStr = FileHelper.read(filename, encoding);
        Assert.assertEquals(content, readStr);

        // 文件存在时覆盖写入
        content = "我是测试的覆盖中文文本";
        FileHelper.write(filename, content, encoding, false);
        readStr = FileHelper.read(filename, encoding);
        Assert.assertEquals(content, readStr);

        // 删除文件
        FileHelper.delete(filename);
        Assert.assertEquals(false, new File(filename).exists());

        // 文件不存在时追加写入
        content = "我是测试的append中文文本";
        FileHelper.write(filename, content, encoding, true);
        readStr = FileHelper.read(filename, encoding);
        Assert.assertEquals(content, readStr);

        // 文件存在时追加写入
        String content2 = "我是测试的append中文文本";
        FileHelper.write(filename, content2, encoding, true);
        readStr = FileHelper.read(filename, encoding);
        Assert.assertEquals(content + content2, readStr);
    }
}
