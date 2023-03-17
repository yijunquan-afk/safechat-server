package stu.software.chatroom.room;


public interface RoomService {
    /**
     * 更新用户头像
     * @param u_id 用户id
     * @param url
     */
    void uploadAvatar(Integer u_id, String url);
}
