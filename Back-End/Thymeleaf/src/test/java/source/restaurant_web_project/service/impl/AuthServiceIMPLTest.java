package source.restaurant_web_project.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.util.Assert;
import source.restaurant_web_project.configuration.RestaurantConfiguration;
import source.restaurant_web_project.configuration.enums.Roles;
import source.restaurant_web_project.model.dto.authentication.UserRegisterDTO;
import source.restaurant_web_project.model.entity.Role;
import source.restaurant_web_project.model.entity.Token;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.model.entity.configuration.RestaurantConfigurationEntity;
import source.restaurant_web_project.repository.*;
import source.restaurant_web_project.service.AuthService;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceIMPLTest {

    @Mock
    private TokenRepository mockedTokenRepository;
    @Mock
    private UserRepository mockedUserRepository;

    private ModelMapper modelMapper;
    @Mock
    private RoleRepository mockedRoleRepository;
    @Mock
    private AddressRepository mockedAddressRepository;
    @Mock
    private ConfgurationRepository mockedConfgurationRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private AuthService authServiceTest;

    private User testUserEntity;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        authServiceTest  = new AuthServiceIMPL(mockedTokenRepository, mockedUserRepository,bCryptPasswordEncoder,modelMapper, mockedRoleRepository, mockedAddressRepository, mockedConfgurationRepository);
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");

        Role roleOwner = new Role();
        roleOwner.setName("ROLE_OWNER");

        testUserEntity = new User();
        testUserEntity.setUsername("test_username");
        testUserEntity.setPassword(bCryptPasswordEncoder.encode("test_password"));
        testUserEntity.setEnabled(true);
        testUserEntity.setEmail("test_email");
        testUserEntity.setRoles(List.of(roleUser,roleOwner));
    }

    @Test
    public void loadUserByUsername_usernameNotFound(){
        when(mockedUserRepository.findUserByUsername("usernameNotFound")).thenReturn(null);
        UsernameNotFoundException exceptionCatch = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> {authServiceTest.loadUserByUsername("usernameNotFound");});

        Assertions.assertEquals("Username not found!",exceptionCatch.getMessage());
    }

    @Test
    public void loadUserByUserName_usernameIsDisabled(){
        testUserEntity.setEnabled(false);
        when(mockedUserRepository.findUserByUsername(testUserEntity.getUsername())).thenReturn(testUserEntity);

        IllegalAccessError exceptionCatch = Assertions.assertThrows(IllegalAccessError.class,
                ()-> {
            authServiceTest.loadUserByUsername(testUserEntity.getUsername());
        });

        Assertions.assertEquals("User is disabled!",exceptionCatch.getMessage());
    }

    @Test
    public void loadUserByUsername_usernameFound(){
        when(mockedUserRepository.findUserByUsername(testUserEntity.getUsername())).thenReturn(testUserEntity);
        UserDetails userDetails = authServiceTest.loadUserByUsername(testUserEntity.getUsername());
        Assertions.assertEquals(userDetails.getUsername(),testUserEntity.getUsername());
    }

    @Test
    public void register_gotAllRolesWhenFirstRegister(){
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("test_username");
        userRegisterDTO.setPassword("test_password");
        userRegisterDTO.setConfirmPassword("test_password");
        userRegisterDTO.setEmail("test_email");

        Role roleUser = new Role();
        Role roleOwner = new Role();
        roleUser.setName("ROLE_USER");
        roleOwner.setName("ROLE_OWNER");

        when(mockedRoleRepository.findByName(roleUser.getName())).thenReturn(roleUser);
        when(mockedUserRepository.count()).thenReturn(Long.valueOf(0));
        when(mockedRoleRepository.findAll()).thenReturn(List.of(roleUser,roleOwner));

        authServiceTest.register(userRegisterDTO);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(mockedUserRepository,times(1)).save(captor.capture());
        Assertions.assertEquals(2,captor.getValue().getRoles().size());
    }

    @Test
    public void register_gotOnlyUserRole(){
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("test_username");
        userRegisterDTO.setPassword("test_password");
        userRegisterDTO.setConfirmPassword("test_password");
        userRegisterDTO.setEmail("test_email");

        Role roleUser = new Role();
        Role roleOwner = new Role();
        roleUser.setName("ROLE_USER");
        roleOwner.setName("ROLE_OWNER");

        when(mockedRoleRepository.findByName(roleUser.getName())).thenReturn(roleUser);
        when(mockedUserRepository.count()).thenReturn(Long.valueOf(1));

        authServiceTest.register(userRegisterDTO);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(mockedUserRepository,times(1)).save(captor.capture());
        Assertions.assertEquals(1,captor.getValue().getRoles().size());
    }
    @Test
    void findUserByEmail_EmailNotFound() {
        when(mockedUserRepository.findUserByEmail("emailNotFound")).thenReturn(null);
        Assertions.assertNull(authServiceTest.findUserByEmail("emailNotFound"));
    }
    @Test
    void findUserByEmail_EmailFound() {
        when(mockedUserRepository.findUserByEmail(testUserEntity.getEmail())).thenReturn(testUserEntity);
        User user = authServiceTest.findUserByEmail(testUserEntity.getEmail());
        Assertions.assertEquals(testUserEntity.getEmail(),user.getEmail());
    }

    @Test
    void verifyToken_isFalse() {
        when(mockedTokenRepository.findPasswordResetTokenByEmailAndToken("wrong","wrong")).thenReturn(null);
        Assertions.assertFalse(authServiceTest.verifyToken("wrong","wrong"));
    }

    @Test
    void verifyToken_isTrue() {
        Token token = new Token();
        token.setEmail(testUserEntity.getEmail());
        token.setToken("token");
        when(mockedTokenRepository.findPasswordResetTokenByEmailAndToken(token.getEmail(),token.getToken())).thenReturn(token);
        Assertions.assertTrue(authServiceTest.verifyToken(token.getToken(),token.getEmail()));
    }

    @Test
    void restorePassword() {
        Token token = new Token();
        token.setEmail(testUserEntity.getEmail());
        token.setToken("token");
        when(mockedUserRepository.findUserByEmail(testUserEntity.getEmail())).thenReturn(testUserEntity);
        when(mockedTokenRepository.findPasswordResetTokenByEmail(testUserEntity.getEmail())).thenReturn(token);
        authServiceTest.restorePassword("somePassword",testUserEntity.getEmail());
        verify(mockedTokenRepository,times(1)).delete(token);
    }

    @Test
    void getMessageWithToken() throws MalformedURLException {
        RestaurantConfigurationEntity restaurantConfiguration = new RestaurantConfigurationEntity();
        restaurantConfiguration.setName("Sunny House");

        when(mockedConfgurationRepository.findAll()).thenReturn(List.of(restaurantConfiguration));
        ArgumentCaptor<Token> captor = ArgumentCaptor.forClass(Token.class);
        String urlPath = "http://test.com:1234";
        URL url = new URL(urlPath);

        String returnAnswer = authServiceTest.getMessageWithToken(testUserEntity,urlPath);

        verify(mockedTokenRepository,times(1)).save(captor.capture());
        String urlVerify = String.format("http://%s:%s/auth/password-reset/verify?token=%s&email=%s",url.getHost(),url.getPort(),captor.getValue().getToken(),captor.getValue().getEmail());

        String lookingFor = String.format("""
           Hey %s!
           We just need to verify your email address before you can continue with next step of your password reseting!
           
           Verify your email address at this link %s
           
           Best regards, the %s team!
           
           ----------------------------------------------------------------------------------
           
           This message is automatically generated by our system, dont answer it!
           """,testUserEntity.getUsername(),urlVerify, restaurantConfiguration.getName());

        Assertions.assertEquals(lookingFor,returnAnswer);
    }

    @Test
    void checkTokenExist_tokenDoesntExist() {
        when(mockedTokenRepository.findPasswordResetTokenByEmail(testUserEntity.getEmail())).thenReturn(null);
        Assertions.assertFalse(authServiceTest.checkTokenExist(testUserEntity.getEmail()));
    }

    @Test
    void checkTokenExist_tokenIsExpired() {
        Token token = new Token();
        token.setExpiryDate(LocalDateTime.now().minusMinutes(15));
        when(mockedTokenRepository.findPasswordResetTokenByEmail(testUserEntity.getEmail())).thenReturn(token);

        Assertions.assertFalse(authServiceTest.checkTokenExist(testUserEntity.getEmail()));
    }

    @Test
    void checkTokenExist() {
        Token token = new Token();
        token.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        when(mockedTokenRepository.findPasswordResetTokenByEmail(testUserEntity.getEmail())).thenReturn(token);
        Assertions.assertTrue(authServiceTest.checkTokenExist(testUserEntity.getEmail()));
    }

    @Test
    void checkUserExist_EmailNotFound() {
        when(mockedUserRepository.findUserByEmail(testUserEntity.getEmail())).thenReturn(null);
        Assertions.assertFalse(authServiceTest.checkUserExist(testUserEntity.getEmail(),"email"));
    }

    @Test
    void checkUserExist_EmailIsValid() {
        when(mockedUserRepository.findUserByEmail(testUserEntity.getEmail())).thenReturn(testUserEntity);
        Assertions.assertTrue(authServiceTest.checkUserExist(testUserEntity.getEmail(),"email"));
    }

    @Test
    void checkUserExist_UsernameIsInvalid() {
        when(mockedUserRepository.findUserByUsername(testUserEntity.getUsername())).thenReturn(testUserEntity);
        Assertions.assertTrue(authServiceTest.checkUserExist(testUserEntity.getUsername(),"username"));
    }

    @Test
    void checkUserExist_userNameIsValid() {
        when(mockedUserRepository.findUserByUsername(testUserEntity.getUsername())).thenReturn(null);
        Assertions.assertFalse(authServiceTest.checkUserExist(testUserEntity.getUsername(),"username"));
    }

    @Test
    void checkUserExist_fieldIsInvalid() {
        Assertions.assertFalse(authServiceTest.checkUserExist(testUserEntity.getUsername(),"invalidField"));
    }

    @Test
    void verifyPassword_valid() {
        String password = "test_password";
        when(mockedUserRepository.findUserByEmail(testUserEntity.getEmail())).thenReturn(testUserEntity);
        Assertions.assertTrue(authServiceTest.verifyPassword(testUserEntity.getEmail(),password));
    }

    @Test
    void verifyPasswordWrong_invalid() {
        String password = "wrongPassword";
        when(mockedUserRepository.findUserByEmail(testUserEntity.getEmail())).thenReturn(testUserEntity);
        Assertions.assertFalse(authServiceTest.verifyPassword(testUserEntity.getEmail(),password));
    }
}