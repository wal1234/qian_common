# JWT工具类迁移指南

本文档提供从旧的JWT工具类（`JwtUtil`和`JwtTokenUtil`）迁移到新的统一JWT工具类（`JwtUtils`）的指南。

## 背景

项目中原有两个类似功能的JWT工具类：
- `com.qian.common.utils.JwtUtil`：主要用于基于用户ID和角色的令牌生成和验证
- `com.qian.security.JwtTokenUtil`：主要用于基于Spring Security的UserDetails的令牌生成和验证

为了简化代码维护和统一API，我们创建了一个新的`com.qian.common.utils.JwtUtils`类，它整合了上述两个类的功能。

## 迁移步骤

### 1. 更新依赖注入

将代码中注入的`JwtUtil`或`JwtTokenUtil`更改为注入`JwtUtils`：

```java
// 旧代码
@Autowired
private JwtUtil jwtUtil;
// 或
@Autowired
private JwtTokenUtil jwtTokenUtil;

// 新代码
@Autowired
private JwtUtils jwtUtils;
```

### 2. 方法映射

#### 从`JwtUtil`迁移

| 旧方法（JwtUtil） | 新方法（JwtUtils） | 说明 |
|-----------------|------------------|-----|
| `generateToken(String userId, List<String> roles)` | `generateToken(String userId, List<String> roles)` | 生成基于用户ID和角色的令牌 |
| `parseJwt(String token)` | `parseToken(String token)` | 解析JWT令牌获取Claims |
| `getUserIdFromToken(String token)` | `getUserIdFromToken(String token)` | 获取用户ID |
| `getRolesFromToken(String token)` | `getRolesFromToken(String token)` | 获取用户角色 |
| `validateToken(String token)` | `validateToken(String token)` | 验证令牌是否有效 |
| `isTokenExpired(String token)` | `isTokenExpired(String token)` | 判断令牌是否过期 |

#### 从`JwtTokenUtil`迁移

| 旧方法（JwtTokenUtil） | 新方法（JwtUtils） | 说明 |
|----------------------|------------------|-----|
| `generateToken(UserDetails userDetails)` | `generateToken(UserDetails userDetails)` | 生成基于UserDetails的令牌 |
| `getUsernameFromToken(String token)` | `getUsernameFromToken(String token)` | 获取用户名 |
| `getExpirationDateFromToken(String token)` | `getExpirationDateFromToken(String token)` | 获取过期时间 |
| `getClaimFromToken(String token, Function<Claims, T> claimsResolver)` | `getClaimFromToken(String token, Function<Claims, T> claimsResolver)` | 获取指定声明 |
| `validateToken(String token, UserDetails userDetails)` | `validateToken(String token, UserDetails userDetails)` | 验证令牌是否适用于指定用户 |

### 3. 新增功能

新的`JwtUtils`类还提供了一些额外的功能：

- `generateToken(String userId, String username, List<String> roles)`：生成包含用户ID、用户名和角色的令牌
- 更详细的异常处理和日志记录
- 默认的过期时间配置（86400000毫秒，即24小时）
- 向后兼容方法`parseJwt`（作为`parseToken`的别名）以简化迁移

### 4. 配置变化

确保在应用配置文件（如`application.yml`或`application.properties`）中设置以下属性：

```yaml
jwt:
  # JWT密钥
  secret: your-secret-key
  # 令牌过期时间（毫秒），可选，默认24小时
  expiration: 86400000
```

## 注意事项

1. 新的`JwtUtils`类使用HS256算法签名，而旧的`JwtTokenUtil`类使用HS512算法。这种变化对大多数应用不会产生影响，但如果您的应用依赖特定算法，请注意这一差异。

2. 旧类已经标记为`@Deprecated`，但为保持兼容性，它们在短期内不会被移除。建议尽快完成迁移。

3. 在迁移过程中，如果遇到任何问题，请参考`README.md`文件了解更多关于新JWT工具类的使用细节。

## 示例

### 生成令牌

```java
// 旧代码 - JwtUtil
String token = jwtUtil.generateToken(userId, roles);

// 新代码 - JwtUtils
String token = jwtUtils.generateToken(userId, roles);
// 或，如果有用户名
String token = jwtUtils.generateToken(userId, username, roles);
```

### 验证令牌

```java
// 旧代码 - JwtTokenUtil
boolean isValid = jwtTokenUtil.validateToken(token, userDetails);

// 新代码 - JwtUtils
boolean isValid = jwtUtils.validateToken(token, userDetails);
```

### 从令牌提取信息

```java
// 旧代码 - JwtUtil
String userId = jwtUtil.getUserIdFromToken(token);
List<String> roles = jwtUtil.getRolesFromToken(token);

// 新代码 - JwtUtils
String userId = jwtUtils.getUserIdFromToken(token);
List<String> roles = jwtUtils.getRolesFromToken(token);
``` 