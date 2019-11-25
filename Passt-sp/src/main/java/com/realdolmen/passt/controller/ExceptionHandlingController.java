package com.realdolmen.passt.controller;

/**
 *
 * @author Angelo Carly
 */
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlingController
{

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public @ResponseBody
    List<ConstraintViolationModel> handleConstraintViolation(
            HttpServletRequest req, final ConstraintViolationException exception)
    {
        ArrayList<ConstraintViolationModel> list = new ArrayList<ConstraintViolationModel>();
        for (ConstraintViolation<?> violation : exception
                .getConstraintViolations())
        {
            list.add(new ConstraintViolationModel(violation.getPropertyPath()
                    .toString(), violation.getMessage(), violation
                    .getInvalidValue()));
        }
        return list;
    }

    private static class ConstraintViolationModel
    {

        public String field;
        public String message;
        public Object invalidValue;

        public ConstraintViolationModel(String field, String message,
                Object invalidValue)
        {
            this.field = field;
            this.message = message;
            this.invalidValue = invalidValue;
        }
    }
}
