package com.beinet.firstpg.httpTest;

import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.httpDemo.HttpHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpHelperTest extends BaseTest {
    private String url = "https://www.baidu.com/";
    private String param = "a=1&b我是中国人bb&cc=联通df&=4d";

    /**
     * 测试GET方法，带参数与不带参数
     */
    @Test
    public void testGet() {
        String str = HttpHelper.GetPage(url);
        out("返回结果：\r\n" + str);

        str = HttpHelper.GetPage(url, param);
        out("返回结果：\r\n" + str);
    }

    /**
     * 测试POST方法
     */
    @Test
    public void testPost(){
        String str = HttpHelper.PostPage(url, param);
        out(str);
        Assert.assertTrue("返回内容不够长", str.length() > 1000);
    }

    /**
     * 测试自定义配置
     */
    @Test
    public void testPostCustom(){
        HttpHelper.Config config = new HttpHelper.Config();
        config.setFollowRedirect(true);
        config.setShowHeader(true);
//        config.setProxy("127.0.0.1:8888"); // 加代理，可以用Fiddler看请求情况是否符合要求
        config.setMethod("POST");
        // POST 不带参数
        String str = HttpHelper.GetPage(url, config);
        out(str);

        // POST 带参数
        config.setParam(param);
        str = HttpHelper.GetPage(url, config);
        out(str);

        Assert.assertTrue("返回内容不够长", str.length() > 1000);
    }


    @Test
    public void testWithCookie(){
        String url2 = "http://10.2.3.43/cookie.aspx";
        String str = HttpHelper.GetPage(url2);
        url2 = "http://10.2.3.43/a.aspx";
        str = HttpHelper.GetPage(url2);
        out("返回结果：\r\n" + str);
    }
}
