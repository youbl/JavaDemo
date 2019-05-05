# JavaDemo
初学Java的Demo演示程序

- 配置读取演示：  
configs/ConfigReader:   
读取application.yml里的配置数据, 并在jobs/ScheduleJobs.outputConfigs方法里输出展示。  

- WebSite的Controller使用演示：  
controller/MainController

- 计划任务使用演示：  
jobs/ScheduleJobs

- mysql读写演示：  
mysql/MySqlTest


- HttpURLConnection封装和请求演示：  
/test/java/com.beinet.firstpg/httpTest/HttpHelperTest  
注：HttpHelper封装，支持301和302，支持cookie传递，完全自定义  

- feign进行api调用演示：  
/test/java/com.beinet.firstpg/httpTest/FeignTest  
注：不支持301/302，但支持Eureka服务名调用

