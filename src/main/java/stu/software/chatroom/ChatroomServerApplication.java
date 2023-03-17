package stu.software.chatroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"stu.software.chatroom.*.dao"})
@MapperScan({"stu.software.chatroom.*.*.dao"})
@MapperScan({"stu.software.chatroom.*.*.*.dao"})
@SpringBootApplication
public class ChatroomServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatroomServerApplication.class, args);
	}

}
