package kr.co.mythings.shakeit.api.config.interceptor;

import kr.co.mythings.shackit.core.db.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Slf4j
@Component
public class AuthenticatedInterceptor extends HandlerInterceptorAdapter {

//    @Autowired
//    private LoginDeviceMapper loginDeviceMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String currentUrl = request.getRequestURI();

        log.info("[" + currentUrl + "] ==========> START ==========>");

        {
            Enumeration<String> e = request.getHeaderNames();
            while (e.hasMoreElements()) {
                String headrName = e.nextElement();
                log.info(" hearders ==> " + headrName + " ==> " + request.getHeader(headrName));
            }
        }

        // Login Token Check
//        String authorization = request.getHeader("Authorization");
//        if (authorization == null) {
//            throw new UnAuthorizedException(ApiStatusCode.INVALID_TOKEN, "Authorization is null");
//        }
//        String token = authorization.split(" ")[1];
//
//        LoginDeviceEntity loginDeviceEntity = loginDeviceMapper.findByLoginToken(token);
//        if (loginDeviceEntity == null) {
//            throw new UnAuthorizedException(ApiStatusCode.INVALID_TOKEN, "login token is not valid");
//        }
//
//        Integer userIdx = loginDeviceEntity.getUserIdx();
//        UserEntity userEntity = userMapper.findByUserIdx(userIdx);
//        if (userEntity == null ) { //|| JWTUtil.verifyLoginToken(loginToken, userEntity.getUserId(), loginDeviceEntity.getLoginDeviceUuid()) == false) {
//            throw new UnAuthorizedException(ApiStatusCode.INVALID_TOKEN, "login token is not valid");
//        }
//
//        log.info("[AuthenticatedInterceptor] User Login Token Check - userIdx: {}", userIdx);
//
//        request.setAttribute("requestUserIdx", userEntity.getUserIdx());
//        request.setAttribute("requestUserEntity", userEntity);
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable ModelAndView modelAndView
    ) {
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable Exception ex
    ) {
        String curUrl = request.getRequestURI();
        log.info(" <========== END <========== [" + curUrl + "]");
    }
}
