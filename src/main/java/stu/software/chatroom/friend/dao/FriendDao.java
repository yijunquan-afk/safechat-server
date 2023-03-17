package stu.software.chatroom.friend.dao;

import stu.software.chatroom.common.model.Friend;

import java.util.List;

/**
 * @author yjq
 * @version 1.0
 * @date 2022/6/8 22:42
 */
public interface FriendDao {
    List<Friend> getMyFriends(Integer u_id);
}
