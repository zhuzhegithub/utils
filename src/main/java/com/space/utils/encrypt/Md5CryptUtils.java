package com.space.utils.encrypt;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * @author zhuzhe
 * 2017/12/12
 * Md5工具类
 */
@Slf4j
public class Md5CryptUtils {

    private static final String encoding = "UTF-8";

    private static MessageDigest md5Digest = null;

    /**
     * 生成md5签名
     * <p/>
     * 公式：sign = md5(data + secretKey)
     * @param data 需要签名的data
     * @param secretKey 密钥
     * @return md5签名
     * @throws Exception
     */
    public static String md5Sign(String data, String secretKey) throws Exception {
        return md5Sign(data + secretKey);
    }

    /**
     * 生成md5签名
     * <p/>
     * 公式：sign = md5(data)
     * @param data 需要签名的data
     * @return
     * @throws Exception
     */
    public static String md5Sign(String data) throws Exception {
        String sign = null;
        try {
            if (md5Digest == null) {
                md5Digest = MessageDigest.getInstance("MD5");
            }
            byte[] bytes = md5Digest.digest(data.getBytes(encoding));
            sign = byte2hex(bytes);
        } catch (Exception e) {
            log.error("md5 sign exception, data:{}", data, e);
            throw e;
        }
        return sign;
    }

    /**
     * 二进制转化为十六进制
     * @param bytes
     * @return
     */
    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex);
        }
        return sign.toString();
    }
}
