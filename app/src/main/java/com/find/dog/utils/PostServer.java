package com.find.dog.utils;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ZhangV on 2017/7/23.
 */

public class PostServer {

    /**
     * post的方式请求
     *
     * @return 返回null 登录异常
     */
    public static void loginByPost(Map<String, String> parameters) {
        // 请求的地址
        String spec = "http://zhaogou.applinzi.com/getinfo_reward.php";
        int size = 0;
        String data = "";
        try {
            // 根据地址创建URL对象
            URL url = new URL(spec);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("POST");
            // 设置请求的超时时间
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            if (parameters != null) {
                Iterator<Map.Entry<String, String>> iter = parameters.entrySet()
                        .iterator();
                while (iter.hasNext()) {
                    size++;
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    if (key != null && value != null) {
                        String normal = key+ "=" + URLEncoder.encode(value, "UTF-8");
                        if(size == parameters.size()){
                            data += normal;
                        }else{
                            data += normal+"&";
                        }

                    }
                }
            }
            // 传递的数据


            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //setDoInput的默认值就是true
            //获取输出流
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            if (urlConnection.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    baos.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                baos.close();
                // 返回字符串
                final String result = new String(baos.toByteArray());
                Log.e("H", "-----result-------" + result);

            } else {
                System.out.println("链接失败.........");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
