
package stu.software.chatroom.common;

public class CurrentUser {

    public static final String SESSION_ATTR_NAME="CurrentUser";//session属性名字

    /**
     * 常量
     */
    public static final String IMG_DIR = "D:/Documents/img"; // 图片存储位置
    public static final String DEFAULT_PASSWORD = "111111"; // 默认密码
    public static final String AVATAR_PREFIX = "AVATAR_"; // 头像文件前缀

    private Integer userId;
    private String userName;
    private String avatar;//头像地址


    public CurrentUser() {
    }

    public CurrentUser(Integer userId) {
        this.userId = userId;
    }

    public CurrentUser(Integer userId, String userName, String avatar) {
        this.userId = userId;
        this.userName = userName;
        this.avatar = avatar;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

