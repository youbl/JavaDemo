package com.beinet.firstpg.arrayTest;

import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.arrayDemo.ArraySort;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArrayTest  extends BaseTest {
    @Test
    public void StrOrder(){
        String origin = "sdlF8HDhkd1fhDFhD3KFHhfd";
        String ret1 = ArraySort.order(origin);
        String ret2 = ArraySort.orderIgnoreCase(origin);
        String ret3 = ArraySort.orderLowFirst(origin);

        System.out.println(origin);
        System.out.println(ret1);
        System.out.println(ret2);
        System.out.println(ret3);
    }

    @Test
    public void ObjOrder(){
        List<ArraySort.Employee> arr = new ArrayList<>();
        ArraySort.Employee emp = new ArraySort.Employee();
        emp.setAge(100);
        emp.setName("xyz");
        arr.add(emp);
        emp = new ArraySort.Employee();
        emp.setAge(40);
        emp.setName("ttt");
        arr.add(emp);
        emp = new ArraySort.Employee();
        emp.setAge(70);
        emp.setName("bbb");
        arr.add(emp);
        emp = new ArraySort.Employee();
        emp.setAge(40);
        emp.setName("bbb");
        arr.add(emp);

        ArraySort.orderEmployee(arr);
        System.out.println(arr);
    }
}
