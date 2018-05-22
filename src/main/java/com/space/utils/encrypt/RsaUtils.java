package com.space.utils.encrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 *  RSA加密解密 
 *  此工具类能使用指定的字符串，每次生成相同的公钥和私钥且在linux和windows密钥也相同；相同的原文和密钥生成的密文相同 
 *  @author zhuzhe
 *  @date 2018/5/3 13:05
 */
public final class RsaUtils {

    private static final String ALGORITHM_RSA = "RSA";
    private static final String ALGORITHM_SHA1PRNG = "SHA1PRNG";
    private static final int KEY_SIZE = 512;
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final int ENCRY_DATA_LENGTH = 48;
    private static final int DECODE_LENGTH = 88;

    /**
     * 用私钥解密，解密字符串，返回字符串 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static String decryptByPrivateKey(String data, String key) throws Exception {
        if (data.length() <= DECODE_LENGTH) {
            return new String(decryptByPrivateKey(Base64.getDecoder().decode(data.getBytes()), key));
        }
        String result = "";
        for (int i = 0 ; i < data.length()/DECODE_LENGTH ; i++) {
            String substring = data.substring(DECODE_LENGTH * i, DECODE_LENGTH * (i + 1));
            result = result + new String(decryptByPrivateKey(Base64.getDecoder().decode(substring.getBytes()), key));
        }
        return result;
    }

    /**
     * 用公钥加密，加密字符串，返回用base64加密后的字符串 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static String encryptByPublicKey(String data, String key) throws Exception {
        if (data.length() <= ENCRY_DATA_LENGTH) {
            return encryptByBytePublicKey(data.getBytes(), key);
        }
        String result = "";
        for ( int i = 0 ; i < data.length() / ENCRY_DATA_LENGTH ; i++  ) {
            String substring = data.substring(ENCRY_DATA_LENGTH * i, ENCRY_DATA_LENGTH * (i + 1));
            result = result + encryptByBytePublicKey(substring.getBytes(), key);
        }
        String lastString = data.substring(ENCRY_DATA_LENGTH * (data.length() / ENCRY_DATA_LENGTH), data.length());
        result = result + encryptByBytePublicKey(lastString.getBytes(), key);
        return result;
    }

    /**
     * 用公钥加密，加密byte数组，返回用base64加密后的字符串 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static String encryptByBytePublicKey(byte[] data, String key) throws Exception {
        return Base64.getEncoder().encodeToString(encryptByPublicKey(data, key));
    }

    /**
     * 用私钥解密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对私钥解密  
        byte[] keyBytes = Base64.getDecoder().decode(key);

        /*取得私钥*/
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        /*对数据解密*/
        //相同的原文、公钥能生成相同的密文。如果使用Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm())
        // 相同的原文、公钥生成的密文不同
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 用公钥加密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        //对公钥解密  
        byte[] keyBytes = Base64.getDecoder().decode(key);
        /*取得公钥*/
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        /*对数据加密*/
        //相同的原文、公钥能生成相同的密文。如果使用Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm())
        // 相同的原文、公钥生成的密文不同  
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     * @param keyMap
     * @return
     */
    private static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 取得公钥
     * @param keyMap
     * @return
     */
    private static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 初始化公钥和私钥
     * @param seed
     * @return
     * @throws Exception
     */
    private static Map<String, Object> initKey(String seed) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);

        //windows和linux默认不同，导致两个平台生成的公钥和私钥不同
        //如果使用SecureRandom random = new SecureRandom();
        SecureRandom random = SecureRandom.getInstance(ALGORITHM_SHA1PRNG);
        //使用种子则生成相同的公钥和私钥  
        random.setSeed(seed.getBytes());
        keyPairGen.initialize(KEY_SIZE, random);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 公钥  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 前台传密文后台解密
     * @param cipherText  密文
     * @param seed  种子
     * @return
     * @throws Exception
     */
    public static String decrypt(String cipherText,String seed) throws Exception {
        if (cipherText == null) {
            return null;
        }
        /**
         * 初始化密钥  
         */
        Map<String, Object> keyMap = RsaUtils.initKey(seed);
        String privateKey = RsaUtils.getPrivateKey(keyMap);
        String decodedStr = RsaUtils.decryptByPrivateKey(cipherText, privateKey);
        return decodedStr;
    }

    /**
     * 加密接口
     * @param plainText 原文
     * @param seed  种子
     * @return 密文
     * @throws Exception
     */
    public static String encrypt(String plainText,String seed) throws Exception {
        if (plainText == null) {
            return null;
        }
        Map<String, Object> keyMap = RsaUtils.initKey(seed);
        String publicKey = RsaUtils.getPublicKey(keyMap);
        String decodedStr = RsaUtils.encryptByPublicKey(plainText, publicKey);
        return decodedStr;
    }
}