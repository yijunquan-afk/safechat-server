package stu.software.chatroom.friend.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stu.software.chatroom.common.model.Friend;
import stu.software.chatroom.friend.FriendService;
import stu.software.chatroom.friend.dao.FriendDao;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yjq
 * @version 1.0
 * @date 2022/6/8 22:42
 */
@Service
@Transactional
public class FriendServiceImpl implements FriendService {
    @Resource
    FriendDao friendDao;
    @Override
    public List<Friend> getMyFriends(Integer u_id) {
        return friendDao.getMyFriends(u_id);
    }
}
