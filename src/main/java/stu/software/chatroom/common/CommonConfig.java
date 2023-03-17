package stu.software.chatroom.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@MapperScan("stu.software.chatroom.common.dao")
@ServletComponentScan("stu.software.chatroom.common.base")
public class CommonConfig {

    private static ApplicationContext applicationContext;
    private static Environment env;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        CommonConfig.applicationContext = applicationContext;
        CommonConfig.env = CommonConfig.applicationContext.getEnvironment();
    }


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Environment getEnv() {
        return env;
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods(RequestMethod.GET.name(), RequestMethod.POST.name(), RequestMethod.PUT.name(), RequestMethod.DELETE.name());
            }
        };
    }

//    @Resource
//    @Bean
//    public CommonService commonService(CommonDao commonDao){
//        return new CommonService(){
//            @Override
//            public User getUserById(String userId) {
//                return commonDao.findUserById(userId);
//            }
//        };
//    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


}
