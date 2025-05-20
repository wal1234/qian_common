package com.qian.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT工具类 - 统一的JWT令牌处理工具
 *
 * @author 王茜兰
 */
@Slf4j
@Component
public class JwtUtils {

    // Token在Claims中的字段名
    private static final String CLAIM_KEY_USER_ID = "userId";
    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_ROLES = "roles";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 默认24小时
    private long expiration;

    /**
     * 生成JWT令牌 - 基于UserDetails
     *
     * @param userDetails Spring Security用户信息
     * @return JWT令牌
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        return generateToken(claims, userDetails.getUsername());
    }

    /**
     * 生成JWT令牌 - 基于用户ID和角色
     *
     * @param userId 用户ID
     * @param roles  用户角色列表
     * @return JWT令牌
     */
    public String generateToken(String userId, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USER_ID, userId);
        claims.put(CLAIM_KEY_ROLES, roles);
        return generateToken(claims, userId);
    }

    /**
     * 生成JWT令牌 - 基于用户ID、用户名和角色
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param roles    用户角色列表
     * @return JWT令牌
     */
    public String generateToken(String userId, String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USER_ID, userId);
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_ROLES, roles);
        return generateToken(claims, userId);
    }

    /**
     * 生成JWT令牌 - 内部实现
     *
     * @param claims  声明信息
     * @param subject 主题(通常是用户ID或用户名)
     * @return JWT令牌
     */
    private String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 解析JWT令牌
     *
     * @param token JWT令牌
     * @return Claims 声明信息
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 解析JWT令牌（兼容旧API）
     *
     * @param token JWT令牌
     * @return Claims 声明信息
     * @deprecated 请使用 {@link #parseToken(String)} 替代
     */
    @Deprecated
    public Claims parseJwt(String token) {
        return parseToken(token);
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get(CLAIM_KEY_USER_ID, String.class);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从令牌中获取用户角色
     *
     * @param token JWT令牌
     * @return 用户角色列表
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get(CLAIM_KEY_ROLES, List.class);
    }

    /**
     * 从令牌中获取过期时间
     *
     * @param token JWT令牌
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从令牌中获取指定声明
     *
     * @param token          JWT令牌
     * @param claimsResolver 声明解析函数
     * @return 解析结果
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 验证令牌 - 基本验证
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("JWT签名验证失败: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JWT令牌格式错误: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT令牌已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("不支持的JWT令牌: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT解析异常: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 验证令牌 - 针对UserDetails
     *
     * @param token       JWT令牌
     * @param userDetails Spring Security用户信息
     * @return 是否有效
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断令牌是否过期
     *
     * @param token JWT令牌
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 获取签名密钥
     *
     * @return 签名密钥
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
} 