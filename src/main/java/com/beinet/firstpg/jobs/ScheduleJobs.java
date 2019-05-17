package com.beinet.firstpg.jobs;

import com.beinet.firstpg.configs.ConfigReader;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * 演示计划任务和配置读取.
 * 注意：在此类上添加注解： @EnableScheduling
 * 也可以添加在main函数的类上
 */
@Component
@Slf4j // 日志对象注解
@EnableScheduling
public class ScheduleJobs {
    private boolean runed;

    private final ConfigReader configs;
    @Autowired
    Environment env;
    @Autowired
    public ScheduleJobs(ConfigReader configs) {
        this.configs = configs;
    }

    /**
     * 每秒执行一次的job
     */
    @Scheduled(cron="* * * * * *")
    public void firstJob() {
        if (runed) return;
        runed = true;

//        // 注: System.getProperties()的key和value，可以在application.yml里配置，也可以通过命令行 java -Dkey=value来设置
//        System.out.println("==================");
//        System.getenv().forEach((k, v) -> System.out.println(k + "===" + v));
//        System.out.println("==================");
//        System.getProperties().forEach((k, v) -> System.out.println(k + "===" + v));//  + "===" + env.getProperty(k.toString())
//        System.out.println("==================");

        String key = "spring.datasource.driver-class-name";
        String val = ConfigReader.getConfig(key);
        log.info(key + "===" + val);

        outputConfigs();
    }

