package stu.software.chatroom.common;

import org.springframework.core.io.ClassPathResource;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * @author yjq
 * @version 1.0
 * @date 2022/6/9 14:54
 */
public class RSAUtils {
    //密钥对字符串
    private static Map<String, String> keyPairString = new HashMap<String, String>();
    private static PrivateKey privateKey;
    private static PublicKey publicKey;
    //加密算法
    private static final String KEYALG = "RSA";
    private static final String SIGALG = "SHA1WithRSA";
    private static String path = "";

    public static Map<String, String> getKeyPair() {
        return keyPairString;
    }

    public static PrivateKey getPrivateKey() {
        return privateKey;
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }

    public static void genKeyPair(String name) throws Exception {
        //以 PKCS12 规格，创建 KeyStore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        path = "keys-and-certs/" + name + ".keystore";
        //载入 jks 和该 jks 的密码 到 KeyStore 内
        keyStore.load(new FileInputStream(new ClassPathResource("keys-and-certs/yjq.keystore").getFile()), "123456".toCharArray());

        // 要获取 key，需要提供 KeyStore 的别名 和该 KeyStore 的密码
        // 获取 keyStore 内所有别名 alias
        Enumeration<String> aliases = keyStore.aliases();
        String alias = null;
        alias = aliases.nextElement();
        char[] keyPassword = "123456".toCharArray();

        String msg = "建立会话";
        keyPairString.clear();

        privateKey = (PrivateKey) keyStore.getKey(alias, keyPassword);
        keyPairString.put("PR", new String(Base64.getEncoder().encode(privateKey.getEncoded())));

        Certificate certificate = keyStore.getCertificate(alias);
        publicKey = certificate.getPublicKey();
        keyPairString.put("PU", new String(Base64.getEncoder().encode(publicKey.getEncoded())));

////        //私钥签名
//        byte[] signOne = sign(msg, privateKey);
//        System.out.println();
//        //公钥验签
//        boolean verifySign = verify(msg, signOne, publicKey);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>验签结果>>>>>>>>>>>>>>>>: " + verifySign);
//        //公钥加密
//        String jiami = encrypt(msg, keyPairString.get("PU"));
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>加密后>>>>>>>>>>>>\n" + jiami);
//
//        //私钥解密
//        String jiemi = decrypt(jiami, keyPairString.get("PR"));
////        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>解密>>>>>>>>>>>>>>>>\n" + jiemi);

    }

    /**
     * 私钥签名
     * @param content 字符串
     * @param priKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] sign(String content, PrivateKey priKey) throws Exception {
        Signature signature = Signature.getInstance(SIGALG);
        signature.initSign(priKey);
        signature.update(content.getBytes());
        return signature.sign();
    }

    /**
     * 公钥验证签名
     * @param content 字符串
     * @param sign 签名
     * @param pubKey 公钥
     * @return 身份是否真实
     * @throws Exception
     */
    public static boolean verify(String content, byte[] sign, PublicKey pubKey) throws Exception {

        Signature signature = Signature.getInstance(SIGALG);
        signature.initVerify(pubKey);
        signature.update(content.getBytes());
        return signature.verify(sign);
    }

    /**
     * RSA公钥加密
     *
     * @param content       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String content, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.getMimeDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(KEYALG).generatePublic(new X509EncodedKeySpec(decoded));
//        System.out.println(pubKey.getAlgorithm());
        //RSA加密
        Cipher cipher = Cipher.getInstance(KEYALG);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param content        加密字符串
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String content, String privateKey) throws Exception {

        //64位解码加密后的字符串
        byte[] inputByte = Base64.getMimeDecoder().decode(content);
        //        //base64编码的私钥
        byte[] decoded = Base64.getMimeDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    /**
     * 导出证书、公钥、私钥
     */
    public static void exportKeysAndCertsFromKeyStore() throws Exception {

        //以 PKCS12 规格，创建 KeyStore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //载入 jks 和该 jks 的密码 到 KeyStore 内
        keyStore.load(new FileInputStream(new ClassPathResource("keys-and-certs/yjq.keystore").getFile()), "123456".toCharArray());

        // 要获取 key，需要提供 KeyStore 的别名 和该 KeyStore 的密码
        // 获取 keyStore 内所有别名 alias
        Enumeration<String> aliases = keyStore.aliases();
        String alias = null;

        //文档写入格式换行+Base64
        final String LINE_SEPARATOR = System.getProperty("line.separator");
        final Base64.Encoder encoder = Base64.getMimeEncoder(64, LINE_SEPARATOR.getBytes());

        while (aliases.hasMoreElements()) {
            alias = aliases.nextElement();
            System.out.println("jks文件别名是：" + alias);
            char[] keyPassword = "123456".toCharArray();

            //私钥
            String keyContent = "-----BEGIN PRIVATE KEY-----" + LINE_SEPARATOR + new String(encoder.encode(keyStore.getKey(alias, keyPassword).getEncoded())) + LINE_SEPARATOR + "-----END PRIVATE KEY-----";
            keyPairString.put("PR", new String(encoder.encode(keyStore.getKey(alias, keyPassword).getEncoded())));
            writeKeyOrCertToFile("./src/main/resources/keys-and-certs/" + alias + ".key", keyContent);

            Certificate certificate = keyStore.getCertificate(alias);

            String certificateContent = "-----BEGIN CERTIFICATE-----" + LINE_SEPARATOR + new String(encoder.encode(certificate.getEncoded())) + LINE_SEPARATOR + "-----END CERTIFICATE-----";
            //证书
            writeKeyOrCertToFile("./src/main/resources/keys-and-certs/" + alias + ".cer", certificateContent);

            PublicKey publicKey = certificate.getPublicKey();
            String cerContent = "-----BEGIN PUBLIC KEY-----" + LINE_SEPARATOR + new String(encoder.encode(publicKey.getEncoded())) + LINE_SEPARATOR + "-----END PUBLIC KEY-----";
            keyPairString.put("PU", new String(encoder.encode(publicKey.getEncoded())));
            //公钥
            writeKeyOrCertToFile("./src/main/resources/keys-and-certs/" + alias + ".pub", cerContent);

        }
//        System.out.println(keyPairString.get("PU"));
//        System.out.println(keyPairString.get("PR"));

    }

    /**
     * 创建文档流
     */
    public static void writeKeyOrCertToFile(String filePathAndName, String fileContent) throws IOException {

        FileOutputStream fos = new FileOutputStream(filePathAndName);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(fileContent);
        bw.close();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        byte[] byte1 = new byte[] { 10,34,56,78,90,-2,-5 };

        String string = new String(byte1,"ISO-8859-1");

        byte[] result = string.getBytes("ISO-8859-1");

        System.out.println("转换前："+ Arrays.toString(byte1));
        System.out.println("转换后："+ Arrays.toString(result));

    }

}
