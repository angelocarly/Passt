package com.realdolmen.passt.config;

import com.realdolmen.passt.exception.ResourceException;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception handler advice class for all SpringMVC controllers.
 * @author norrisshelton
 * @see org.springframework.web.bind.annotation.ControllerAdvice
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@org.springframework.web.bind.annotation.ControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * Handles ResourceExceptions for the SpringMVC controllers.
     * @param e SpringMVC controller exception.
     * @return http response entity
     * @see ExceptionHandler
     */
    @ExceptionHandler(ResourceException.class)
    public ResponseEntity handleException(ResourceException e) {
        JSONObject o = new JSONObject();
        o.put("error", e.getCause().getMessage());
        o.put("status", e.getHttpStatus().value());
        return ResponseEntity.status(e.getHttpStatus()).body(o);
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Error methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private Error processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        Error error = new Error(BAD_REQUEST.value(), "validation error");
        for (org.springframework.validation.FieldError fieldError: fieldErrors) {
            error.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return error;
    }

    private class Error {
        private final int status;
        private final String message;
        private List<FieldError> fieldErrors = new ArrayList<>();

        Error(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public void addFieldError(String path, String message) {
            FieldError error = new FieldError(path, message);
            fieldErrors.add(error);
        }

        public List<FieldError> getFieldErrors() {
            return fieldErrors;
        }
    }
    
    private class FieldError {
        private String message;
        private String field;

        public FieldError(String field, String message)
        {
            this.message = message;
            this.field = field;
        }
        
        public String getMessage()
        {
            return message;
        }

        public void setMessage(String message)
        {
            this.message = message;
        }

        public String getField()
        {
            return field;
        }

        public void setField(String field)
        {
            this.field = field;
        }
        
        
    }
    
}