package com.beinet.firstpg.mongoTest;

import com.beinet.firstpg.mongoDemo.Employee;
import com.beinet.firstpg.mongoDemo.EmployeeRepository;
import com.mongodb.DuplicateKeyException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoTest {
    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void AddTest() {
        int id = 456;

        // 先执行删除，id不存在也不会抛异常
        employeeRepository.deleteById(id);

        // 测试，正常插入
        Employee emp = new Employee();
        emp.setId(id);
        emp.setAge(123);
        emp.setName("ybl");
        employeeRepository.insert(emp);

        // 测试主键冲突
        try {
            employeeRepository.insert(emp);
            Assert.assertTrue(false);
        } catch (org.springframework.dao.DuplicateKeyException exp) {
            Assert.assertTrue(true);
        }

        // 测试更新
        String name = "beinet";
        emp.setName(name);
        emp.setAge(34);
        employeeRepository.save(emp);

        // 测试根据name查找
        Employee dbEmp = employeeRepository.findByName("notExists");
        Assert.assertNull(dbEmp);
        dbEmp = employeeRepository.findByName(name);
        Assert.assertNotNull(dbEmp);
        Assert.assertEquals(dbEmp.getId(), emp.getId());
    }

    @Test
    public void QuertTest() {
        int adultNum = AddTestData();

        List<Employee> arr = employeeRepository.findAllAdultsByName("ybl");
        System.out.println("全部读取返回条数：" + arr.size());
        Assert.assertEquals(adultNum, arr.size());
        if(adultNum > 0)
            Assert.assertNull(arr.get(0).getName());

        Pageable pageable = PageRequest.of(0,5);
        arr = employeeRepository.findAllAdultsByName("ybl", pageable);
        System.out.println("分页读取返回条数：" + arr.size());
        if(adultNum >= 5 && adultNum >= arr.size())
            Assert.assertEquals(arr.size(), 5);
        else
            Assert.assertTrue(arr.size() < 5);
    }

    private int AddTestData() {
        int begin = 99999999;
        int delNum = employeeRepository.deleteByIdAfter(begin - 1);
        System.out.println("删除条数：" + delNum);

        Employee emp = new Employee();
        int adultNum = 0;
        // 插入20条记录
        for (int i = begin; i < begin + 20; i++) {
            emp.setId(i);
            int rnd = (int) (1 + Math.random() * (10 - 1 + 1)); // 1~10的随机数
            int age = rnd + 10;
            if (age >= 18) adultNum++;
            emp.setAge(age);
            emp.setName("ybl-" + rnd);
            employeeRepository.insert(emp);
        }
        return adultNum;
    }
}