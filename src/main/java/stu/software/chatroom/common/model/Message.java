package stu.software.chatroom.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class Message {
    String sender_name;
    String receiver_name;
    String avatar;
    String content;
    String type;
    String sign;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "HH:mm:ss")
    Date time;

    public Message() {

    }

    public Message(String send_name, String avatar, String content) {
        this.sender_name = send_name;
        this.avatar = avatar;
        this.content = content;
    }

    public Message(String send_name, String avatar, String content, Date time, String type) {
        this.sender_name = send_name;
        this.avatar = avatar;
        this.content = content;
        this.time = time;
        this.type = type;
    }

    public Message(String send_name, String avatar, String sign, String content, Date time, String type) {
        this.sender_name = send_name;
        this.avatar = avatar;
        this.content = content;
        this.time = time;
        this.type = type;
        this.sign = sign;
    }
}
