package com.sky.validiton;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName EnumValuValidation
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/1 12:58
 * @Version 1.0
 **/
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {
    private String[] strValues;
    private int[] intValues;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        strValues = constraintAnnotation.strValues();
        intValues = constraintAnnotation.intValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value instanceof String) {
            for (String s : strValues) {
                if (s.equals(value)) {
                    return true;
                }
            }
        } else if (value instanceof Integer) {
            for (int s : intValues) {
                if (s == (Integer) value) {
                    return true;
                }
            }
        }
        return false;
    }
}
