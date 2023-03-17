package stu.software.chatroom.common.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    private Integer u_id;
    private String u_pwd;
    private String u_name;
    private String u_avatar;
    
    public User(String u_name, String u_avatar) {
    	this.u_name = u_name;
    	this.u_avatar = u_avatar;
    }
}

