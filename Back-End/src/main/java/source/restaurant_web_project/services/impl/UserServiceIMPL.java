package source.restaurant_web_project.services.impl;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.errors.ConflictException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.models.dto.user.*;
import source.restaurant_web_project.models.entity.Address;
import source.restaurant_web_project.models.entity.Role;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.repositories.AddressRepository;
import source.restaurant_web_project.repositories.RoleRepository;
import source.restaurant_web_project.repositories.UserRepository;
import source.restaurant_web_project.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceIMPL implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;

    public UserServiceIMPL(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder, AddressRepository addressRepository, RoleRepository roleRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        modelMapper.getConfiguration().setPropertyCondition(Conditions.not(a->a.getSource()==null || a.getSource().toString().equals("0")));
    }


    @Override
    public UserDataViewDTO getUserData(String email) {
        User user = userRepository.findUserByEmail(email);

        if(user==null){
            throw new NotFoundException("We dont have user with this email!");
        }

        return modelMapper.map(user, UserDataViewDTO.class);
    }

    @Override
    public void editUserData(UserEditDTO userEditDTO) {
        User currUser = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!currUser.getEmail().equals(userEditDTO.getEmail()) && userEditDTO.getEmail()!=null) {
            if (userRepository.findUserByEmail(userEditDTO.getEmail()) != null) {
                throw new ConflictException("We have user with this email!");
            }
        }

        if(userEditDTO.getOldPassword()!=null){
            if(!bCryptPasswordEncoder.matches(userEditDTO.getOldPassword(),currUser.getPassword())){
                throw new BadCredentialsException("Old password is not valid!");
            } else{
                if(userEditDTO.getNewPassword()!=null){
                    currUser.setPassword(bCryptPasswordEncoder.encode(userEditDTO.getNewPassword()));
                }
            }
        }

        modelMapper.map(userEditDTO, currUser);

        List<AddressDTO> addressDTO = userEditDTO.getAddress();

        if (addressDTO.size() > 0) {
            addressDTO.forEach(a -> {
                if (a.getId() == 0) {
                    Address address = modelMapper.map(a,Address.class);
                    address.setUser(currUser);
                    a.setId(addressRepository.saveAndFlush(modelMapper.map(address, Address.class)).getId());
                }
            });
        }

        currUser.setAddress(addressDTO.stream().map(a->modelMapper.map(a,Address.class)).collect(Collectors.toList()));

        userRepository.saveAndFlush(currUser);
    }

    @Override
    public List<RoleDTO> getActiveRoles() {
        return roleRepository.findAll().stream().map(a -> modelMapper.map(a, RoleDTO.class)).toList();
    }

    @Override
    public void changeUserCoreData(String email, UserControlDTO userControlDTO) {
        User user = userRepository.findUserByEmail(email);

        if(user == null){
            throw new NotFoundException("We dont have user with this email!");
        }

        List<RoleDTO> roleDTOS = userControlDTO.getRoles();

        if(roleDTOS == null || roleDTOS.stream().noneMatch(a->a.getName().equals("ROLE_USER"))){
            throw new ConflictException("We must have user role!");
        }

        List<Role> roles = userControlDTO.getRoles().stream().map(a -> {
            Role role = roleRepository.findByName(a.getName());
            if (role == null) {
                throw new NotFoundException(String.format("We dont have role with name %s !",a.getName()));
            }

            return role;
        }).toList();

        user.getRoles().clear();
        user.getRoles().addAll(roles);

        if(userControlDTO.isEnabled()){
            if(!user.isEnabled()){
                user.setEnabled(true);
            }
        }else{
            if(user.isEnabled()){
                user.setEnabled(false);
            }
        }

        userRepository.saveAndFlush(user);
    }

    @Override
    public UserControlDTO getUserCoreSettings(String email) {
        User user = userRepository.findUserByEmail(email);

        if(user==null){
            throw new NotFoundException("We dont have user with this email!");
        }

        UserControlDTO userControlDTO = new UserControlDTO();modelMapper.map(user,userControlDTO);
        return userControlDTO;
    }
}
