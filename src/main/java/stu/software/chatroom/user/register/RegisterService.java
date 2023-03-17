package stu.software.chatroom.user.register;

import stu.software.chatroom.common.model.user.register.RegisterDto;

public interface RegisterService {
	
	// 获取新的用户 id
	Integer getNewU_id();
	
	// 查询用户名数量
	Integer getUserNameCount(RegisterDto registerDto);

	// 添加新用户
	void addNewUser(RegisterDto registerDto);
}
