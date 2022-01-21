package user.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final String message;
    private final int code;
    private final String status;
    private final String objectName;
    private final List<ErrorObject> errors;
}
