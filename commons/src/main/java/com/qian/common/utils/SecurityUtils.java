package com.qian.common.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.qian.common.domain.LoginUser;
import com.qian.common.core.domain.entity.SysUser;
import com.qian.common.exception.ServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import cn.hutool.core.util.StrUtil;
import com.qian.common.constant.HttpStatus;

/**
 * AES-CBC-PKCS7Padding加密工具类（使用BouncyCastleProvider）
 * 支持256位密钥 + HMAC完整性校验
 */
public class SecurityUtils {
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 10000;
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 16;
    private static final String ALGORITHM = "AES/CBC/PKCS7Padding";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    
    /**
     * AES加密（包含HMAC校验）
     * @param plaintext 明文
     * @param password 加密密码
     * @return base64(salt + iv + ciphertext + hmac)
     */
    public static String encrypt(String plaintext, String password) {
        try {
            // 生成随机盐值
            byte[] salt = generateSalt();
            
            // 生成密钥
            SecretKey secretKey = generateSecretKey(password.toCharArray(), salt);
            
            // 生成随机IV
            byte[] iv = generateIv();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            
            // AES加密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
            
            // 生成HMAC校验码
            byte[] hmac = generateHmac(secretKey, ciphertext);
            
            // 拼接完整密文：salt + iv + ciphertext + hmac
            byte[] combined = new byte[salt.length + iv.length + ciphertext.length + hmac.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(iv, 0, combined, salt.length, iv.length);
            System.arraycopy(ciphertext, 0, combined, salt.length + iv.length, ciphertext.length);
            System.arraycopy(hmac, 0, combined, salt.length + iv.length + ciphertext.length, hmac.length);
            
            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException e) {
            throw new SecurityException("加密过程发生异常", e);
        }
    }

    /**
     * AES解密（包含完整性校验）
     * @param encryptedText 密文
     * @param password 解密密码
     * @return 明文
     */
    public static String decrypt(String encryptedText, String password) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            throw new IllegalArgumentException("密文不能为空");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedText);
            
            // 解析数据块
            int pos = 0;
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(combined, pos, salt, 0, SALT_LENGTH);
            pos += SALT_LENGTH;
            
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(combined, pos, iv, 0, IV_LENGTH);
            pos += IV_LENGTH;
            
            if (combined.length < SALT_LENGTH + IV_LENGTH + 32) {
                throw new SecurityException("无效的密文格式");
            }
            int minLength = SALT_LENGTH + IV_LENGTH + 32;
            byte[] ciphertext = new byte[combined.length - minLength];
            System.arraycopy(combined, pos, ciphertext, 0, ciphertext.length);
            pos += ciphertext.length;
            
            byte[] hmac = new byte[32];
            System.arraycopy(combined, pos, hmac, 0, 32);
            
            // 生成密钥
            SecretKey secretKey = generateSecretKey(password.toCharArray(), salt);
            
            // 校验HMAC
            if (!verifyHmac(secretKey, ciphertext, hmac)) {
                throw new SecurityException("HMAC校验失败，数据可能被篡改");
            }
            
            // AES解密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] plaintext = cipher.doFinal(ciphertext);
            
            return new String(plaintext);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException e) {
            throw new SecurityException("解密过程发生异常", e);
        }
    }

    private static SecretKey generateSecretKey(char[] password, byte[] salt) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (password == null || password.length == 0) {
            throw new IllegalArgumentException("密码不能为空");
        }
        if (salt == null || salt.length != SALT_LENGTH) {
            throw new IllegalArgumentException("无效的盐值长度");
        }
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private static byte[] generateHmac(SecretKey key, byte[] data) 
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        return mac.doFinal(data);
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }

    private static boolean verifyHmac(SecretKey key, byte[] data, byte[] expectedHmac) 
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] actualHmac = generateHmac(key, data);
        return constantTimeEquals(actualHmac, expectedHmac);
    }

    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ServiceException("获取用户信息异常");
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        try {
            return Long.parseLong(getLoginUser().getUsername());
        } catch (Exception e) {
            throw new ServiceException("获取用户ID异常");
        }
    }

    /**
     * 获取部门ID
     */
    public static Long getDeptId() {
        try {
            return getLoginUser().getUser().getDeptId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户
     */
    public static SysUser getUser() {
        try {
            return getLoginUser().getUser();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}