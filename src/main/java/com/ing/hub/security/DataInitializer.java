package com.ing.hub.security;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ing.hub.entity.Role;
import com.ing.hub.entity.User;
import com.ing.hub.enums.UserRole;
import com.ing.hub.repository.RoleRepository;
import com.ing.hub.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
     CommandLineRunner initData(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder) {
        return args -> {
            if (roleRepo.findByRoleName(UserRole.ROLE_ADMIN) == null) {
                Role adminRole = new Role();
                adminRole.setRoleName(UserRole.ROLE_ADMIN);
                roleRepo.save(adminRole);
                
                Role employeeRole = new Role();
                employeeRole.setRoleName(UserRole.ROLE_EMPLOYEE);
                roleRepo.save(employeeRole);
                
                Role customerRole = new Role();
                customerRole.setRoleName(UserRole.ROLE_CUSTOMER);
                roleRepo.save(customerRole);

                User admin = new User();
                admin.setUsername("12345678910");
                admin.setName("ali");
                admin.setSurName("sakalli");
                admin.setTckn("12345678910");
                admin.setPassword(encoder.encode("1234"));
                admin.setRole(adminRole);
                userRepo.save(admin);
            }
        };
    }
}
