package com.beinet.firstpg.dbTest;

import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.mysql.MySqlService;
import com.beinet.firstpg.mysql.entity.Users;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MySqlTest extends BaseTest {
    @Autowired
    private MySqlService mysql;

    @Test
    public void TestInsert() {
        // 插入数据
        String account = "ybl" + Math.round(Math.random() * 1000000);

        // 在Entity的主键上添加注解 @GeneratedValue(strategy = GenerationType.IDENTITY)
        // 会在保存后返回id的值
        Users user = mysql.AddUser(account, "水边", "123456");
        out(user.toString());
        Assert.assertNotEquals(user.getId(), 0);
    }


    @Test
    public void TestGet() {
        Users user = mysql.GetUserByAccount("ybl");
        out(user.toString());
        Assert.assertEquals(user.getId(), 1);
    }

    @Test
    public void TestBatchInsert(){
        List<Users> arr = new ArrayList<>();
        Users user = new Users();
        user.setAccount("aa");
        user.setUserName("张三");
        user.setPwd("123");
        arr.add(user);

        user = new Users();
        user.setAccount("bbc");
        user.setUserName("李四");
        user.setPwd("123");
        arr.add(user);

        user = new Users();
        user.setAccount("ccc");
        user.setUserName("王五");
        user.setPwd("123");
        arr.add(user);

        int insNum = mysql.BatchInsert(arr);
        Assert.assertEquals(insNum, 3);
    }



    @Test
    public void TestCustomGet() throws SQLException {
        List<String[]> arr1 = mysql.Execute("select * from users");
        List<String[]> arr2 = mysql.Execute2("select * from users");
        Assert.assertEquals(arr1.size(), arr2.size());

        for (String[] row : arr1){
            for (String cell : row)
                System.out.print(cell + ",");
            System.out.println();
        }
        System.out.println("====================");
        for (String[] row : arr2){
            for (String cell : row)
                System.out.print(cell + ",");
            System.out.println();
        }

    }
}
