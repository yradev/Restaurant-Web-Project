package source.restaurant_web_project.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import source.restaurant_web_project.models.dto.authentication.UserRegisterDTO;
import source.restaurant_web_project.models.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

public interface AuthService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User register(UserRegisterDTO userRegisterDTO);

    void login(String token, HttpServletRequest request, UserDetails userDetails);

    void sendVerifyMessage(String email, String url);

    boolean verifyToken(String token, String email);

    void resetPassword(String password, String email);




}