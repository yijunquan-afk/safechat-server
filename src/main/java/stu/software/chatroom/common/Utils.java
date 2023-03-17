package stu.software.chatroom.common;

import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.format.annotation.DateTimeFormat;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class Utils {

    public static boolean dirExists(String dir) {
        File file = new File(dir);
        log.info(dir);
        log.info("" + file.exists());
        return file.exists() && file.isDirectory();
    }

    public static boolean createDir(String dir) {
        File file = new File(dir);
        return file.mkdirs();
    }

    /**
     * 生成图片随机名称
     * 时间+UUID生成的随机字符串
     */
    public static String imgNameGenerator() {
        // 图片的随机名称，上传时间+UUID随机字符串
        String uuid = UUID.randomUUID().toString();
        String now = getDateString();
        return now + "_" + uuid;
    }

    /**
     * 将一个Java对象转换为json串，并向浏览器（客户端）输出
     *
     * @param resp 响应对象
     * @param obj  需要转化为json串的java对象
     * @throws IOException
     */
    public static void outJson(HttpServletResponse resp, Object obj) throws IOException {
        //json格式的媒体标准：application/json
        resp.setContentType("application/json;charset=UTF-8");//设置字符编码
        PrintWriter out = resp.getWriter();//获取向客户端发送字符信息流对象
        // 将list集合对象转化为json格式字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(obj);
        out.print(jsonString);

        out.flush();
        out.close();
    }

    /**
     * 获取并返回当前日期，返回一个字符串
     * 如：20210607
     */
    public static String getDateString() {
        //Calendar calender = Calendar.getInstance();
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH) + 1;
        String mon = month < 10 ? ("0" + month) : ("" + month);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        String d = day < 10 ? ("0" + day) : ("" + day);
        return Integer.toString(year) + mon + d;
    }

    /**
     * 由帖子id生成链接
     *
     * @param pid 帖子id
     * @return 相应链接
     */
    public static String generateLinkByPid(String pid) {
        return "http://localhost:3000/forum/detail?p_id=" + pid;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public static Date getNow() {
        Calendar cal = Calendar.getInstance();
        try {
            // 设置最后修改时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.parse(dateFormat.format(cal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Integer generateRandomInteger(Integer max) {
        int value = (int) (Math.random() * max);
        return Integer.parseInt(String.valueOf(value));
    }


    /**
     * 返回0~max之间的整数（不含max）列表
     */
    public static List<Integer> generateRandomIntegerList(Integer max, Integer n) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(generateRandomInteger(max));
        }
        return result;
    }

    // 获取均匀列表
    public static List<Integer> generateEvenIntegerList(int size, Integer count) {
        List<Integer> list = new ArrayList<>();
        int interval = size / (count + 3);
        for (int i = 0, k = interval; i < count; i++) {
            if (k % size > 0)
                list.add(k % size);
            else
                list.add(size / 2);
            k += interval;
        }
        return list;
    }

    /**
     * 获取 IP 地址
     */
    public static String getIpByClient(SocketIOClient client) {
        String addr = client.getRemoteAddress().toString();
        String clientIP = addr.substring(1, addr.indexOf(":"));
        return clientIP;
    }

    /***
     * 利用Apache的工具类实现SHA-256加密
     * @return str 加密后的报文
     */
    public static String getSHA256Str(String str) {
        MessageDigest messageDigest;
        String encodeSir = str;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            encodeSir = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeSir;
    }

    /**
     * 通过该方法将密码加密
     */
    public static String encodePwd(String u_pwd) {
        // 密码通过此方法解密并再加密
        return getSHA256Str(u_pwd);
    }

    /**
     * 16进制的字符串表示转成字节数组
     *
     * @param hexString 16进制格式的字符串
     * @return 转换后的字节数组
     **/

    public static byte[] toByteArray(String hexString) {

        hexString = hexString.toLowerCase();

        final byte[] byteArray = new byte[hexString.length() / 2];

        int k = 0;

        for (int i = 0; i < byteArray.length; i++) {// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先

            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);

            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);

            byteArray[i] = (byte) (high << 4 | low);

            k += 2;

        }

        return byteArray;

    }

    /**
     * 字节数组转成16进制表示格式的字符串
     *
     * @param byteArray 需要转换的字节数组
     * @return 16进制表示格式的字符串
     **/

    public static String toHexString(byte[] byteArray) {

        String str = null;

        if (byteArray != null && byteArray.length > 0) {

            StringBuffer stringBuffer = new StringBuffer(byteArray.length);

            for (byte byteChar : byteArray) {

                stringBuffer.append(String.format("%02X", byteChar));

            }

            str = stringBuffer.toString();

        }

        return str;

    }

}