    /*
    *  Centos7环境下：
    *  System.getenv() 数据如下：
PATH===/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/usr/mongodb/bin:/root/bin
HISTCONTROL===ignoredups
LESSOPEN===||/usr/bin/lesspipe.sh %s
SHELL===/bin/bash
HISTSIZE===1000
SSH_TTY===/dev/pts/0
SSH_CLIENT===10.1.1.1 51547 22
OLDPWD===/root
TERM===linux
USER===root
LANG===en_US.UTF-8
XDG_SESSION_ID===13196
SSH_CONNECTION===10.1.1.1 51547 172.18.245.154 22
MAIL===/var/spool/mail/root
HOSTNAME===iZwz988rg4xz5nu0rg86h3Z
LOGNAME===root
XDG_RUNTIME_DIR===/run/user/0
PWD===/root/japp
LS_COLORS===rs=0:di=01;34:ln=01;36:mh=00:pi=40;33:so=01;35:do=01;35:bd=40;33;01:cd=40;33;01:or=40;31;01:mi=01;05;37;41:su=37;41:sg=30;43:ca=30;41:tw=30;42:ow=34;42:st=37;44:ex=01;32:*.tar=01;31:*.tgz=01;31:*.arc=01;31:*.arj=01;31:*.taz=01;31:*.lha=01;31:*.lz4=01;31:*.lzh=01;31:*.lzma=01;31:*.tlz=01;31:*.txz=01;31:*.tzo=01;31:*.t7z=01;31:*.zip=01;31:*.z=01;31:*.Z=01;31:*.dz=01;31:*.gz=01;31:*.lrz=01;31:*.lz=01;31:*.lzo=01;31:*.xz=01;31:*.bz2=01;31:*.bz=01;31:*.tbz=01;31:*.tbz2=01;31:*.tz=01;31:*.deb=01;31:*.rpm=01;31:*.jar=01;31:*.war=01;31:*.ear=01;31:*.sar=01;31:*.rar=01;31:*.alz=01;31:*.ace=01;31:*.zoo=01;31:*.cpio=01;31:*.7z=01;31:*.rz=01;31:*.cab=01;31:*.jpg=01;35:*.jpeg=01;35:*.gif=01;35:*.bmp=01;35:*.pbm=01;35:*.pgm=01;35:*.ppm=01;35:*.tga=01;35:*.xbm=01;35:*.xpm=01;35:*.tif=01;35:*.tiff=01;35:*.png=01;35:*.svg=01;35:*.svgz=01;35:*.mng=01;35:*.pcx=01;35:*.mov=01;35:*.mpg=01;35:*.mpeg=01;35:*.m2v=01;35:*.mkv=01;35:*.webm=01;35:*.ogm=01;35:*.mp4=01;35:*.m4v=01;35:*.mp4v=01;35:*.vob=01;35:*.qt=01;35:*.nuv=01;35:*.wmv=01;35:*.asf=01;35:*.rm=01;35:*.rmvb=01;35:*.flc=01;35:*.avi=01;35:*.fli=01;35:*.flv=01;35:*.gl=01;35:*.dl=01;35:*.xcf=01;35:*.xwd=01;35:*.yuv=01;35:*.cgm=01;35:*.emf=01;35:*.axv=01;35:*.anx=01;35:*.ogv=01;35:*.ogx=01;35:*.aac=01;36:*.au=01;36:*.flac=01;36:*.mid=01;36:*.midi=01;36:*.mka=01;36:*.mp3=01;36:*.mpc=01;36:*.ogg=01;36:*.ra=01;36:*.wav=01;36:*.axa=01;36:*.oga=01;36:*.spx=01;36:*.xspf=01;36:
HOME===/root
SHLVL===1
_===/usr/bin/java
    * ==================================================================================================================
    * System.getProperties() 数据如下：
sun.cpu.isalist===
sun.io.unicode.encoding===UnicodeLittle
sun.cpu.endian===little
java.vendor.url.bug===http://bugreport.sun.com/bugreport/
file.separator===/
com.zaxxer.hikari.pool_number===1
catalina.base===/tmp/tomcat.368407757870947775.8081
java.awt.headless===true
java.vendor===Oracle Corporation
sun.boot.class.path===/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.212.b04-0.el7_6.x86_64/jre/lib/resources.jar:/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.212.b04-0.el7_6.x86_64/jre/lib/rt.jar:/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.212.b04-0.el7_6.x86_64/jre/lib/sunrsasign.jar:/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.212.b04-0.el7_6.x86_64/jre/lib/jsse.jar:/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.212.b04-0.el7_6.x86_64/jre/lib/jce.jar:/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.212.b04-0.el7_6.x86_64/jre/lib/charsets.jar:/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.212.b04-0.el7_6.x86_64/jre/lib/jfr.jar:/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.212.b04-0.el7_6.x86_64/jre/classes
java.ext.dirs===/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.212.b04-0.el7_6.x86_64/jre/lib/ext:/usr/java/packages/lib/ext
java.version===1.8.0_212
java.vm.info===mixed mode
awt.toolkit===sun.awt.X11.XToolkit
user.language===en
java.specification.vendor===Oracle Corporation
sun.java.command===beinet-firstpg.jar
java.home===/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.212.b04-0.el7_6.x86_64/jre
sun.arch.data.model===64
java.vm.specification.version===1.8
java.class.path===beinet-firstpg.jar
user.name===root
catalina.home===/tmp/tomcat.368407757870947775.8081
@appId===firstdemo
file.encoding===UTF-8
java.specification.version===1.8
java.awt.printerjob===sun.print.PSPrinterJob
catalina.useNaming===false
user.timezone===Asia/Shanghai
user.home===/root
os.version===3.10.0-693.2.2.el7.x86_64
sun.management.compiler===HotSpot 64-Bit Tiered Compilers
java.specification.name===Java Platform API Specification
java.class.version===52.0
spring.beaninfo.ignore===true
java.library.path===/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib
sun.jnu.encoding===UTF-8
os.name===Linux
java.vm.specification.vendor===Oracle Corporation
java.io.tmpdir===/tmp
line.separator===

java.endorsed.dirs===/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.212.b04-0.el7_6.x86_64/jre/lib/endorsed
os.arch===amd64
java.awt.graphicsenv===sun.awt.X11GraphicsEnvironment
java.runtime.version===1.8.0_212-b04
PID===25900
java.vm.specification.name===Java Virtual Machine Specification
user.dir===/root/japp
user.country===US
sun.java.launcher===SUN_STANDARD
sun.os.patch.level===unknown
java.vm.name===OpenJDK 64-Bit Server VM
file.encoding.pkg===sun.io
path.separator===:
java.vm.vendor===Oracle Corporation
java.vendor.url===http://java.oracle.com/
java.protocol.handler.pkgs===org.springframework.boot.loader
sun.boot.library.path===/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.212.b04-0.el7_6.x86_64/jre/lib/amd64
java.vm.version===25.212-b04
java.runtime.name===OpenJDK Runtime Environment
    * */

