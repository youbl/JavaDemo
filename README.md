# JavaDemo
初学Java的Demo演示程序

- 配置读取演示：  
configs/ConfigReader: 依赖于Spring Boot，  
　　读取application.yml里的配置数据。
configs/ConfigHelper: 独立，不依赖Spring  
　　读取application.yml里的配置数据。　　
configs/YmlHelper: Yml解析工具    

- WebSite的Controller使用演示：  
controller/MainController

- 计划任务使用演示：  
jobs/ScheduleJobs

- mysql读写演示：  
mysql/MySqlTest  
有JPA操作 和 Jdbc批量插入2类Demo

- HttpURLConnection封装和请求演示：  
/test/java/com.beinet.firstpg/httpTest/HttpHelperTest  
注：HttpHelper封装，支持301和302，支持cookie传递，完全自定义  

- feign进行api调用演示：  
/test/java/com.beinet.firstpg/httpTest/FeignTest  
注：不支持301/302，但支持Eureka服务名调用

- 数组排序方法演示：  
/test/java/com.beinet.firstpg/arrayTest/ArrayTest.java

- Redis读写演示
/test/java/com.beinet.firstpg/redisTest/RedisTest.java

- 文件读取、写入、覆盖演示（带编码）：
/test/java/com.beinet.firstpg/fileTest/FileHelperTest.java