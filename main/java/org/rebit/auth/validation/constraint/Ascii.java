package org.rebit.auth.validation.constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.rebit.auth.validation.AsciiOnlyConstraintValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = AsciiOnlyConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ascii {

		String message() default "{characters.asciionly}";

		Class<?>[] groups() default {};

		Class<? extends Payload>[] payload() default {};
	}

