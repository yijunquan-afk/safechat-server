package stu.software.chatroom.common;

import stu.software.chatroom.common.model.user.User;

public interface CommonService {
    User getUserById(String userId);

	String getU_avatarByU_name(String u_name);
}
