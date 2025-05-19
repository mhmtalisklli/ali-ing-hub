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
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(adminRole);
                userRepo.save(admin);

                System.out.println("Varsayılan admin eklendi: admin / admin123");
            }
        };
    }
}
