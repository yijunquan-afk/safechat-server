package stu.software.chatroom.user.login;

import stu.software.chatroom.common.model.user.login.LoginDto;

public interface LoginService {
	
	// 获取用户数量
	Integer getUserCount(LoginDto dto);

	// 获取用户的 u_id
	Integer getU_idByU_name(String u_name);
}
