package com.space.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 *     RSA 公钥加密算法是1977年由Ron Rivest、Adi Shamirh和LenAdleman在（美国麻省理工学院）开发的。
 *     RSA取名来自开发他们三者的名字。RSA是目前最有影响力的公钥加密算法，它能够抵抗到目前为止已知的
 *     所有密码攻击，已被ISO推荐为公钥数据加密标准。
 * </p>
 * <p>
 *     加密解密：公钥加密的密文只有通过私钥才可以解密，同样，私钥加密的密文只有通过公钥才可以解密；
 *     签名：签名不同于加解密，只有私钥可以用来签名，公钥用来验证签名，签名无法被解密成明文，
 *          公钥仅是验证签名的正确性。{@link RSAUtil#verify(byte[], java.lang.String, java.lang.String)}
 * </p>
 */
public class RSAUtil {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /** */
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = new Base64().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return new String(new Base64().encode(signature.sign()));
    }

    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = new Base64().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(new Base64().decode(sign));
    }

    /**
     * 私钥解密
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = new Base64().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        byte[] resData = dealData(data, cipher, MAX_DECRYPT_BLOCK);
        return resData;
    }

    /**
     * 公钥解密
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = new Base64().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        byte[] resData = dealData(data, cipher, MAX_DECRYPT_BLOCK);
        return resData;
    }

    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = new Base64().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        byte[] resData = dealData(data, cipher, MAX_ENCRYPT_BLOCK);
        return resData;
    }

    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = new Base64().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        byte[] resData = dealData(data, cipher, MAX_ENCRYPT_BLOCK);
        return resData;
    }

    private static byte[] dealData(byte[] data, Cipher cipher, int size)
            throws IllegalBlockSizeException, BadPaddingException, IOException {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > size) {
                cache = cipher.doFinal(data, offSet, size);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * size;
        }
        byte[] resData = out.toByteArray();
        out.close();
        return resData;
    }

    /**************************************  生成公钥和私钥  ******************************************/

    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return new String(new Base64().encode(key.getEncoded()));
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return new String(new Base64().encode(key.getEncoded()));
    }

    /**************  测试  **************/
    public static void main(String[] args) throws Exception {

//        Map<String, Object> map = RSAUtil.genKeyPair();
//        String privateKey = RsaUtils.getPrivateKey(map);
//        String publicKey = RsaUtils.getPublicKey(map);
//
//        System.out.println(privateKey);
//        System.out.println(publicKey);

        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJOPwbOC316RXOENrrTPlgh7CIh/VjGSFLx6X1fv/LGJ2wkcY9AaICNkwd9eWyNsPsi744M0KJz3fvIr74oRlLBUxZ2ewL0pPJMC2vCRCSMst5bhUcFNDZxFBk+mjVZhFxavRbY9mKt8Oxoq6unQlGo1/u4HIAU8Usm9/6G6imVHAgMBAAECgYAP4MwrEM4QUvv3i41dD2lJiFOmvGz98IYisdhIkh5SzE6xcEidP/JtXI4wBusvqJoo527X02j7WvZiALBJ3xJsrNMusWVvnikeXIXldp9zBrRg42A8DYKA+05XCmD9t5s7dNjlhaZ51iXtX6wJSY9oYKSEOIrgtEQyxuKZalarIQJBANrSckDMXWUqZWYXVFjv9EuceoTkb7pteZZDNpNb7pWvvizCwVtvCJdrZps9KjG8l+8hN5/mKoabhJmqiwaK6SMCQQCsod73lDXQ0o78l2UqtMkC0MeRrWAs40ag2Ms3yLUrD4eN0yuItxYqX8OfBPutoQIMUK9lFKLWEHZBUuZQrF+NAkAj7hQ2kZfwSZLWvZPq+pOJassvUPcLjHg/dQ/cCGdXisDWRrBLHCM0Ab2aVeO8wPPNnw2sIrXu3lBajwc+7sYzAkBgp1yj91pOKaWe7NDaoU+uAODLwmuHllRT+C2HdRtiGZbGpEXKjN+fJ/Dp1qtm4i+4X3BkQrKM8biUCGslHe19AkEA0afrlCsJ4Y6/iZ5SuEKxZLEgycFYJbapG282ceE5U22eJ8+rv9WdqIuloLzBIlJdOkLdmBblmNFe1kp0qfP/Jw==";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTj8Gzgt9ekVzhDa60z5YIewiIf1YxkhS8el9X7/yxidsJHGPQGiAjZMHfXlsjbD7Iu+ODNCic937yK++KEZSwVMWdnsC9KTyTAtrwkQkjLLeW4VHBTQ2cRQZPpo1WYRcWr0W2PZirfDsaKurp0JRqNf7uByAFPFLJvf+huoplRwIDAQAB";

        String ming = "我是明文";

        byte[] encrypt = RSAUtil.encryptByPrivateKey(ming.getBytes(), privateKey);
        byte[] encrypt2 = RSAUtil.encryptByPublicKey(ming.getBytes(), publicKey);
        System.out.println(new String(new Base64().encode(encrypt2)));
        byte[] bytes = RSAUtil.decryptByPrivateKey(encrypt2, privateKey);
        System.out.println(new String(bytes));

        String sign = RSAUtil.sign("数据".getBytes(), privateKey);
        boolean verify = RSAUtil.verify("数据".getBytes(), publicKey, sign);
        System.out.println(verify);

    }
}
