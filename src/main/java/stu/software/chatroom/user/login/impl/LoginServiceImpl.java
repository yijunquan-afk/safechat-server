package stu.software.chatroom.user.login.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import stu.software.chatroom.common.model.user.login.LoginDto;
import stu.software.chatroom.user.login.LoginService;
import stu.software.chatroom.user.login.dao.LoginDao;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Resource
	private LoginDao loginDao;
	
	@Override
	public Integer getUserCount(LoginDto dto) {
		return loginDao.selectUserCount(dto);
	}

	@Override
	public Integer getU_idByU_name(String u_name) {
		return loginDao.selectU_idByU_name(u_name);
	}

}
