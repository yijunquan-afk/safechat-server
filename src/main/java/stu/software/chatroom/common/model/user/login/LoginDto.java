package stu.software.chatroom.common.model.user.login;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginDto {
	private Integer u_id;
	private String u_name;
	private String u_pwd;
}
