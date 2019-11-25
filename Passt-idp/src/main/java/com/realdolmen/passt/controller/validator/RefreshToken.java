package com.realdolmen.passt.controller.validator;

import com.auth0.jwt.interfaces.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;

/**
 *
 * @author Angelo Carly
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RefreshTokenValidator.class)
public @interface RefreshToken {
    String message() default "Ill-formatted token";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}