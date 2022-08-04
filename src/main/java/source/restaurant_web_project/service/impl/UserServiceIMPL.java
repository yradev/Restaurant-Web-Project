package source.restaurant_web_project.service.impl;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.model.dto.UserSettingsDTO;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.repository.UserRepository;
import source.restaurant_web_project.service.UserService;
import source.restaurant_web_project.util.DataValidator;

@Service
public class UserServiceIMPL implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final DataValidator dataValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceIMPL(UserRepository userRepository, ModelMapper modelMapper, DataValidator dataValidator, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.dataValidator = dataValidator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public User getUser(String name) {
        return userRepository.findUserByUsername(name);
    }

    @Override
    public void changeSettings(UserSettingsDTO userSettingsDTO, String name) {
        User user = userRepository.findUserByUsername(name);

        modelMapper.getConfiguration().setPropertyCondition(Conditions.not(a->a.getSource().equals("")));
        modelMapper.map(userSettingsDTO,user);

        if(!userSettingsDTO.getPassword().equals("")){
            user.setPassword(bCryptPasswordEncoder.encode(userSettingsDTO.getPassword()));
        }

      userRepository.save(user);

    }

}
