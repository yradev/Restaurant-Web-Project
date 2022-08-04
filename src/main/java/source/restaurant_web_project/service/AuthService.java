package source.restaurant_web_project.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import source.restaurant_web_project.model.dto.UserRegisterDTO;
import source.restaurant_web_project.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

public interface AuthService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    void register(UserRegisterDTO userRegisterDTO);


    User findUserByEmail(String email);

    boolean verifyToken(String token, String email);

    void restorePassword(String password, String email);

    String getMessageWithToken(User user, HttpServletRequest request) throws MalformedURLException;

    boolean checkTokenExist(String email);

    boolean checkUserExist(String checkValue, String checkByField);

    boolean verifyPassword(String email, String password);
}
