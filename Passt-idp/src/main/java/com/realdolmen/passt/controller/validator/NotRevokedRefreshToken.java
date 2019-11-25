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
@Constraint(validatedBy = NotRevokedRefreshTokenValidator.class)
public @interface NotRevokedRefreshToken {
    String message() default "Invalid refresh token";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}