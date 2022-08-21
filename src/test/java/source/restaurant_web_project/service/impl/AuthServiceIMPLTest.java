package source.restaurant_web_project.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import source.restaurant_web_project.configuration.enums.Roles;
import source.restaurant_web_project.model.entity.Role;
import source.restaurant_web_project.model.entity.Token;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.repository.*;
import source.restaurant_web_project.service.AuthService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceIMPLTest {

    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ConfgurationRepository confgurationRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private AuthServiceIMPL authServiceTest;

    private User testUserEntity;
    @BeforeEach
    void setUp() {
        authServiceTest  = new AuthServiceIMPL(tokenRepository,userRepository,bCryptPasswordEncoder,modelMapper,roleRepository,addressRepository,confgurationRepository);
        Role role1 = new Role();
        role1.setName("ROLE_USER");

        testUserEntity = new User();
        testUserEntity.setUsername("validTestUsername");
        testUserEntity.setEmail("myEmail@a.b");
        testUserEntity.setRoles(List.of(role1));
        testUserEntity.setPassword(bCryptPasswordEncoder.encode("testPassword"));
        testUserEntity.setEnabled(true);
    }

    @Test
    void loadUserByUsernameValid() {
        when(userRepository.findUserByUsername(testUserEntity.getUsername())).thenReturn(testUserEntity);

        UserDetails userDetails = authServiceTest.loadUserByUsername(testUserEntity.getUsername());

        Assertions.assertEquals(testUserEntity.getUsername(),userDetails.getUsername());
    }

    @Test
    void loadUserByUsernameNotFound(){
        UsernameNotFoundException thrown = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> {authServiceTest.loadUserByUsername("invalidUserName");});

        Assertions.assertEquals("Username not found!",thrown.getMessage());
    }

    @Test
    void loadUserByUsernameIsNotActive(){
       testUserEntity.setEnabled(false);

        when(userRepository.findUserByUsername(testUserEntity.getUsername())).thenReturn(testUserEntity);

        UsernameNotFoundException thrown = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> {authServiceTest.loadUserByUsername(testUserEntity.getUsername());});

        Assertions.assertEquals("Username not found!",thrown.getMessage());    }

    @Test
    void findUserByEmailValid() {
        when(userRepository.findUserByEmail(testUserEntity.getEmail())).thenReturn(testUserEntity);

        Assertions.assertEquals(testUserEntity.getEmail(),authServiceTest.findUserByEmail(testUserEntity.getEmail()).getEmail());
    }

    @Test
    void findUserByEmailInvalid(){
        assertNull(authServiceTest.findUserByEmail("invalidEmailValue"));
    }

    @Test
    void verifyTokenFound() {
        Token token = new Token();
        token.setToken("token");
        token.setEmail(testUserEntity.getEmail());

        when(tokenRepository.findPasswordResetTokenByEmailAndToken(token.getEmail(),token.getToken())).thenReturn(token);

        assertTrue(authServiceTest.verifyToken(token.getToken(), token.getEmail()));
    }

    @Test
    void verifyTokenIsInvalid(){
        assertFalse(authServiceTest.verifyToken("invalid-token","invalid-email"));
    }

    @Test
    void checkTokenExistNotToken() {
        when(tokenRepository.findPasswordResetTokenByEmail(testUserEntity.getEmail())).thenReturn(null);
        Assertions.assertFalse(authServiceTest.checkTokenExist(testUserEntity.getEmail()));
    }

    @Test
    void checkTokenExistIsExpired(){
        Token token = new Token();
        token.setExpiryDate(LocalDateTime.now().minusMinutes(15));
        when(tokenRepository.findPasswordResetTokenByEmail(testUserEntity.getEmail())).thenReturn(token);

        Assertions.assertFalse(authServiceTest.checkTokenExist(testUserEntity.getEmail()));
    }

    @Test
    void checkTokenExistIsValid(){
        Token token = new Token();
        token.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        when(tokenRepository.findPasswordResetTokenByEmail(testUserEntity.getEmail())).thenReturn(token);
        Assertions.assertTrue(authServiceTest.checkTokenExist(testUserEntity.getEmail()));
    }

    @Test
    void checkUserExistEmail (){
        when(userRepository.findUserByEmail(testUserEntity.getEmail())).thenReturn(testUserEntity);
        Assertions.assertTrue(authServiceTest.checkUserExist(testUserEntity.getEmail(),"email"));
    }

    @Test
    void checkUserExistEmailInvalid (){
        when(userRepository.findUserByEmail(testUserEntity.getEmail())).thenReturn(null);
        Assertions.assertFalse(authServiceTest.checkUserExist(testUserEntity.getEmail(),"email"));
    }

    @Test
    void checkUserExistUsername (){
        when(userRepository.findUserByUsername(testUserEntity.getUsername())).thenReturn(testUserEntity);
        Assertions.assertTrue(authServiceTest.checkUserExist(testUserEntity.getUsername(),"username"));
    }

    @Test
    void checkUserExistUsernameInvalid (){
        when(userRepository.findUserByUsername(testUserEntity.getUsername())).thenReturn(null);
        Assertions.assertFalse(authServiceTest.checkUserExist(testUserEntity.getUsername(),"username"));
    }

    @Test
    void verifyPassword() {
        String password = "testPassword";
        when(userRepository.findUserByEmail(testUserEntity.getEmail())).thenReturn(testUserEntity);
        Assertions.assertTrue(authServiceTest.verifyPassword(testUserEntity.getEmail(),password));
    }

    @Test
    void verifyPasswordWrong() {
        String password = "wrongPassword";
        when(userRepository.findUserByEmail(testUserEntity.getEmail())).thenReturn(testUserEntity);
        Assertions.assertFalse(authServiceTest.verifyPassword(testUserEntity.getEmail(),password));
    }

    @Test
    void register(){

    }

}