package source.restaurant_web_project.errors;

import org.springframework.security.core.AuthenticationException;

public class DisabledUserException extends AuthenticationException {
    public DisabledUserException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DisabledUserException(String msg) {
        super(msg);
    }
}
