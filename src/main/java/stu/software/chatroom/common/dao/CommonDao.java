package stu.software.chatroom.common.dao;

import stu.software.chatroom.common.model.user.User;

public interface CommonDao {
    User findUserById(String u_id);

	String selectU_avatarByU_name(String u_name);
}
