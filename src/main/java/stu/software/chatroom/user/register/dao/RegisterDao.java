package stu.software.chatroom.user.register.dao;

import stu.software.chatroom.common.model.user.register.RegisterDto;

public interface RegisterDao {

	Integer selectMaxU_id();

	Integer selectUserCountByU_name(String u_name);

	void insertUser(RegisterDto registerDto);
}
