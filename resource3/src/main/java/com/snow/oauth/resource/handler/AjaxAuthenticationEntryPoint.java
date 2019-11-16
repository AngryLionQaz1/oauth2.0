package com.snow.oauth.resource.handler;


import com.snow.oauth.resource.common.HttpUtilsResultVO;
import com.snow.oauth.resource.common.ResponseVo;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未登录
 */
@Configuration
public class AjaxAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        //        Throwable cause = authException.getCause();  如果需要具体判断类型的话，从这里取类型
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            HttpUtilsResultVO.writerError(new ResponseVo(401, "未登录"), response);
//               response.getWriter().write(new ResponseVo(401, authException.getMessage()), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
