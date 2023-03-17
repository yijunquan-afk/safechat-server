package stu.software.chatroom.user.login.dao;

import stu.software.chatroom.common.model.user.login.LoginDto;

public interface LoginDao {

	Integer selectUserCount(LoginDto dto);

	Integer selectU_idByU_name(String u_name);
}
