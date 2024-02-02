package com.umc.networkingService.global.common.validation.validator;

import com.umc.networkingService.global.common.validation.annotation.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {

    private Class<? extends Enum<?>> enumClass;


    @Override
    public void initialize(ValidEnum constraint) {
        this.enumClass = constraint.enumClass();
    }

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        Object[] enumValues = this.enumClass.getEnumConstants();

        if (enumValues != null) {
            for(Object enumValue: enumValues) {
                if(value.equals(enumValue))
                    return true;
            }
        }
        return false;
    }
}

