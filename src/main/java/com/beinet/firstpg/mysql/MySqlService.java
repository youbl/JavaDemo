package com.beinet.firstpg.mysql;

import com.beinet.firstpg.configs.ConfigReader;
import com.beinet.firstpg.mysql.entity.Users;
import com.beinet.firstpg.mysql.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作服务类，表结构定义：
 CREATE TABLE `users` (
 `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
 `account` varchar(20) NOT NULL DEFAULT '' COMMENT '账号',
 `userName` varchar(50) NOT NULL DEFAULT '' COMMENT '姓名',
 `pwd` varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
 `CreationTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `LastModificationTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
 PRIMARY KEY (`id`),
 UNIQUE KEY `unq_account` (`account`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表'
 */
@Service
@Slf4j
public class MySqlService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    ApplicationContext applicationContext;

    // <editor-fold desc="JPA调用相关方法">


    /**
     * 添加用户
     */
    public Users AddUser(String account, String userName, String password) {
        Users user = new Users();
        user.setAccount(account);
        user.setUserName(userName);
        user.setPwd(password);
        user = usersRepository.save(user);
        return user;
    }

    public Users GetUserByAccount(String account) {
        return usersRepository.findByAccount(account);
    }

    /**
     * 返回所有用户数据
     *
     * @return 用户列表
     */
    public List<Users> GetUsers() {
        return usersRepository.findAll();
    }

    /**
     * 返回最近7天创建的用户数据
     */
    public List<Users> GetUsersByWeek() {
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = time1.minusDays(7);
        return usersRepository.findAllByCreationTimeAfterAndCreationTimeAfterOrderByAccountDesc(time2, time1);
    }

    // </editor-fold>



    // <editor-fold desc="JdbcTemplate执行自定义SQL相关方法">

    /**
     * 用单条sql 批量插入实现
     *
     * @return 插入行数
     */
    public int BatchInsert(List<Users> users) {
        String sql = "INSERT INTO users (account,userName,pwd) VALUES";
        StringBuilder sb = new StringBuilder();
        for (Users user : users) {
            if(sb.length() > 0)
                sb.append(",");
            // 字符串替换单引号，避免注入
            sb.append("('").append(sqlStr(user.getAccount())).append("','")
                    .append(sqlStr(user.getUserName())).append("','")
                    .append(sqlStr(user.getPwd())).append("')");
        }
        System.out.println(sql + sb);
        return jdbcTemplate.update(sql + sb);
    }

    /**
     * 自定义数据源，实例化JdbcTemplate对象 示例
     * @param sql
     * @return
     */
    public List<String[]> Execute(String sql) {
//        // 这一句是获取application.yml里的数据库配置，可以替换下面5句
//        DataSource dataSourceConfig = applicationContext.getBean(DataSource.class);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(ConfigReader.getConfig("spring.datasource.driver-class-name"));
        dataSource.setUrl(ConfigReader.getConfig("spring.datasource.url"));
        dataSource.setUsername(ConfigReader.getConfig("spring.datasource.username"));
        dataSource.setPassword(ConfigReader.getConfig("spring.datasource.password"));

        //创建JdbcTemplate对象，设置数据源
        JdbcTemplate template = new JdbcTemplate(dataSource);
        // String sql = "select * from users";
        List<String[]> arr = template.query(sql, new MyRowMapper());
        return arr;
    }

    class MyRowMapper implements RowMapper<String[]> {
        public String[] mapRow(ResultSet resultSet, int i) throws SQLException {
            return getRow(resultSet);
        }
    }

    /**
     * 自定义数据源，实例化JdbcTemplate对象 示例
     * @param sql
     * @return
     */
    public List<String[]> Execute2(String sql) throws SQLException {
        // 读取配置文件配置
        DataSource dataSourceConfig = applicationContext.getBean(DataSource.class);
        Connection conn =dataSourceConfig.getConnection();
//        String url = ConfigReader.getConfig("spring.datasource.url");
//        String user = ConfigReader.getConfig("spring.datasource.username");
//        String pwd = ConfigReader.getConfig("spring.datasource.password");
//        Connection conn = DriverManager.getConnection(url, user, pwd);

        List<String[]> ret = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            try (ResultSet rs = pst.executeQuery()) {
                while(rs.next()){
                    String[] row = getRow(rs);
                    ret.add(row);
                }
            }
        }
        return ret;
    }

    String[] getRow(ResultSet rs) throws SQLException {
        String[] ret = new String[6];
        ret[0] = rs.getString("id");
        ret[1] = rs.getString("account");
        ret[2] = rs.getString("userName");
        ret[3] = rs.getString("pwd");
        ret[4] = rs.getString("creationTime");
        ret[5] = rs.getString("lastModificationTime");
        return ret;
    }
    // </editor-fold>


    private String sqlStr(String str){
        if(str == null || str.length() == 0)
            return "";
        return str.replace("'", "''");
    }
}
