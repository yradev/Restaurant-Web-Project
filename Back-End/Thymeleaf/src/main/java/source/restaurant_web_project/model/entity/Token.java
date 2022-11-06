package source.restaurant_web_project.model.entity;

import source.restaurant_web_project.model.entity.superClass.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "tokens")
public class Token extends BaseEntity {
    private String token;
    private String email;

    private LocalDateTime expiryDate;

    public Token() {}

    public Token(String email, String token){
        this.email = email;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
