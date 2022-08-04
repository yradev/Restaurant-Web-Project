package source.restaurant_web_project.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import source.restaurant_web_project.model.dto.UserSettingsDTO;
import source.restaurant_web_project.model.entity.User;

@Component
public class DataValidator {
    public final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DataValidator(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean isUserLogged(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken);
   }

    public BindingResult validateSettings(User user, BindingResult bindingResult, UserSettingsDTO userSettingsDTO) {

        // email settings verification

        if(user.getEmail().equals(userSettingsDTO.getEmail())){
            FieldError fieldError = new FieldError("equalEmails","email","Email is equal to current!");
            bindingResult.addError(fieldError);
        }

        // password settings verification


        if(!userSettingsDTO.getCurrentPassword().isEmpty() && !bCryptPasswordEncoder.matches(userSettingsDTO.getCurrentPassword(),user.getPassword())){
            FieldError fieldError = new FieldError("wrongOldPassword","currentPassword","Current password is wrong!");
            bindingResult.addError(fieldError);


        }
        if(!userSettingsDTO.getConfirmPassword().isEmpty() && !userSettingsDTO.getConfirmPassword().isEmpty() && !userSettingsDTO.getPassword().equals(userSettingsDTO.getConfirmPassword())){
            FieldError fieldError = new FieldError("NotEqualPasswords","confirmPassword","Passwords are not equal!");
            bindingResult.addError(fieldError);
        }

        // address settings verification


        if(!userSettingsDTO.getAddressCity().isEmpty() && user.getAddress().getCity()!=null && user.getAddress().getCity().equals(userSettingsDTO.getAddressCity())){
            FieldError fieldError = new FieldError("EqualCity","addressCity","City cant be equal to current!");
            bindingResult.addError(fieldError);
        }

        if(!userSettingsDTO.getAddressVillage().isEmpty() && user.getAddress().getVillage()!=null && user.getAddress().getVillage().equals(userSettingsDTO.getAddressVillage())){
            FieldError fieldError = new FieldError("EqualVillage","addressVillage","Village cant be equal to current!");
            bindingResult.addError(fieldError);
        }

        if(!userSettingsDTO.getAddressStreet().isEmpty() && user.getAddress().getStreet()!=null && user.getAddress().getStreet().equals(userSettingsDTO.getAddressStreet())){
            FieldError fieldError = new FieldError("EqualStreet","addressStreet","Street cant be equal to current!");
            bindingResult.addError(fieldError);
        }

        if(userSettingsDTO.getAddressNumber()!=0 && user.getAddress().getNumber()!=0 && user.getAddress().getNumber()==userSettingsDTO.getAddressNumber()){
            FieldError fieldError = new FieldError("EqualNumber","addressNumber","House or block number cant be equal to current!");
            bindingResult.addError(fieldError);
        }

        if(!userSettingsDTO.getAddressEntrance().isEmpty() && user.getAddress().getEntrance()!=null && user.getAddress().getEntrance().equals(userSettingsDTO.getAddressEntrance())){
            FieldError fieldError = new FieldError("EqualEntrance","addressEntrance","Entrance cant be equal to current!");
            bindingResult.addError(fieldError);
        }

        if(userSettingsDTO.getAddressFloor()!=0 && user.getAddress().getFloor()!=0 && user.getAddress().getNumber()==userSettingsDTO.getAddressFloor()){
            FieldError fieldError = new FieldError("EqualFloor","addressFloor","Floor cant be equal to current!");
            bindingResult.addError(fieldError);
        }

        if(userSettingsDTO.getAddressApartmentNumber()!=0 && user.getAddress().getApartmentNumber()!=0 && user.getAddress().getApartmentNumber()==userSettingsDTO.getAddressApartmentNumber()){
            FieldError fieldError = new FieldError("EqualApartmentNumbers","addressApartmentNumber","Apartment number cant be equal to current!");
            bindingResult.addError(fieldError);
        }

        return bindingResult;
    }
}
