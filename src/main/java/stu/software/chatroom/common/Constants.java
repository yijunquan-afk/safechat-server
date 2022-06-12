package stu.software.chatroom.common;

public class Constants {

    public static final String HEADER_TOKEN = "Token";

    public static final String DEFAULT_AVATAR="https://note-image-1307786938.cos.ap-beijing.myqcloud.com/typora/logo.jpeg";

    //公钥加密消息
	public static final String MASTER_MESSAGE = "MASTER";
	//对称加密消息
	public static final String SESSION_MESSAGE = "SESSION";

    /**
	 * 自定义事件, 用于服务端与客户端通信
	 */
	public static final String EVENT_MESSAGE_TO_SERVER = "send_triger";
	public static final String CHAT_WITH_FRIEND = "chat";
    public static final String EVENT_ENTRY = "entry";
	public static final String EVENT_LEAVE = "leave";
	public static final String EVENT_MESSAGE_SEND = "send";
	public static final String EVENT_MESSAGE_RECEIVE = "receive";
	public static final String EVENT_ONLINEUSER_CHANGE = "onlineuser";
	public static final String EVENT_ONLINEUSER_LIST = "onlineUserList";
}
