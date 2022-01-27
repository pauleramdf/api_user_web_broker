package user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import user.model.User;
import user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
public class Interceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userName = request.getUserPrincipal().getName();

        Optional<User> u = userService.findByName(userName);
        if(u.isEmpty()){
            userService.save(new User(userName, "lula 2022", Double.valueOf(100000)));
        }

        String token = request.getHeader("Authorization");
        log.info(request.getUserPrincipal().getName());
        log.info(token);

        return true;
    }


}
