package stu.software.chatroom.common.model.user.register;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterDto {
	private Integer u_id;
	private String u_name;
	private String u_pwd;
	private String u_avatar;
}
