package com.sustainabilitytracker.sustainabilitytracker.services;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.RegisterUserRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.UserResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.Department;
import com.sustainabilitytracker.sustainabilitytracker.entities.User;
import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.BadRequestException;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.DuplicateResourceException;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.ResourceNotFoundException;
import com.sustainabilitytracker.sustainabilitytracker.mappers.UserMapper;
import com.sustainabilitytracker.sustainabilitytracker.repositories.DepartmentRepository;
import com.sustainabilitytracker.sustainabilitytracker.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse create(RegisterUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User with email '" + request.getEmail() + "' already exists");
        }

        User user = userMapper.toEntity(request);
        Role userRole = user.getRole();

        if (userRole == Role.DEPT_MANAGER || userRole == Role.EMPLOYEE) {
            if (user.getDepartment() == null || user.getDepartment().getId() == null) {
                throw new BadRequestException("Department is required for DEPT_MANAGER and EMPLOYEE roles");
            }

            Long departmentId = user.getDepartment().getId();
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Department with id " + departmentId + " does not exist"
                    ));

            user.setCompany(department.getCompany());
//            user.setDepartment(department);
        }
        else if (userRole == Role.ADMIN) {

            if (user.getCompany() != null) {
                throw new BadRequestException("ADMIN user should not have companyId");
            }
            if (user.getDepartment() != null) {
                throw new BadRequestException("ADMIN user should not have departmentId");
            }
        }

//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword(user.getPassword());

        User savedUser = userRepository.save(user);

        // Send welcome notification later

        return userMapper.toResponse(savedUser);
    }
}
