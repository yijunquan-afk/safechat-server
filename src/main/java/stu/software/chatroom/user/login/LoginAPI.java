package stu.software.chatroom.user.login;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import stu.software.chatroom.common.CommonService;
import stu.software.chatroom.common.RSAUtils;
import stu.software.chatroom.common.Result;
import stu.software.chatroom.common.TokenUtils;
import stu.software.chatroom.common.model.user.User;
import stu.software.chatroom.common.model.user.login.LoginDto;

@RestController
@RequestMapping("/user/login")
public class LoginAPI {
	@Resource
	private CommonService commonService;
	@Resource
	private LoginService loginService;
	
	@PostMapping("")
	public Result login(@RequestBody LoginDto dto) {
		Integer cnt = loginService.getUserCount(dto);
		if (cnt == 1) {
			Integer u_id = loginService.getU_idByU_name(dto.getU_name());
			String token = TokenUtils.loginSign(Integer.toString(u_id), dto.getU_pwd());
			return Result.success((Object)token);
		} else {
			return Result.fail(Result.ERR_CODE_BUSINESS, "帐号或密码错误");
		}
	}
	
	@GetMapping("/getUserInfo")
	public Result getUserInfo(@RequestHeader("Token") String token) throws Exception {

		Integer u_id = TokenUtils.getUserInfo(token, commonService).getUserId();
		String u_name = TokenUtils.getUserInfo(token, commonService).getUserName();
		String u_avatar = TokenUtils.getUserInfo(token, commonService).getAvatar();
		RSAUtils.genKeyPair("u_name");
		User user = new User();
		user.setU_id(u_id);
		user.setU_name(u_name);
		user.setU_avatar(u_avatar);
		
		return Result.success(user);
	}
}
