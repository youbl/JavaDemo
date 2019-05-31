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

- WebSite的swagger文档接入演示：  
controller/MainController  
controller/demo/demoController.java  
访问演示地址： http://localhost:8081/swagger-ui.html  

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

- 文件读取、写入、覆盖演示（带字符编码格式）：
/test/java/com.beinet.firstpg/fileTest/FileHelperTest.java  

- RabbitMQ消息队列 创建交换器、队列、生产消息、消费消息演示：
/test/java/com.beinet.firstpg/mqTest/RabbitMQTest.java

- Kafka消息队列 创建删除Topic、生产消息、消费消息演示：
/test/java/com.beinet.firstpg/mqTest/KafkaTest.java

- MongoDB读取、写入、更新 演示：
/test/java/com/beinet/firstpg/mongoTest/MongoTest.java