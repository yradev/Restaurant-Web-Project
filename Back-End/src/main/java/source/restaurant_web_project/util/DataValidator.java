package source.restaurant_web_project.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import source.restaurant_web_project.models.dto.item.CategoryAddDTO;
import source.restaurant_web_project.models.dto.item.CategoryEditDTO;
import source.restaurant_web_project.models.dto.item.ItemEditDTO;
import source.restaurant_web_project.models.dto.user.UserAddressSettingsDTO;
import source.restaurant_web_project.models.dto.user.UserChangeEmailDTO;
import source.restaurant_web_project.models.dto.user.UserPasswordChangeDTO;
import source.restaurant_web_project.models.entity.Address;
import source.restaurant_web_project.models.entity.Category;
import source.restaurant_web_project.models.entity.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataValidator {
    public final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DataValidator(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public BindingResult validateCategoryAdd(CategoryAddDTO categoryAddDTO, Category category, BindingResult bindingResult) {
        if(category!=null && category.getName().equals(categoryAddDTO.getName())){
            bindingResult.addError(new FieldError("categoryExist","name","We have category with this name!"));
        }
        return bindingResult;
    }

    public BindingResult validateCategoryEdit(BindingResult bindingResult, CategoryEditDTO categoryEditDTO, Category category) {
        if(categoryEditDTO.getName()!=null){
            if(categoryEditDTO.getName().length()<3 || categoryEditDTO.getName().length()>10){
                bindingResult.addError( new FieldError("categoryNameLength","name","Category name must be between 3 and 10 chars!"));
            }
        }

        if(category!=null) {
                FieldError fieldError = new FieldError("categoryNameExist", "name", "Category name exists!");
                bindingResult.addError(fieldError);
            }

        return bindingResult;
    }

    public BindingResult validateItemAdd(BindingResult bindingResult, Item item) {
        if(item!=null){
            FieldError fieldError = new FieldError("nameEquals","name","Item name exist!");
            bindingResult.addError(fieldError);
        }
        return bindingResult;
    }

    public void validateItemEdit(BindingResult bindingResult, ItemEditDTO itemEditDTO, Item item) {
        if(item!=null && !itemEditDTO.getName().equals("")){
            FieldError fieldError = new FieldError("nameEquals","name","Item name exist!");
            bindingResult.addError(fieldError);
        }

        if(!itemEditDTO.getName().isEmpty()){
            if(itemEditDTO.getName().length()<3 || itemEditDTO.getName().length()>20){
                FieldError fieldError = new FieldError("nameLengthError","name","Item name must be between 3 and 20 chars");
                bindingResult.addError(fieldError);
            }

        }
    }

    public void validateEmail(UserChangeEmailDTO emailDTO, BindingResult bindingResult) {
        String currentEmail = emailDTO.getCurrentEmail();
        String newEmail = emailDTO.getEmail();

        if(!newEmail.isEmpty()) {
            if (currentEmail.equals(newEmail)) {
                FieldError fieldError = new FieldError("equalEmails", "email", "Email is equal to current!");
                bindingResult.addError(fieldError);
            } else {
                if (!emailDTO.isEmailIsFree()) {
                    FieldError fieldError = new FieldError("equalNotFree", "email", "This email is used from another user!");
                    bindingResult.addError(fieldError);
                }
            }
        }
    }

    public BindingResult validatePasswordChange(UserPasswordChangeDTO userPasswordChangeDTO, BindingResult bindingResult, String currentEncryptedPassword) {

        String currentPassword = userPasswordChangeDTO.getCurrentPassword();
        String newPassword = userPasswordChangeDTO.getPassword();
        String confirmNewPassword = userPasswordChangeDTO.getConfirmPassword();

            if (!bCryptPasswordEncoder.matches(currentPassword, currentEncryptedPassword)) {
                FieldError fieldError = new FieldError("wrongOldPassword", "currentPassword", "Current password is wrong!");
                bindingResult.addError(fieldError);
            }

            if (!newPassword.equals(confirmNewPassword)) {
                FieldError fieldError = new FieldError("NotEqualPasswords", "confirmPassword", "Passwords are not equal!");
                bindingResult.addError(fieldError);
            }
        return bindingResult;
    }

    public BindingResult validateAddressAdd(BindingResult bindingResult, UserAddressSettingsDTO userAddressSettingsDTO, List<Address> address) {

        if(!address.isEmpty()){
            List<Address>addresses = address.stream().filter(a->a.getName().equals(userAddressSettingsDTO.getName())).collect(Collectors.toList());

            if(!addresses.isEmpty()){
                bindingResult.addError(new FieldError("nameExist","name","Address name exists in your addresses!"));
            }
        }

        return bindingResult;
    }

    public boolean isUserLogged() {
        return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("Guest");
    }
}
