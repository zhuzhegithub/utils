package com.space.utils.encrypt;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import java.io.IOException;

public class Base64Util {

    /**
     * 加密 
     * 加密byte[]类型，密文为字符串 
     *
     * @param b
     * @return
     */
    public static String encode(byte[] b) {
        return new String(new Base64().encode(b));
    }

    /**
     * 解密 
     * 将字符串解密为String类型 
     *
     * @param source
     * @return
     */
    public static String decode(String source) {
        return new String(new Base64().decode(source.getBytes()));
    }

    /**
     * 解密 
     * 将字符串解密为String类型 
     *
     * @param s
     * @param charSet 字符编码
     * @return
     */
    public static String getFromBase64(String s, String charSet) throws IOException {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            b = decoder.decodeBuffer(s);
            result = new String(b, charSet);
        }
        return result;
    }
}
