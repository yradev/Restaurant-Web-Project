package source.restaurant_web_project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.model.dto.UserRegisterDTO;
import source.restaurant_web_project.model.entity.Token;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.repository.TokenRepository;
import source.restaurant_web_project.repository.UserRepository;
import source.restaurant_web_project.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthServiceIMPL implements AuthService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public AuthServiceIMPL(TokenRepository passwordResetTokenRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.tokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("Username not found!");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }

    @Override
    public boolean register(UserRegisterDTO userRegisterDTO) {
        return false;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public boolean verifyToken(String token, String email) {
        return tokenRepository.findPasswordResetTokenByEmailAndToken(email,token) != null;
    }

    @Override
    public void restorePassword(String password, String email) {
        String encrPassword = bCryptPasswordEncoder.encode(password);

        User user = userRepository.findUserByEmail(email);
        user.setPassword(encrPassword);
        userRepository.save(user);
        Token token = tokenRepository.findPasswordResetTokenByEmail(email);
        tokenRepository.delete(token);
    }

    @Override
    public String getMessageWithToken(User user, HttpServletRequest request) throws MalformedURLException {
        String token = UUID.randomUUID().toString();
        Token passwordChangeToken = new Token(user.getEmail(),token);
        passwordChangeToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        tokenRepository.save(passwordChangeToken);
        URL url = new URL(request.getRequestURL().toString());

        return "http://"+ url.getHost() +":" + url.getPort() +"/auth/password-reset/verifiy?token=" + token+ "&email=" + user.getEmail();}

    @Override
    public boolean checkTokenExist(String email) {
        Token token = tokenRepository.findPasswordResetTokenByEmail(email);
        if(token==null){
            return false;
        }

        if(LocalDateTime.now().isAfter(token.getExpiryDate())){
            tokenRepository.delete(token);
            return false;
        }

        return true;
    }
}
