package org.rebit.auth.validation;

import org.rebit.auth.validation.constraint.Ascii;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AsciiOnlyConstraintValidator implements ConstraintValidator<Ascii, String> {

@Override
public boolean isValid(String value, ConstraintValidatorContext context) {

			if (value != null) {
				return value.matches("\\A\\p{ASCII}*\\z");
			} else {
				return true;
			}
		}


}
