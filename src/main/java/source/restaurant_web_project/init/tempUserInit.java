package source.restaurant_web_project.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.model.entity.Role;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.repository.RoleRepository;
import source.restaurant_web_project.repository.UserRepository;
import source.restaurant_web_project.util.EmailSender;

import java.util.List;

@Component
public class tempUserInit implements CommandLineRunner {
   private final UserRepository userRepository;
   private final RoleRepository roleRepository;
   private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public tempUserInit(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
       this.roleRepository = roleRepository;
       this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count()==0){
            User user = new User();
            String password = bCryptPasswordEncoder.encode("123");
            user.setPassword(password);
            user.setUsername("test");
            user.setEnabled(true);
            user.setEmail("yanko.radev1995@gmail.com");
            List<Role> roleList = roleRepository.findAll();
            user.setRoles(roleList);
            userRepository.save(user);
        }
    }
}
