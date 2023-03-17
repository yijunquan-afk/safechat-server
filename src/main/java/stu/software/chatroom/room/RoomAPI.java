package stu.software.chatroom.room;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import stu.software.chatroom.common.CommonService;
import stu.software.chatroom.common.CurrentUser;
import stu.software.chatroom.common.Result;
import stu.software.chatroom.common.TokenUtils;

import javax.annotation.Resource;


@Slf4j
@RestController
@RequestMapping("/room")
public class RoomAPI {
    @Resource
    private CommonService commonService;
    @Resource
    private RoomService roomService;

    /**
     * 上传头像
     * @param url
     * @param token
     * @return
     */
    @PostMapping("/avatar")
    public Result uploadAvatar(@RequestParam("url") String url, @RequestHeader("Token") String token){
        Integer u_id = TokenUtils.getUserInfo(token, commonService).getUserId();
        roomService.uploadAvatar(u_id,url);
        return Result.success("上传头像成功");
    }

    /**
     * 退出系统
     * @param token
     * @return
     */
    @DeleteMapping("/exit")
    public Result exit(@RequestHeader("Token") String token){
        //在服务端清除缓存的token
        TokenUtils.removeToken(token);
        return Result.success("退出登录成功");
    }
}
