# JWT工具类说明

## 简介
`JwtUtils` 是一个统一的JWT (JSON Web Token) 处理工具类，提供了令牌生成、解析、验证等功能，替代了原来分散的 `JwtUtil` 和 `JwtTokenUtil` 类。

## 主要功能

### 令牌生成
- 支持基于UserDetails生成令牌
  ```java
  String token = jwtUtils.generateToken(userDetails);
  ```

- 支持基于用户ID和角色生成令牌
  ```java
  String token = jwtUtils.generateToken("123456", rolesList);
  ```

- 支持基于用户ID、用户名和角色生成令牌
  ```java
  String token = jwtUtils.generateToken("123456", "admin", rolesList);
  ```

### 令牌解析
- 解析JWT令牌
  ```java
  Claims claims = jwtUtils.parseToken(token);
  ```

- 从令牌中获取用户ID
  ```java
  String userId = jwtUtils.getUserIdFromToken(token);
  ```

- 从令牌中获取用户名
  ```java
  String username = jwtUtils.getUsernameFromToken(token);
  ```

- 从令牌中获取用户角色
  ```java
  List<String> roles = jwtUtils.getRolesFromToken(token);
  ```

### 令牌验证
- 基本验证（检查令牌格式和签名是否有效）
  ```java
  boolean isValid = jwtUtils.validateToken(token);
  ```

- 基于UserDetails验证（检查用户名是否匹配且令牌未过期）
  ```java
  boolean isValid = jwtUtils.validateToken(token, userDetails);
  ```

- 检查令牌是否过期
  ```java
  boolean isExpired = jwtUtils.isTokenExpired(token);
  ```

## 配置说明
在application.yml或application.properties中配置：

```yaml
jwt:
  # JWT密钥，建议使用足够复杂的随机字符串
  secret: your-secret-key-must-be-very-long-and-secure
  # 令牌过期时间（毫秒），默认24小时
  expiration: 86400000
```

## 异常处理
令牌验证失败时会返回false，并记录详细的错误日志，包括：
- 签名验证失败
- 令牌格式错误
- 令牌已过期
- 不支持的令牌类型
- 其他JWT解析异常

## 使用示例
在认证服务中：
```java
@Service
public class AuthService {
    @Autowired
    private JwtUtils jwtUtils;
    
    public String login(String username, String password) {
        // 验证用户名密码...
        
        // 生成令牌
        List<String> roles = getUserRoles(userId);
        return jwtUtils.generateToken(userId, username, roles);
    }
}
```

在过滤器中：
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        // 从请求头获取令牌
        String token = getTokenFromRequest(request);
        
        if (token != null && jwtUtils.validateToken(token)) {
            // 获取用户信息并设置安全上下文
            String userId = jwtUtils.getUserIdFromToken(token);
            List<String> roles = jwtUtils.getRolesFromToken(token);
            
            // 设置认证信息...
        }
        
        chain.doFilter(request, response);
    }
} 