    /*
     *  Win10环境下：
     *  System.getenv() 数据如下：
USERDOMAIN_ROAMINGPROFILE===DESKTOP-ALFG3IK
PROCESSOR_LEVEL===6
SESSIONNAME===Console
ALLUSERSPROFILE===C:\ProgramData
PROCESSOR_ARCHITECTURE===AMD64
PSModulePath===C:\Program Files\WindowsPowerShell\Modules;C:\Windows\system32\WindowsPowerShell\v1.0\Modules
SystemDrive===C:
=E:===E:\mysource\JavaDemo\target
=ExitCode===00000082
MAVEN_HOME===C:\apache-maven-3.6.0
USERNAME===youbl
ProgramFiles(x86)===C:\Program Files (x86)
FPS_BROWSER_USER_PROFILE_STRING===Default
PATHEXT===.COM;.EXE;.BAT;.CMD;.VBS;.VBE;.JS;.JSE;.WSF;.WSH;.MSC
DriverData===C:\Windows\System32\Drivers\DriverData
ProgramData===C:\ProgramData
ProgramW6432===C:\Program Files
HOMEPATH===\Users\youbl
PROCESSOR_IDENTIFIER===Intel64 Family 6 Model 158 Stepping 10, GenuineIntel
ProgramFiles===C:\Program Files
PUBLIC===C:\Users\Public
windir===C:\Windows
=::===::\
LOCALAPPDATA===C:\Users\youbl\AppData\Local
IntelliJ IDEA===C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\bin;
USERDOMAIN===DESKTOP-ALFG3IK
FPS_BROWSER_APP_PROFILE_STRING===Internet Explorer
LOGONSERVER===\\DESKTOP-ALFG3IK
JAVA_HOME===C:\Program Files\Java\jdk1.8.0_201
PROMPT===$P$G
OneDrive===C:\Users\youbl\OneDrive
=C:===C:\Users\youbl
APPDATA===C:\Users\youbl\AppData\Roaming
CommonProgramFiles===C:\Program Files\Common Files
Path===C:\Program Files (x86)\Common Files\Oracle\Java\javapath;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0\;C:\Windows\System32\OpenSSH\;C:\Program Files\Microsoft SQL Server\130\Tools\Binn\;C:\Program Files\dotnet\;C:\Program Files\TortoiseGit\bin;C:\Program Files\Java\jdk1.8.0_201/bin;C:\apache-maven-3.6.0\bin\;C:\Program Files\Git\bin;C:\Users\youbl\AppData\Local\Microsoft\WindowsApps;;C:\Users\youbl\AppData\Local\Programs\Fiddler;C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\bin;
OS===Windows_NT
COMPUTERNAME===DESKTOP-ALFG3IK
PROCESSOR_REVISION===9e0a
CLASSPATH===./;C:\Program Files\Java\jdk1.8.0_201/lib/tools.jar;C:\Program Files\Java\jdk1.8.0_201/lib/dt.jar
CommonProgramW6432===C:\Program Files\Common Files
ComSpec===C:\Windows\system32\cmd.exe
=D:===D:\
SystemRoot===C:\Windows
TEMP===C:\Users\youbl\AppData\Local\Temp
HOMEDRIVE===C:
USERPROFILE===C:\Users\youbl
TMP===C:\Users\youbl\AppData\Local\Temp
CommonProgramFiles(x86)===C:\Program Files (x86)\Common Files
NUMBER_OF_PROCESSORS===6
    * ==================================================================================================================
    * System.getProperties() 数据如下：
sun.cpu.isalist===amd64
sun.desktop===windows
sun.stdout.encoding===ms936
sun.io.unicode.encoding===UnicodeLittle
sun.cpu.endian===little
java.vendor.url.bug===http://bugreport.sun.com/bugreport/
file.separator===\
com.zaxxer.hikari.pool_number===1
catalina.base===C:\Users\youbl\AppData\Local\Temp\tomcat.5975311057998624242.8081
java.awt.headless===true
java.vendor===Oracle Corporation
sun.stderr.encoding===ms936
sun.boot.class.path===C:\Program Files\Java\jre1.8.0_201\lib\resources.jar;C:\Program Files\Java\jre1.8.0_201\lib\rt.jar;C:\Program Files\Java\jre1.8.0_201\lib\sunrsasign.jar;C:\Program Files\Java\jre1.8.0_201\lib\jsse.jar;C:\Program Files\Java\jre1.8.0_201\lib\jce.jar;C:\Program Files\Java\jre1.8.0_201\lib\charsets.jar;C:\Program Files\Java\jre1.8.0_201\lib\jfr.jar;C:\Program Files\Java\jre1.8.0_201\classes
java.ext.dirs===C:\Program Files\Java\jre1.8.0_201\lib\ext;C:\Windows\Sun\Java\lib\ext
java.version===1.8.0_201
java.vm.info===mixed mode
awt.toolkit===sun.awt.windows.WToolkit
user.language===zh
java.specification.vendor===Oracle Corporation
sun.java.command===beinet-firstpg.jar
java.home===C:\Program Files\Java\jre1.8.0_201
sun.arch.data.model===64
java.vm.specification.version===1.8
java.class.path===beinet-firstpg.jar
user.name===youbl
catalina.home===C:\Users\youbl\AppData\Local\Temp\tomcat.5975311057998624242.8081
@appId===firstdemo
file.encoding===GBK
java.specification.version===1.8
java.awt.printerjob===sun.awt.windows.WPrinterJob
catalina.useNaming===false
user.timezone===Asia/Shanghai
user.home===C:\Users\youbl
spring.datasource.password===123456
os.version===10.0
sun.management.compiler===HotSpot 64-Bit Tiered Compilers
java.specification.name===Java Platform API Specification
java.class.version===52.0
spring.beaninfo.ignore===true
java.library.path===C:\Program Files (x86)\Common Files\Oracle\Java\javapath;C:\Windows\Sun\Java\bin;C:\Windows\system32;C:\Windows;C:\Program Files (x86)\Common Files\Oracle\Java\javapath;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0\;C:\Windows\System32\OpenSSH\;C:\Program Files\Microsoft SQL Server\130\Tools\Binn\;C:\Program Files\dotnet\;C:\Program Files\TortoiseGit\bin;C:\Program Files\Java\jdk1.8.0_201/bin;C:\apache-maven-3.6.0\bin\;C:\Program Files\Git\bin;C:\Users\youbl\AppData\Local\Microsoft\WindowsApps;;C:\Users\youbl\AppData\Local\Programs\Fiddler;C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\bin;;.
sun.jnu.encoding===GBK
os.name===Windows 10
user.variant===
java.vm.specification.vendor===Oracle Corporation
spring.datasource.url===jdbc:mysql://10.2.0.242:3306/firstdemodb?characterEncoding=utf8&serverTimezone=UTC&useSSL=false
java.io.tmpdir===C:\Users\youbl\AppData\Local\Temp\
line.separator===

java.endorsed.dirs===C:\Program Files\Java\jre1.8.0_201\lib\endorsed
os.arch===amd64
java.awt.graphicsenv===sun.awt.Win32GraphicsEnvironment
java.runtime.version===1.8.0_201-b09
PID===10388
java.vm.specification.name===Java Virtual Machine Specification
user.dir===E:\mysource\JavaDemo\target
user.country===CN
user.script===
sun.java.launcher===SUN_STANDARD
sun.os.patch.level===
java.vm.name===Java HotSpot(TM) 64-Bit Server VM
file.encoding.pkg===sun.io
path.separator===;
java.vm.vendor===Oracle Corporation
java.vendor.url===http://java.oracle.com/
java.protocol.handler.pkgs===org.springframework.boot.loader
sun.boot.library.path===C:\Program Files\Java\jre1.8.0_201\bin
java.vm.version===25.201-b09
java.runtime.name===Java(TM) SE Runtime Environment
    */

    /**
     * 读取所有配置数据，并输出
     */
    private void outputConfigs(){
        log.info("spring.application.name : " + configs.getConfig("spring.application.name"));
        log.info("not exists : " + configs.getConfig("not.exists"));

       /*
        getFields()只能获取public的字段，包括父类的。
        getDeclaredFields()只能获取自己声明的各种字段，包括public，protected，private。
        */
        for(Field field : configs.getClass().getFields()) {
            log.info(field.getName() + " -> " + field.toString());
            try {
                Object obj = field.get(configs);
                if (obj == null) {
                    log.info("    is null.");
                    continue;
                }
                if (obj.getClass().isArray()) {
                    StringBuilder msg = new StringBuilder();
                    // 反射遍历数组
                    int j = Array.getLength(obj);
                    msg.append("长度:").append(j).append("  ");
                    for (int i = 0; i < j; i++) {
                        msg.append(Array.get(obj, i)).append(",");
                    }
                    log.info("    " + msg);
                } else {
                    log.info("    " + obj.toString());
                }
            } catch (Exception exp) {
                log.error(exp.toString());
            }
        }
    }

}
