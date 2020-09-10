package com.beinet.firstpg.mongoDemo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EmployeeRepository {// extends MongoRepository<Employee, Integer> {
    /**
     * 根据名称精确匹配
     * @param name 姓名
     * @return 记录
     */
    Employee findByName(String name);

    /**
     * 删除id大于指定参数的记录
     * @param id 最小id
     * @return 删除行数
     */
    int deleteByIdAfter(int id);

    /**
     * 返回所有大于18岁的成员
     * @param name 姓名
     * @return 列表
     */
    @Query(value = "{'name':{'$regex':?0}, 'age':{'$gte':18}}", fields = "{'id':1,'age':1}")
    List<Employee> findAllAdultsByName(String name);

    /**
     * 返回所有大于18岁的成员
     * @param name 姓名
     * @param pageable 分页
     * @return 列表
     */
    @Query(value = "{'name':{'$regex':?0}, 'age':{'$gte':18}}", fields = "{'id':1,'age':1}")
    List<Employee> findAllAdultsByName(String name, Pageable pageable);

}
