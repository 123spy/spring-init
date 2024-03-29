# SpringBoot 项目开发模板

>  作者：程序员spy

---

## 模板特点

### 技术栈

- Spring Boot
- Spring MVC
- MyBatis + MyBatis Plus 数据访问
- Spring Boot 调试工具和项目处理器
- Spring AOP 切面编程

### 数据存储

- MySQL 数据库
- Redis 内存数据库

### 工具类

- Apache Commons Lang3 工具类
- Lombok 注解

### 业务特性

- Spring Session Redis 分布式登录
- 全局请求响应拦截器（记录日志）
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- Swagger + Knife4j 接口文档
- 自定义权限注解 + 全局校验
- 全局跨域处理
- 长整数丢失精度解决
- 多环境配置

### 业务功能

- 用户登录、注册、注销、更新、检索、权限管理

## 快速上手

### MySQL 数据库配置

1）修改 application.yml 的数据库配置为你自己的：

```yaml
spring:
  # DataSource Config
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/springinit?serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

2）执行 sql/create_sql.sql 中的数据库语句，自动创建库表
3）启动项目，访问 http://localhost:8080/api/doc.html 即可打开接口文档，不需要写前端就能在线调试接口了~

### Redis 分布式登录

1）修改 application.yml 的 Redis 配置为你自己的：

```yaml
spring:
  redis:
    database: 1
    host: localhost
    port: 6379
    timeout: 5000
    password: 123456
```

2）修改 application.yml 中的 session 存储方式：

```yaml
spring:
  session:
    store-type: redis
```