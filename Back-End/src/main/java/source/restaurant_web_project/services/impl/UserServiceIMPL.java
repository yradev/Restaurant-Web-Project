package source.restaurant_web_project.services.impl;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import source.restaurant_web_project.models.dto.user.*;
import source.restaurant_web_project.models.entity.Address;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.models.entity.superClass.BaseEntity;
import source.restaurant_web_project.repositories.AddressRepository;
import source.restaurant_web_project.repositories.RoleRepository;
import source.restaurant_web_project.repositories.UserRepository;
import source.restaurant_web_project.services.UserService;
import source.restaurant_web_project.util.DataValidator;

import java.util.stream.Collectors;

@Service
public class UserServiceIMPL implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final DataValidator dataValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;

    public UserServiceIMPL(UserRepository userRepository, ModelMapper modelMapper, DataValidator dataValidator, BCryptPasswordEncoder bCryptPasswordEncoder, AddressRepository addressRepository, RoleRepository roleRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.not(a->a.getSource()==null || a.getSource().equals("") || a.getSource().toString().equals("0")));
        this.dataValidator = dataValidator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public User getUser(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void changeEmail(UserChangeEmailDTO userSettingsDTO, String email) {
        User user = userRepository.findUserByEmail(email);
        modelMapper.map(userSettingsDTO,user);

        userRepository.save(user);
    }

    @Override
    public void changeSettings(UserPasswordChangeDTO userSettingsDTO, String email) {
        User user = userRepository.findUserByEmail(email);
        modelMapper.map(userSettingsDTO,user);

        userRepository.save(user);
    }


    @Override
    public UserViewSettingsDTO getUserSettings(String email) {
        return modelMapper.map(userRepository.findUserByEmail(email),UserViewSettingsDTO.class);
    }

    @Override
    public boolean verifyUserByEmail(String email) {
        return userRepository.findUserByEmail(email) == null;
    }

    @Override
    public BindingResult validateUserChangeEmail(UserChangeEmailDTO userChangeEmailDTO,BindingResult bindingResult, String name) {

        dataValidator.validateEmail(userChangeEmailDTO,bindingResult);

        return bindingResult;
    }

    @Override
    public BindingResult validateUserChangePassword(UserPasswordChangeDTO userPasswordChangeDTO, BindingResult bindingResult, String email) {
        User currentUser = userRepository.findUserByEmail(email);
        bindingResult = dataValidator.validatePasswordChange(userPasswordChangeDTO,bindingResult,currentUser.getPassword());
       return bindingResult;
    }

    @Override
    public void changePassword(UserPasswordChangeDTO userPasswordChangeDTO, String email) {
        String encryptedNewPassword = bCryptPasswordEncoder.encode(userPasswordChangeDTO.getPassword());
        User currentLoggedUser = userRepository.findUserByEmail(email);
        currentLoggedUser.setPassword(encryptedNewPassword);
        userRepository.saveAndFlush(currentLoggedUser);
    }

    @Override
    public BindingResult validateAddress(UserAddressSettingsDTO userAddressSettingsDTO, BindingResult bindingResult, String email) {
        User user = userRepository.findUserByEmail(email);

        return dataValidator.validateAddressAdd(bindingResult,userAddressSettingsDTO,user.getAddress());
    }

    @Override
    public void addAddress(UserAddressSettingsDTO userAddressSettingsDTO, String email) {
        User user = userRepository.findUserByEmail(email);
        Address address = modelMapper.map(userAddressSettingsDTO,Address.class);
        address.setUser(user);
        addressRepository.saveAndFlush(address);
    }

    @Override
    public void editAddress(UserAddressSettingsDTO userAddressSettingsDTO, BindingResult bindingResult, String email) {
        Long id = userRepository.findUserByEmail(email).getAddress().stream()
                .filter(a->a.getName()
                        .equals(userAddressSettingsDTO.getCurrentName()))
                .map(BaseEntity::getId)
                .findFirst().orElse(null);

        Address address = addressRepository.findAddressById(id);
       modelMapper.map(userAddressSettingsDTO,address);
       User user = userRepository.findUserByEmail(email);
       address.setUser(user);
       addressRepository.saveAndFlush(address);
    }

    @Override
    public UserControlDTO findUserForUserControl(String email) {
        User user = userRepository.findUserByEmail(email);
        return user==null?null:modelMapper.map(user,UserControlDTO.class);
    }

    @Override
    public void addRole(String email, String role) {
        User user = userRepository.findUserByEmail(email);
        user.getRoles().add(roleRepository.findByName(role));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void removeRole(String email, String role) {
        User user = userRepository.findUserByEmail(email);
        user.setRoles(user.getRoles().stream().filter(currentRole->!currentRole.getName().equals(role)).collect(Collectors.toList()));
        userRepository.saveAndFlush(user);
    }
}
