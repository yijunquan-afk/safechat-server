package stu.software.chatroom.room;

/**
 * @author yjq
 * @version 1.0
 * @date 2022/5/12 15:13
 */
public interface RoomService {
    /**
     * 更新用户头像
     * @param u_id 用户id
     * @param url
     */
    void uploadAvatar(Integer u_id, String url);
}
