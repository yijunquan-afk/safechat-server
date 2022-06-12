package stu.software.chatroom.common;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common")
public class CommonAPI {
    @Resource
    private CommonService commonService;
}
