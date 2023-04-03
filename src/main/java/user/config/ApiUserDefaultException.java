package user.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApiUserDefaultException extends Exception {
    public ApiUserDefaultException() {
        super();
    }

    public ApiUserDefaultException(String message) {
        super(message);
    }

    public ApiUserDefaultException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiUserDefaultException(String message, String cause) {
        super(message, new Throwable(cause));
    }

    public ApiUserDefaultException(Throwable cause) {
        super(cause);
    }
}
