package stu.software.chatroom.websocket.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import stu.software.chatroom.common.*;
import stu.software.chatroom.common.model.Message;
import stu.software.chatroom.websocket.SocketIOService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SocketIOServiceImpl implements SocketIOService {

    // 存放已连接的客户端
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();
    // 存放用户的公钥
    private static Map<String, String> publicKeyStringMap = new ConcurrentHashMap<>();
    // 存放用户的公钥
    private static Map<String, PublicKey> publicKeyMap = new ConcurrentHashMap<>();
    //存访AES的密钥
    private static String AesKey = "";


    @Autowired
    private SocketIOServer socketIOServer;

    @Resource
    private CommonService commonService;

    /**
     * Spring IoC 容器创建之后, 在加载 SocketIOServiceImpl Bean 之后启动
     */
    @PostConstruct
    private void autoStartup() {
        start();
    }

    /**
     * Spring IoC 容器在销毁 SocketIOServiceImpl Bean 之前关闭
     * 避免重启项目服务端口占用问题
     */
    @PreDestroy
    private void autoStop() {
        stop();
    }

    @Override
    public void start() {
// 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            String u_name = getParamsByClient(client, "u_name");
            clientMap.put(u_name, client);
            log.info("新建客户端连接: " + Utils.getIpByClient(client) + ", 用户名 :" + u_name);
            try {
                RSAUtils.genKeyPair(u_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String publicKey = RSAUtils.getKeyPair().get("PU");
            log.info("密钥获取成功: " + publicKey);
            publicKeyStringMap.put(u_name, publicKey);
            publicKeyMap.put(u_name, RSAUtils.getPublicKey());
            log.info("目前公钥库中有: " + publicKeyMap.size() + " 个公钥");
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String u_name = getParamsByClient(client, "u_name");
            clientMap.remove(u_name);
            log.info("用户 " + u_name + " 已经断开连接");
        });


        // 监听客户端发送消息
        socketIOServer.addEventListener(Constants.EVENT_MESSAGE_TO_SERVER, String.class, (client, data, ackSender) -> {
            String sender_name = getParamsByClient(client, "u_name");
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(data, Message.class);
            String receiver_name = message.getReceiver_name();

            if (message.getType().equals(Constants.MASTER_MESSAGE)) {
                //使用公钥加密传送会话密钥
                if (AesKey.equals("")) {
                    log.info("用户" + sender_name + "生成会话密钥");
                    AesKey = AESUtils.genAesSecret();
                    message.setContent(AesKey);
                    log.info("用户" + sender_name + "使用用户" + sender_name + "的私钥对会话密钥进行签名");
                    String sign = new String(RSAUtils.sign(message.getContent(), RSAUtils.getPrivateKey()), "ISO-8859-1");
                    message.setSign(sign);
                    String result = RSAUtils.encrypt(message.getContent(), publicKeyStringMap.get(receiver_name));
                    log.info("使用用户" + receiver_name + "的公钥对会话密钥进行加密：" + result);
                    message.setContent(result);
                    sendMessageToFriend(message.getReceiver_name(), message);
                } else {
                    return;
                }
            } else {
                //使用会话密钥发送消息
                byte[] bytes = AESUtils.encrypt(message.getContent().getBytes(), AesKey.getBytes());
                String encrypt = new String(bytes, "ISO-8859-1");
                log.info("用户" + sender_name + "使用会话密钥加密消息");
                message.setContent(encrypt);
                sendMessageToFriend(message.getReceiver_name(), message);
            }

        });
        //
        //GBK,  GB2312，UTF-8等一些编码方式为多字节或者可变长编码，原来的字节数组就被改变了，再转回原来的byte[]数组就会发生错误了。
        //ISO-8859-1通常叫做Latin-1，Latin-1包括了书写所有西方欧洲语言不可缺少的附加字符，其中 0~127的字符与ASCII码相同，
        // 它是单字节的编码方式，在来回切换时不会出现错误。

        // 监听客户端接收消息
        socketIOServer.addEventListener("receive_triger", String.class, (client, data, ackSender) -> {

            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(data, Message.class);
            String sender_name = message.getSender_name();
            String receiver_name = message.getReceiver_name();
            if (message.getType().equals(Constants.MASTER_MESSAGE)) {
                log.info("收到来自" + sender_name + "发送给" + message.getReceiver_name() + "的消息: " + message.getContent());
                String result = RSAUtils.decrypt(message.getContent(), RSAUtils.getKeyPair().get("PR"));
                log.info("用户" + receiver_name + "使用用户" + receiver_name + "的私钥对消息进行解密：");
                message.setContent(result);
                log.info("用户" + receiver_name + "使用用户" + sender_name + "的公钥对消息进行验证签名");
                Boolean sign = (RSAUtils.verify(message.getContent(), message.getSign().getBytes("ISO-8859-1"), publicKeyMap.get(sender_name)));
                if (sign) {
                    log.info("签名验证成功！身份无误");
                } else {
                    throw new Exception("签名错误！");
                }

                receiveMessageFromFriend(message.getReceiver_name(), message);
            } else {
                log.info("收到来自" + sender_name + "发送给" + message.getReceiver_name() + "的消息: " + message.getContent());
                String text = new String(AESUtils.decrypt(message.getContent().getBytes("ISO-8859-1"), AesKey.getBytes()), "UTF-8");
                log.info("用户" + receiver_name + "使用会话密钥进行解密");
                message.setContent(text);
                receiveMessageFromFriend(message.getReceiver_name(), message);
            }
        });
        // 启动服务
        socketIOServer.start();
    }


    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    /**
     * 发送信息给指定用户
     *
     * @param receiver_name
     * @param msg
     */
    private static void sendMessageToFriend(String receiver_name, Message msg) {
        SocketIOClient receiver_client = clientMap.get(receiver_name);
        if (receiver_client != null) {
            receiver_client.sendEvent(Constants.EVENT_MESSAGE_SEND, msg);
        }
    }

    /**
     * 接收消息
     *
     * @param receiver_name
     * @param msg
     */
    private static void receiveMessageFromFriend(String receiver_name, Message msg) {
        SocketIOClient receiver_client = clientMap.get(receiver_name);
        if (receiver_client != null) {
            receiver_client.sendEvent(Constants.EVENT_MESSAGE_RECEIVE, msg);
        }
    }

    /**
     * 获取客户端 url 中的参数
     *
     * @param client: 客户端
     * @return: 获取的参数
     */
    private String getParamsByClient(SocketIOClient client, String key) {
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();

        System.out.println(params.toString());

        List<String> userIdList = params.get(key);
        if (!CollectionUtils.isEmpty(userIdList)) {
            return userIdList.get(0);
        }
        return null;
    }

}
