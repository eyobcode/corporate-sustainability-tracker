package com.sustainabilitytracker.sustainabilitytracker.validation;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.RegisterUserRequest;
import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegisterUserValidator implements ConstraintValidator<ValidRegisterUser, RegisterUserRequest> {

    @Override
    public boolean isValid(RegisterUserRequest request, ConstraintValidatorContext context) {

        if (request.getRole() == null) return false;

        if (request.getRole() == Role.ADMIN) {
            return request.getCompanyId() == null && request.getDepartmentId() == null;
        }

        if (request.getCompanyId() == null) return false;

        if (request.getRole() == Role.EMPLOYEE) {
            return request.getDepartmentId() != null;
        }

        return true;
    }
}
