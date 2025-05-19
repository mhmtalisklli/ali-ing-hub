package com.ing.hub.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ing.hub.dto.WalletDto;
import com.ing.hub.entity.User;
import com.ing.hub.entity.Wallet;
import com.ing.hub.enums.Currency;
import com.ing.hub.exception.ApiResponse;
import com.ing.hub.repository.UserRepository;
import com.ing.hub.repository.WalletRepository;
import com.ing.hub.service.WalletService;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
	
	
	@Mock
    private WalletRepository walletRepository;

	
	@Mock
    private UserRepository userRepository;
	
	
    @InjectMocks
    private WalletService walletService;
    
    
    
    
    
    
    
    @Test
    void addWallet() {
        // Arrange: Gerekli DTO ve entity objelerini hazırla
        WalletDto walletDto = new WalletDto();
        walletDto.setWalletName("dsdds");
        walletDto.setCurrency(Currency.USD);
        walletDto.setCustomerTckn("11111111111");

        User user = new User();
        when(userRepository.findByTckn(anyString())).thenReturn(Optional.of(user));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: Test edilen metod çağrılır
        walletService.addWallet(walletDto);

        // Assert: Doğru metodun çağrıldığını kontrol et
        verify(walletRepository).save(any(Wallet.class));
    }
    
    
    
    
    
    
    
    
    

}
