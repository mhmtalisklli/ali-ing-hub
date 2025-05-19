package com.ing.hub.dto;

import java.util.Arrays;
import java.util.Set;

import org.apache.coyote.BadRequestException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ing.hub.entity.User;
import com.ing.hub.enums.UserRole;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
	
	
	@NotBlank(message = "Ad boş olamaz")
    private String name;
	
	@NotBlank(message = "Soyad boş olamaz")
    private String surName;
    
    @Pattern(regexp = "\\d{11}", message = "Tckn 11 haneli olmalı ve rakamlardan oluşmalıdır")
    private String tckn;
	
    @NotNull
    private UserRole role;
    
    private Set<UserWalletDto> wallet;
    
    @NotBlank(message = "Parola boş olamaz")
    private String password;
    
    
    public void validateUserDto() throws BadRequestException {
    	if (role == null || Arrays.stream(UserRole.values()).noneMatch(r -> r.equals(role))) {
    	    throw new ValidationException("Geçerli bir rol giriniz");
    	}
    }
    
    
    public User toEntity() {
    	
    	User user = new User();
    	
    	user.setName(getName());
    	user.setSurName(getSurName());
    	user.setTckn(getTckn());
    	
    	//username olarak Tc kullanılacak
    	user.setUsername(getTckn());
    	
    	return user;
    	
    }
    
}
