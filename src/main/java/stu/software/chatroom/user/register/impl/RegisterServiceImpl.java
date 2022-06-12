package stu.software.chatroom.user.register.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import stu.software.chatroom.common.model.user.register.RegisterDto;
import stu.software.chatroom.user.register.RegisterService;
import stu.software.chatroom.user.register.dao.RegisterDao;

@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {
	@Resource
	private RegisterDao registerDao;
	
	@Override
	public Integer getNewU_id() {
		return registerDao.selectMaxU_id() + 1;
	}

	@Override
	public Integer getUserNameCount(RegisterDto registerDto) {
		return registerDao.selectUserCountByU_name(registerDto.getU_name());
	}

	@Override
	public void addNewUser(RegisterDto registerDto) {
		registerDao.insertUser(registerDto);
	}

}
