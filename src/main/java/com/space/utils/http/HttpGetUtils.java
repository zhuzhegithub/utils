package com.space.utils.http;

/**
 * http get请求工具类
 *
 * @author zhuzhe
 * @date 2018/4/19 16:32
 * @email zhe.zhu1@outlook.com
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetUtils {

    /**
     * 获取页面内容
     *
     * @param templatePath
     * @return
     * @throws IOException
     */
    public static String getPageContents(String templatePath) throws IOException {

        /**
         * 1.创建URL对象
         * 2.创建Http链接
         */
        URL url = new URL(templatePath);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        /**
         * 3.设置请求超时和读取超时时间限制   xxx毫秒
         */
        httpURLConnection.setConnectTimeout(60000);
        httpURLConnection.setReadTimeout(10000);
        /**
         * 3.设置请求方式
         * 4.设施请求内容类型
         */
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        /**
         * 5.设置请求参数
         * 6.使用输出流发送参数
         */
        //String content="username:";
        //OutputStream outputStream = httpURLConnection.getOutputStream();
        //outputStream.write(content.getBytes());
        /**
         * 7.使用输入流接受数据
         */
        InputStream inputStream = httpURLConnection.getInputStream();
        //此处可以用StringBuffer等接收
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while (true) {
            len = inputStream.read(b);
            if (len == -1) {
                break;
            }
            byteArrayOutputStream.write(b, 0, len);
        }
        return byteArrayOutputStream.toString();
    }
}