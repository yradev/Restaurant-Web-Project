package source.restaurant_web_project.intercaptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DeleteCategoryInterceptor implements HandlerInterceptor {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info(String.format("%s deleted category!",request.getRemoteUser()));
    }
}
