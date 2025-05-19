package com.ing.hub.test.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ing.hub.dto.UserDto;
import com.ing.hub.dto.UserWalletDto;
import com.ing.hub.dto.WalletDto;
import com.ing.hub.entity.Role;
import com.ing.hub.entity.User;
import com.ing.hub.entity.Wallet;
import com.ing.hub.enums.Currency;
import com.ing.hub.enums.UserRole;
import com.ing.hub.repository.RoleRepository;
import com.ing.hub.repository.UserRepository;
import com.ing.hub.repository.WalletRepository;
import com.ing.hub.service.CustomUserDetailsService;
import com.ing.hub.service.WalletService;


@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
	
	
	
	
	@Mock
    private WalletRepository walletRepository;

	
	@Mock
    private UserRepository userRepository;
	
	@Mock
    private RoleRepository roleRepository;
	
	@Mock
	PasswordEncoder encoder;
	
    
    @InjectMocks
    CustomUserDetailsService   customUserDetailsService;
    
    

    
    
    @Test
    void addUser() {
       

        Role role = new Role();
        role.setRoleName(UserRole.ROLE_CUSTOMER);
        UserDto userDto = new UserDto();
   
        userDto.setName("aa");
        userDto.setPassword("1");
        userDto.setSurName("a");
        userDto.setRole(UserRole.ROLE_CUSTOMER);
        userDto.setTckn("1111111111");
     
        when(roleRepository.findByRoleName(userDto.getRole())).thenReturn(role);

        when(encoder.encode(userDto.getPassword())).thenReturn("pass");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
       
        customUserDetailsService.addUser(userDto);

   
        verify(userRepository).save(any(User.class)); 
    }
    

}
