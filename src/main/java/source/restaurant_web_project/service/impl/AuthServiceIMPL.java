package source.restaurant_web_project.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.repository.ConfgurationRepository;
import source.restaurant_web_project.model.dto.authentication.UserRegisterDTO;
import source.restaurant_web_project.model.entity.Role;
import source.restaurant_web_project.model.entity.Token;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.repository.AddressRepository;
import source.restaurant_web_project.repository.RoleRepository;
import source.restaurant_web_project.repository.TokenRepository;
import source.restaurant_web_project.repository.UserRepository;
import source.restaurant_web_project.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthServiceIMPL implements AuthService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final ConfgurationRepository confgurationRepository;

    @Autowired
    public AuthServiceIMPL(TokenRepository passwordResetTokenRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, RoleRepository roleRepository, AddressRepository addressRepository, ConfgurationRepository confgurationRepository) {
        this.tokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.addressRepository = addressRepository;
        this.confgurationRepository = confgurationRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("Username not found!");
        }

        if(!user.isEnabled()){
            throw new IllegalAccessError("User is disabled!");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {

        User user = modelMapper.map(userRegisterDTO,User.class);
        user.setAddress(new ArrayList<>());
        Role role = roleRepository.findByName("ROLE_USER");
        List<Role> roles = List.of(role);
        String password = bCryptPasswordEncoder.encode(userRegisterDTO.getPassword());
        user.setPassword(password);
        user.setEnabled(true);
        if(userRepository.count()==0){
            roles = roleRepository.findAll();
        }
        user.setRoles(roles);

        userRepository.save(user);
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
    public String getMessageWithToken(User user, String urlPath) throws MalformedURLException {
        String token = UUID.randomUUID().toString();
        Token passwordChangeToken = new Token(user.getEmail(),token);
        passwordChangeToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        tokenRepository.save(passwordChangeToken);
        URL url = new URL(urlPath);
        String urlVerify = String.format("http://%s:%s/auth/password-reset/verify?token=%s&email=%s",url.getHost(),url.getPort(),token,user.getEmail());
        String restaurantName = confgurationRepository.findAll().stream().findFirst().get().getName();

   return String.format("""
           Hey %s!
           We just need to verify your email address before you can continue with next step of your password reseting!
           
           Verify your email address at this link %s
           
           Best regards, the %s team!
           
           ----------------------------------------------------------------------------------
           
           This message is automatically generated by our system, dont answer it!
           """,user.getUsername(),urlVerify, restaurantName);}

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

    @Override
    public boolean checkUserExist(String checkValue, String checkByField) {
        switch (checkByField){
            case "email":
                if(userRepository.findUserByEmail(checkValue)!=null){
                    return true;
                }
            case "username":
                if(userRepository.findUserByUsername(checkValue)!=null){
                    return true;
                }
        }
        return false;
    }

    @Override
    public boolean verifyPassword(String email, String password) {
        return bCryptPasswordEncoder.matches(password,userRepository.findUserByEmail(email).getPassword());
    }
}
