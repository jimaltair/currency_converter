package ru.jimaltair.currencyconverter.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotFutureDate {

    String message() default "future date is not allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
