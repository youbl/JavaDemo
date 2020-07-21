package com.beinet.firstpg.mysql;

import com.beinet.firstpg.configs.ConfigReader;
import com.beinet.firstpg.mysql.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateDemo {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ApplicationContext applicationContext;

    // <editor-fold desc="JdbcTemplate执行自定义SQL相关方法">

    /**
     * 使用自动装配的JdbaTemplate，返回数据库清单
     * @return 数据库清单
     */
    public List<String> getDatabases() {
        String sql = "SELECT DISTINCT table_schema FROM information_schema.tables ORDER BY table_schema";
        return jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString(1); // 每行返回第1列
            }
        });
    }
    /**
     * 使用自动装配的JdbaTemplate，参数化查询指定数据库下的所有表
     * @param database 哪个库
     * @return 表清单
     */
    public List<String> getTables(String database) {
        String sql = "SELECT DISTINCT table_name FROM information_schema.tables WHERE table_schema=? ORDER BY table_name";
        return jdbcTemplate.query(sql, new Object[]{database}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString(1); // 每行返回第1列
            }
        });
    }

    /**
     * 使用自动装配的JdbaTemplate，使用数组参数化查询指定数据库下的所有表
     * @param database 哪个库
     * @return 表清单
     */
    public List<String> getTables(Iterable<String> database) {
        String sql = "SELECT DISTINCT table_schema, table_name FROM information_schema.tables " +
                "WHERE table_schema in (:dbs) ORDER BY table_schema, table_name";
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dbs", database);
        return jdbc.query(sql, paramMap, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString(1) + ":" + resultSet.getString(2);
            }
        });
    }

    /**
     * 用单条sql 批量插入实现
     * 使用自动装配得到的JdbaTemplate
     *
     * @return 插入行数
     */
    public int BatchInsert(List<Users> users) {
        String sql = "INSERT INTO users (account,userName,pwd) VALUES";
        StringBuilder sb = new StringBuilder();
        for (Users user : users) {
            if (sb.length() > 0)
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
     *
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
        /**
         * 把数据库的行，转换为字符串数组返回
         *
         * @param resultSet 数据集
         * @param i         当前第几行数据
         * @return 行数据得到的结果
         * @throws SQLException 数据库异常
         */
        public String[] mapRow(ResultSet resultSet, int i) throws SQLException {
            return getRow(resultSet);
        }
    }

    /**
     * 自定义数据源，实例化JdbcTemplate对象 示例
     *
     * @param sql
     * @return
     */
    public List<String[]> Execute2(String sql) throws SQLException {
        // 读取配置文件配置
        DataSource dataSourceConfig = applicationContext.getBean(DataSource.class);
        Connection conn = dataSourceConfig.getConnection();
//        String url = ConfigReader.getConfig("spring.datasource.url");
//        String user = ConfigReader.getConfig("spring.datasource.username");
//        String pwd = ConfigReader.getConfig("spring.datasource.password");
//        Connection conn = DriverManager.getConnection(url, user, pwd);

        List<String[]> ret = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
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


    private String sqlStr(String str) {
        if (str == null || str.length() == 0)
            return "";
        return str.replace("'", "''");
    }
}
