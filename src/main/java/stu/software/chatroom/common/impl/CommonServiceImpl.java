package stu.software.chatroom.common.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import stu.software.chatroom.common.CommonService;
import stu.software.chatroom.common.dao.CommonDao;
import stu.software.chatroom.common.model.user.User;

@Service
@Transactional
public class CommonServiceImpl implements CommonService {
	@Resource
	private CommonDao commonDao;
	
	@Override
	public User getUserById(String userId) {
		return commonDao.findUserById(userId);
	}

	@Override
	public String getU_avatarByU_name(String u_name) {
		return commonDao.selectU_avatarByU_name(u_name);
	}

}
