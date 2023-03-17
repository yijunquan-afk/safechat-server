package stu.software.chatroom.friend;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stu.software.chatroom.common.CommonService;
import stu.software.chatroom.common.Result;
import stu.software.chatroom.common.TokenUtils;
import stu.software.chatroom.common.model.Friend;
import stu.software.chatroom.common.model.user.User;
import stu.software.chatroom.user.login.LoginService;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

/**
 * @author yjq
 * @version 1.0
 * @date 2022/6/8 22:35
 */
@RestController
@RequestMapping("/friend")
public class FriendAPI {

    @Resource
    private CommonService commonService;
    @Resource
    private FriendService friendService;

    @GetMapping("/get")
    public Result getMyFriends(@RequestHeader("Token") String token) {
        Integer u_id = TokenUtils.getUserInfo(token, commonService).getUserId();
        List<Friend> friendList = friendService.getMyFriends(u_id);
        return Result.success(friendList);
    }

}
