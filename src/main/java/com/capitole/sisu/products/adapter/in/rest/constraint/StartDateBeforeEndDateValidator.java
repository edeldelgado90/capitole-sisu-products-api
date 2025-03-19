package com.capitole.sisu.products.adapter.in.rest.constraint;

import com.capitole.sisu.products.adapter.in.rest.dto.PriceDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartDateBeforeEndDateValidator
        implements ConstraintValidator<StartDateBeforeEndDate, PriceDTO> {

    @Override
    public boolean isValid(PriceDTO price, ConstraintValidatorContext context) {
        if (price.getStartDate() == null || price.getEndDate() == null) {
            return true;
        }
        return price.getStartDate().isBefore(price.getEndDate());
    }
}
