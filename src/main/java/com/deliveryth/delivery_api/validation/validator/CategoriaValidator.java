package com.deliveryth.delivery_api.validation.validator;

import com.deliveryth.delivery_api.enums.CategoriaRestaurante;
import com.deliveryth.delivery_api.validation.CategoriaValida;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoriaValidator implements ConstraintValidator<CategoriaValida, String> {
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        if (value == null || value.isBlank()) {
            return false;
        }
        try{
            CategoriaRestaurante.valueOf(value.toUpperCase());
            return true;
        }catch(IllegalArgumentException e){
            return false;
        }
    }
}
