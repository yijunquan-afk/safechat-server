package com.southwind.controller;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Enumeration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class test {
    @GetMapping("/")
    public void test(@RequestHeader("Token") String token) throws Exception {
        //以 PKCS12 规格，创建 KeyStore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //载入 jks 和该 jks 的密码 到 KeyStore 内
        keyStore.load(new FileInputStream(new ClassPathResource("keys-and-certs/yjq.keystore").getFile()), "123456".toCharArray());

        // 要获取 key，需要提供 KeyStore 的别名 和该 KeyStore 的密码
        // 获取 keyStore 内所有别名 alias
        Enumeration<String> aliases = keyStore.aliases();
        String alias = null;
        alias = aliases.nextElement();

        System.out.println("jks文件别名是：" + alias);
        char[] keyPassword = "rOXoL5KtJdRV0GQv".toCharArray();

        String msg = "RaviJun.东风广场.sig";
        String msgTwo = "Jeffrey.东风广场.sig";
        //
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPassword);
        System.out.println("===============privateKey==================\n" + new String(Base64.getEncoder().encode(privateKey.getEncoded())));
        Certificate certificate = keyStore.getCertificate(alias);
        System.out.println("===============certificate=================\n" + new String(Base64.getEncoder().encode(certificate.getEncoded())));
        PublicKey publicKey = certificate.getPublicKey();
        System.out.println("==============publicKey===============\n" + new String(Base64.getEncoder().encode(publicKey.getEncoded())));

        //私钥签名
        byte[] signOne = sign(msg, privateKey) ;
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>签名后>>>>>>>>>>>>\n" + new String(Base64.getEncoder().encode(signOne)));

        //公钥验签
        boolean verifySign = verify(msg,signOne,publicKey);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>验签结果>>>>>>>>>>>>>>>>\n" + verifySign);

    }
    //私钥签名
    public static byte[] sign(String content, PrivateKey priKey) throws Exception {
        //这里可以从证书中解析出签名算法名称
        //Signature signature = Signature.getInstance(getSigAlgName(pubCert));
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(priKey);
        signature.update(content.getBytes());
        return signature.sign();
    }
    //公钥验签
    public static boolean verify(String content, byte[] sign, PublicKey pubKey) throws Exception {
        //这里可以从证书中解析出签名算法名称
        //Signature signature = Signature.getInstance(getSigAlgName(priCert));
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initVerify(pubKey);
        signature.update(content.getBytes());
        return signature.verify(sign);
    }
}