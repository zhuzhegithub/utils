package com.space.utils.encrypt;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * HmacSha256加密工具类
 * @author zhuzhe
 * @date 2018/4/9 11:04
 */
@Slf4j
public class HmacSha256Utils {

    /**
     * HmacSha256加密
     * @param message 消息
     * @param secret  秘钥
     * @return 加密后字符串
     */
    public static String encrypt(String message, String secret){
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            return byteArrayToHexString(bytes);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String temp = null;
        for (int n = 0; b!=null && n < b.length; n++) {
            temp = Integer.toHexString(b[n] & 0XFF);
            if (temp.length() == 1) {
                hs.append('0');
            }
            hs.append(temp);
        }
        return hs.toString().toUpperCase();
    }
}
