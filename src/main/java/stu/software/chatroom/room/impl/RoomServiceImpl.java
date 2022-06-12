package stu.software.chatroom.room.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stu.software.chatroom.room.RoomService;
import stu.software.chatroom.room.dao.RoomDao;

import javax.annotation.Resource;

/**
 * @author yjq
 * @version 1.0
 * @date 2022/5/12 15:12
 */
@Service
@Transactional
public class RoomServiceImpl implements RoomService {
    @Resource
    private RoomDao roomDao;
    @Override
    public void uploadAvatar(Integer u_id, String url) {
        roomDao.uploadAvatar(u_id,url);
    }
}
