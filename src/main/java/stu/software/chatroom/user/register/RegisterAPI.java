package stu.software.chatroom.user.register;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stu.software.chatroom.common.CommonService;
import stu.software.chatroom.common.Constants;
import stu.software.chatroom.common.Result;
import stu.software.chatroom.common.Utils;
import stu.software.chatroom.common.model.user.register.RegisterDto;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user/register")
public class RegisterAPI {
    @Resource
    private CommonService commonService;
    @Resource
    private RegisterService registerService;

    @PostMapping("")
    public Result register(@RequestBody RegisterDto registerDto) {
        Integer cnt = registerService.getUserNameCount(registerDto);
        if (cnt == 0) {
            // 该昵称可以使用
            // 获取新的 u_id
            Integer u_id = registerService.getNewU_id();
            registerDto.setU_id(u_id);
            String encode_pwd = Utils.encodePwd(registerDto.getU_pwd());
            registerDto.setU_pwd(encode_pwd);//加密密码
            registerDto.setU_avatar(Constants.DEFAULT_AVATAR);
            registerService.addNewUser(registerDto);
            return Result.success("注册成功, 您的 u_id 是 " + u_id, u_id);
        } else {
            // 该昵称无法使用
            return Result.fail(Result.ERR_CODE_BUSINESS, "该昵称已被占用!");
        }
    }
}
