## 项目简介
`fota_update` 是一个提供Fota升级、设备上线监控、网络信号等设备信息及分析管理功能的 `spring boot` 的项目，
该项目已有设备管理、导入设备并创建任务、升级项目管理、版本文件添加、以及查看设备udp上报数据的功能

## 开发环境
- **JDK 1.8**
- **Maven 3.6.3**
- **IntelliJ IDEA ULTIMATE 2020.1.2 +** 
- **Mysql 5.7.32**
- **redis**
## 运行方式

1. `git clone http://git.ranchip.com:6280/fota-/fota-server.git`
2. 使用 IDEA 打开 clone 下来的项目
3. 在 IDEA 中 Maven Projects 的面板导入项目根目录下 的 `pom.xml` 文件


### 各 Module 介绍
| Module 名称                                                  | Module 介绍                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [annotation]                                                 | 用于获取excel输入和设备鉴权管理的注解                              |
| [config]                                                     | 用于进行配置的类信息                            |
| [controller]                                                 | 处理显示信息的controller层 |
| [dao]                                                        | 进行数据库查询语句编写的数据库访问层 |
| [entity]                                                     | java bean |
| [exception]                                                  | 全局异常处理                                |
| [filter]                                                     | 用户登录逻辑             |
| [interceptor]                                                | 设备登录逻辑 |
| [redis]                                                      | 生成redis的key信息                        |
| [result]                                                     | 查询返回结果封装                         |
| [security]                                                   | 密码编码解码认证                              |
| [service]                                                    | 业务处理                              |
| [udpjson]                                                    | 解析udp发来的数据 |
| [util]                                                       | 一些复用的工具类    |

###部署


##### 配置`docker`远程连接

参考连接：https://blog.csdn.net/chenfeidi1/article/details/80866889

在`/usr/lib/systemd/system`目录下，修改`docker.service`文件，如下图所示



![](/images/1645409924(1).jpg)

注意修改结束后，重启docker服务

```bash
$ sudo systemctl daemon-reload 
$ sudo systemctl restart docker
```



#### 3.2.2 IDEA连接`docker`

注意！在此之前，需要在阿里云上将端口开放。默认阿里云的安全组仅开放了少量端口。

在IDEA中，`File --> Settings`如下图所示：

![](/images/dockercon.jpg)

点击 + 添加一个docker的连接，填上`ip`和端口后，最下面显示Connection Successful表示连接成功。



#### 3.2.3 编写`Dockerfile`文件

```bash
# Dockerfile
FROM java:8
MAINTAINER xuch
ADD target/fota_update-1.0-SNAPSHOT.jar app.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
ENTRYPOINT ["java","-jar","app.jar"]
```

注意 `fota_update-1.0-SNAPSHOT.jar`  这里的可执行文件名和目录要和打包生成的对应。



#### 3.2.4 部署

![](/images/部署1.jpg)



![](/images/部署2.jpg)



配置如下所示：

![](/images/部署3.jpg)

完成了上述文件的配置之后，我们就可以点击部署了，操作如下



![](/images/部署4.jpg)
选择上一步的配置

###目前需要注意的
1.HardwareController中的update接口，会通过升级后的版本名信息判断是否升级成功，但好像设备端升级后给的版本名没有变化导致没有正常的改变任务状态，需要排查
2.由于目前不知道udp上报数据时间戳的格式，目前使用的是fota系统的时间戳