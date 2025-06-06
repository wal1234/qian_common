# 日志服务接入文档

## 1. 简介

本文档描述了如何在微服务中接入统一的日志服务。日志服务提供了操作日志和登录日志的记录功能，支持异步处理，确保日志记录不影响主业务流程。

## 2. 依赖配置

在微服务的 `pom.xml` 中添加以下依赖：

```xml
<dependency>
    <groupId>com.qian</groupId>
    <artifactId>qian-common</artifactId>
    <version>${project.version}</version>
</dependency>
```

## 3. 配置说明

### 3.1 异步配置

在微服务中添加异步支持配置：

```java
@Configuration
@EnableAsync
public class AsyncConfig {
    // 可以添加自定义的线程池配置
}
```

### 3.2 数据库配置

确保微服务的数据库中存在以下表：

1. 操作日志表：`sys_operation_log`
2. 登录日志表：`sys_login_log`

## 4. 使用说明

### 4.1 注解方式（推荐）

在需要记录操作日志的方法上添加 `@Log` 注解：

```java
@Log(title = "用户管理", businessType = BusinessType.INSERT)
@PostMapping("/add")
public AjaxResult add(@RequestBody SysUser user) {
    // 业务逻辑
    return AjaxResult.success();
}
```

### 4.2 工具类方式

#### 4.2.1 记录登录日志

```java
// 登录成功
LogUtils.recordLoginLog(username, "0", "登录成功");

// 登录失败
LogUtils.recordLoginLog(username, "1", "用户名或密码错误");
```

#### 4.2.2 记录操作日志

```java
LogUtils.recordOperLog(
    "用户管理",           // 模块名称
    BusinessType.INSERT, // 业务类型
    "addUser",          // 方法名称
    "POST",             // 请求方式
    OperatorType.MANAGE,// 操作类别
    username,           // 操作人员
    ip,                 // 操作IP
    location,           // 操作地点
    params,             // 操作参数
    result,             // 返回结果
    0,                  // 操作状态（0正常 1异常）
    null                // 错误消息
);
```

### 4.3 事件方式（不推荐）

如果上述方式不满足需求，可以直接使用事件方式：

```java
@Autowired
private ApplicationEventPublisher eventPublisher;

// 发布操作日志事件
eventPublisher.publishEvent(new OperationLogEvent(...));

// 发布登录日志事件
eventPublisher.publishEvent(new LoginLogEvent(...));
```

## 5. 日志查询

### 5.1 操作日志查询

注入操作日志服务：

```java
@Autowired
private ISysOperLogService operLogService;
```

查询操作日志：

```java
// 查询条件
SysOperLog operLog = new SysOperLog();
operLog.setTitle("操作模块");
operLog.setBusinessType(1);
operLog.setStatus(0);

// 查询日志列表
List<SysOperLog> list = operLogService.selectOperLogList(operLog);

// 查询单条日志
SysOperLog log = operLogService.selectOperLogById(1L);

// 删除日志
operLogService.deleteOperLogByIds(new Long[]{1L, 2L});

// 清空日志
operLogService.cleanOperLog();
```

### 5.2 登录日志查询

注入登录日志服务：

```java
@Autowired
private ISysLoginLogService loginLogService;
```

查询登录日志：

```java
// 查询条件
SysLoginLog loginLog = new SysLoginLog();
loginLog.setUsername("username");
loginLog.setStatus("0");

// 查询日志列表
List<SysLoginLog> list = loginLogService.selectLoginLogList(loginLog);

// 删除日志
loginLogService.deleteLoginLogByIds(new Long[]{1L, 2L});

// 清空日志
loginLogService.cleanLoginLog();
```

## 6. 注意事项

1. 日志记录采用异步方式，不会阻塞主业务流程
2. 所有日志操作都采用软删除机制
3. 日志查询支持多条件组合查询
4. 建议定期清理历史日志数据
5. 日志记录失败不会影响主业务流程，但会在系统日志中记录错误信息
6. 推荐使用注解方式记录操作日志，代码更简洁
7. 登录日志推荐使用工具类方式记录

## 7. 常见问题

### 7.1 日志没有记录

检查：
1. 是否正确添加了 `@Log` 注解
2. 是否正确调用了工具类方法
3. 检查系统日志中是否有错误信息

### 7.2 日志查询不到数据

检查：
1. 数据库连接是否正常
2. 查询条件是否正确
3. 数据是否被软删除

### 7.3 日志记录性能问题

建议：
1. 调整异步线程池配置
2. 定期清理历史数据
3. 优化数据库索引

## 8. 更新日志

### v1.1.0 (2024-03-xx)
- 新增注解方式记录操作日志
- 新增工具类方式记录日志
- 优化日志记录方式

### v1.0.0 (2024-03-xx)
- 初始版本发布
- 支持操作日志和登录日志
- 实现异步处理机制
- 提供完整的CRUD接口 