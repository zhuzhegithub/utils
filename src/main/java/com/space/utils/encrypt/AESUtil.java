package com.space.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * <p>
 * AES密码学中的高级加密标准（Advanced Encryption Standard，AES），
 * 又称高级加密标准 Rijndael加密法，是美国联邦政府采用的一种区块加密标准。
 * 这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用。
 * 经过五年的甄选流程，高级加密标准由美国国家标准与技术研究院（NIST）于2001年11月26日发布于FIPS PUB 197，
 * 并在2002年5月26日成为有效的标准。2006年，高级加密标准已然成为对称密钥加密中最流行的算法之一。
 * 该算法为比利时密码学家Joan Daemen和Vincent Rijmen所设计，结合两位作者的名字，以Rijndael之命名之，
 * 投稿高级加密标准的甄选流程。
 * </p>
 *
 * @author zhuzhe
 * @date 2019/7/25 11:06
 * @email zhuzhe_mail@163.com
 */
public class AESUtil {

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bytes = doAES(Cipher.ENCRYPT_MODE, data.getBytes(), key.getBytes());
        // base64编码字节
        return new String(new Base64().encode(bytes), "utf-8");
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        byte[] src = new Base64().decode(data);
        byte[] bytes = doAES(Cipher.DECRYPT_MODE, src, key.getBytes());
        return new String(bytes, "utf-8");
    }

    private static byte[] doAES(int mode, byte[] data, byte[] key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        // 1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // 2.根据ecnodeRules规则初始化密钥生成器
        // 生成一个128位的随机源,根据传入的字节数组
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key);
        keyGenerator.init(128, secureRandom);
        // 3.产生原始对称密钥
        SecretKey secretKey = keyGenerator.generateKey();
        // 4.获得原始对称密钥的字节数组
        byte[] enCodeFormat = secretKey.getEncoded();
        // 5.根据字节数组生成AES密钥
        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
        // 6.根据指定算法AES自成密码器
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(mode, keySpec);
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception {

        String key = "123qwe";
        String text = "我是明文";
        String data = encrypt(text, key);
        System.out.println("明文是:" + text);
        System.out.println("加密后:" + data);
        System.out.println("解密后：" + decrypt(data, key));
    }
}
