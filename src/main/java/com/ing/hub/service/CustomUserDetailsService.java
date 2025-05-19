package com.ing.hub.service;

import java.util.Collections;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ing.hub.dto.UserDto;
import com.ing.hub.dto.UserWalletDto;
import com.ing.hub.dto.WalletDto;
import com.ing.hub.entity.User;
import com.ing.hub.exception.ApiResponse;
import com.ing.hub.entity.Role;
import com.ing.hub.repository.RoleRepository;
import com.ing.hub.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    WalletService walletService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));

        return new org.springframework.security.core.userdetails.User(
        	    user.getUsername(),
        	    user.getPassword(),
        	    Collections.singletonList(
        	        new SimpleGrantedAuthority(user.getRole().getRoleName().toString())
        	    )
        	);
    }
    
    @Transactional
    public ResponseEntity<ApiResponse<Void>> addUser(UserDto userDto) {
    	
    	try {
    	   User user = userDto.toEntity();
    
    		Role role = roleRepository.findByRoleName(userDto.getRole());
    		
	    	user.setRole(role); 	
	    	user.setPassword(encoder.encode(userDto.getPassword()));
  
    	userRepository.save(user);
    	 if (userDto.getWallet() != null) {
	    	for(UserWalletDto wallet : userDto.getWallet()) {
	    		
	    		WalletDto walletDto = new WalletDto();
	    		walletDto = wallet.toWalletDto();
	    		walletDto.setCustomerTckn(user.getTckn());
	    		walletService.addWallet(walletDto);
	    		
	    	}
    	 }
    	 	ApiResponse<Void> response = new ApiResponse<>(true, "Kullanıcı başarıyla eklendi", null);
            return ResponseEntity.ok(response);
    	}catch(Exception e) {
    		ApiResponse<Void> errorResponse = new ApiResponse<>(false, "Beklenmeyen bir hata oluştu, lütfen bilgileri kontrol edin", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    		
    	}
    }
}
