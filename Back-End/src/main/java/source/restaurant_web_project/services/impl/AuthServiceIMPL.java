package source.restaurant_web_project.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.errors.DisabledUserException;
import source.restaurant_web_project.repositories.RestaurantConfigurationRepository;
import source.restaurant_web_project.models.dto.authentication.UserRegisterDTO;
import source.restaurant_web_project.models.entity.Role;
import source.restaurant_web_project.models.entity.Token;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.repositories.RoleRepository;
import source.restaurant_web_project.repositories.TokenRepository;
import source.restaurant_web_project.repositories.UserRepository;
import source.restaurant_web_project.services.AuthService;
import source.restaurant_web_project.util.EmailSender;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthServiceIMPL implements AuthService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final RestaurantConfigurationRepository confgurationRepository;

    @Autowired
    public AuthServiceIMPL(TokenRepository passwordResetTokenRepository, UserRepository userRepository, EmailSender emailSender, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, RoleRepository roleRepository, RestaurantConfigurationRepository confgurationRepository) {
        this.tokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.confgurationRepository = confgurationRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found!");
        }

        if (!user.isEnabled()) {
            throw new DisabledUserException("User is disabled!");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }

    @Override
    public void login(String token, HttpServletRequest request, UserDetails userDetails) {

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {

        User user = modelMapper.map(userRegisterDTO, User.class);

        if (userRepository.findUserByEmail(user.getEmail()) != null) {
            throw new BadCredentialsException("We have user with this email!");
        }

        user.setAddress(new ArrayList<>());
        Role role = roleRepository.findByName("ROLE_USER");
        List<Role> roles = List.of(role);
        String password = bCryptPasswordEncoder.encode(userRegisterDTO.getPassword());
        user.setPassword(password);

        user.setEnabled(true);

        if (userRepository.count() == 0) {
            roles = roleRepository.findAll();
        }
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public void sendVerifyMessage(String email, String url) {
        if(userRepository.findUserByEmail(email)==null){
            throw new BadCredentialsException(String.format("We dont have user with email %s!",email));
        }

        Token passwordChangeToken = tokenRepository.findPasswordResetTokenByEmail(email);

        if(passwordChangeToken != null){
            if (LocalDateTime.now().isAfter(passwordChangeToken.getExpiryDate())) {
                tokenRepository.delete(passwordChangeToken);
            }else{
                throw new BadCredentialsException("We have active token!");
            }
        }

            String token = UUID.randomUUID().toString();
            passwordChangeToken = new Token(email, token);
            passwordChangeToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
            tokenRepository.save(passwordChangeToken);

        String urlVerify = String.format("%s?token=%s&email=%s", url, passwordChangeToken.getToken(), email);
        String restaurantName = confgurationRepository.findAll().stream().findFirst().get().getName();

        String message = String.format("""
                Hey there!
                We just need to verify your email address before you can continue with next step of your password reseting!
                           
                Verify your email address at this link %s
                
                Link is avaliable for next 10min!
                           
                Best regards, the %s team!
                           
                ----------------------------------------------------------------------------------
                           
                This message is automatically generated by our system, dont answer it!
                """, urlVerify, restaurantName);

        emailSender.sendEmail(email,"Password reset", message);
    }

    @Override
    public void resetPassword(String token, String email, String password) {
        Token currToken = tokenRepository.findPasswordResetTokenByEmailAndToken(email, token);

        if(currToken==null){
            throw new BadCredentialsException("Invalid token!");
        }

        if (LocalDateTime.now().isAfter(currToken.getExpiryDate())) {
            tokenRepository.delete(currToken);
            throw new BadCredentialsException("Invalid token!");
        }

        String encrPassword = bCryptPasswordEncoder.encode(password);

        User user = userRepository.findUserByEmail(email);

        if(user==null){
            throw new BadCredentialsException(String.format("User with email %s doesnt exist!",email));
        }
        user.setPassword(encrPassword);
        userRepository.save(user);

        tokenRepository.delete(currToken);
    }
}