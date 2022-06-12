package stu.software.chatroom.common.base;

//import stu.software.chatroom.common.*;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stu.software.chatroom.common.CommonService;
import stu.software.chatroom.common.Constants;
import stu.software.chatroom.common.Result;
import stu.software.chatroom.common.TokenUtils;
import stu.software.chatroom.common.Utils;

import java.io.IOException;


@WebFilter("/*")
public class SecurityFilter implements Filter {

    @Resource
    private CommonService commonService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getServletPath();

        if(path.startsWith("/user/login") | path.startsWith("/user/register") || path.startsWith("/socket.io")){//放行不需要验证token的接口
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        String clientToken = request.getHeader(Constants.HEADER_TOKEN);

        try {
            TokenUtils.verifyToken(clientToken,commonService);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.outJson(response, Result.fail(Result.ERR_CODE_UNLOGIN,e.getMessage()));
            return;
        }

        try{
            filterChain.doFilter(servletRequest,servletResponse);
        }catch (Exception e) {
            e.printStackTrace();
            Utils.outJson(response, Result.fail(Result.ERR_CODE_SYS,e.getMessage()));
        }
    }

    @Override
    public void destroy() {

    }
}
