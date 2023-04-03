package user.config;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class RestErrorHandler {

    @Value("${server.error.include-message}")
    private String include_message;
    @Value("${server.error.include-exception}")
    private String include_exception;
    @Value("${server.error.include-stacktrace}")
    private String include_stacktrace;
    @Value("${server.error.include-binding-errors}")
    private String include_binding_errors;
    static private Logger logger = LoggerFactory.getLogger(RestErrorHandler.class);

    @ExceptionHandler(ApiUserDefaultException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map processValidationError(WebRequest webRequest, ApiUserDefaultException ex) {
        logger.error(ex.getMessage()+" -> "+ex.getCause());
        DefaultErrorAttributes err = new DefaultErrorAttributes();
        final int resquetScope = 0;
        webRequest.setAttribute("javax.servlet.error.status_code", HttpStatus.BAD_REQUEST.value(), resquetScope);
        return err.getErrorAttributes(webRequest, getErrorAttributeOptions());
    }


    private ErrorAttributeOptions getErrorAttributeOptions(){
        ErrorAttributeOptions errorAttributeOptions = ErrorAttributeOptions.defaults();
        if("always".equalsIgnoreCase(include_message))
            errorAttributeOptions = errorAttributeOptions.including(ErrorAttributeOptions.Include.MESSAGE);
        if("true".equalsIgnoreCase(include_exception))
            errorAttributeOptions = errorAttributeOptions.including(ErrorAttributeOptions.Include.EXCEPTION);
        if("always".equalsIgnoreCase(include_stacktrace))
            errorAttributeOptions = errorAttributeOptions.including(ErrorAttributeOptions.Include.STACK_TRACE);
        if("always".equalsIgnoreCase(include_binding_errors))
            errorAttributeOptions = errorAttributeOptions.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        return errorAttributeOptions;

    }
}
