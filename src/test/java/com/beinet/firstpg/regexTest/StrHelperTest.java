package com.beinet.firstpg.regexTest;

import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.regexDemo.StrHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StrHelperTest extends BaseTest {

    /**
     * 正则结果遍历 验证
     */
    @Test
    public void TestRegLoop() {
        String digiStr = "abc123,345dfas789;;;112";
        List<Integer> arr = StrHelper.getNums(digiStr);
        System.out.println(arr);

        Assert.assertEquals(arr.size(), 4);
        Assert.assertEquals((int) (arr.get(2)), 789);
//        Assert.assertEquals(java.util.Optional.ofNullable(arr.get(2)), java.util.Optional.ofNullable(789));
    }


    @Test
    public void TestRegMatch() {
        String str = "abc123,345dfas789;;;112";
        boolean ret = StrHelper.isEmail(str);
        System.out.println(ret);
        Assert.assertFalse(ret);

        str = "youbl@126.com";
        ret = StrHelper.isEmail(str);
        System.out.println(ret);
        Assert.assertTrue(ret);

        str = "256.1.2.3";
        ret = StrHelper.isIp(str);
        System.out.println(ret);
        Assert.assertFalse(ret);

        str = "255.1.2.3";
        ret = StrHelper.isIp(str);
        System.out.println(ret);
        Assert.assertTrue(ret);

        str = "I'm english";
        ret = StrHelper.containChinese(str);
        System.out.println(ret);
        Assert.assertFalse(ret);

        str = "I have 中文 and english";
        ret = StrHelper.containChinese(str);
        System.out.println(ret);
        Assert.assertTrue(ret);
    }


    @Test
    public void TestLength() {
        String str = "abãc我是。";
        int len = StrHelper.byteLength(str);
        Assert.assertEquals(len, 10);
    }
}
