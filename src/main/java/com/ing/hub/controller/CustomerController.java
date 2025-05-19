package com.ing.hub.controller;

import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ing.hub.dto.DepositDto;
import com.ing.hub.dto.ResponseWalletDto;
import com.ing.hub.dto.TransactionDto;
import com.ing.hub.dto.UserWalletDto;
import com.ing.hub.dto.WalletDto;
import com.ing.hub.dto.WithdrawDto;
import com.ing.hub.entity.Wallet;
import com.ing.hub.exception.ApiResponse;
import com.ing.hub.repository.WalletRepository;
import com.ing.hub.service.CustomUserDetailsService;
import com.ing.hub.service.TransactionService;
import com.ing.hub.service.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/customer"})
public class CustomerController {
	
	@Autowired
	CustomUserDetailsService userService;
	
	@Autowired
	private WalletRepository walletRepository;
	 
	@Autowired
	WalletService walletService;
	
	@Autowired
	TransactionService transactionService;
	

		private UserDetails getCurrentUser() {
		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    if (auth == null || !auth.isAuthenticated()) {
		        throw new RuntimeException("Kullanıcı doğrulanmamış");
		    }
		    return (UserDetails) auth.getPrincipal();
		}
	
	   @PreAuthorize("hasRole('CUSTOMER')")
	   @PostMapping("/add-WALLET")
	    public ResponseEntity<ApiResponse<Void>> addUserWallet(@Valid @RequestBody UserWalletDto wallet) {
		   	  
		   try {
			   WalletDto walletDto = wallet.toWalletDto();
			   walletDto.setCustomerTckn(getCurrentUser().getUsername());
			  
			   return walletService.addWallet(walletDto);
		   }catch (Exception e) {
				ApiResponse<Void> errorResponse = new ApiResponse<>(false,
						"Beklenmeyen bir hata oluştu, lütfen bilgileri kontrol edin", null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
			}
		 
	        
	    }
	   
	   
	   @PreAuthorize("hasRole('CUSTOMER')")
	   @GetMapping("/list-wallets")
	    public List<ResponseWalletDto> getMyWallets() {
		   
	  
		   return walletService.listMyWallets(getCurrentUser().getUsername());
	        
	    }
	   
	   
	   @PreAuthorize("hasRole('CUSTOMER')")
	   @PostMapping("/make-deposit")
	    public ResponseEntity<ApiResponse<Void>> makeDeposit(@Valid @RequestBody DepositDto depositDto) {
		   
		   try {
			   
		   checkEligibleOwner(depositDto.getWalletId());
		   
		   return  walletService.makeDeposit(depositDto);
		   }catch (Exception e) {
				ApiResponse<Void> errorResponse = new ApiResponse<>(false,
						"Beklenmeyen bir hata oluştu, lütfen bilgileri kontrol edin", null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
			}  
	        
	    }
	   
	   @PreAuthorize("hasRole('CUSTOMER')")
	   @PostMapping("/make-withdraw")
	    public ResponseEntity<ApiResponse<Void>> makeWithdraw(@Valid @RequestBody WithdrawDto withdrawDto) throws BadRequestException {
		   
		   try {  
		    checkEligibleOwner(withdrawDto.getWalletId());
		    return walletService.makeWithdraw(withdrawDto);
	   }catch (Exception e) {
			ApiResponse<Void> errorResponse = new ApiResponse<>(false,
					"Beklenmeyen bir hata oluştu, lütfen bilgileri kontrol edin", null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	        
	    }
	   
	   @PreAuthorize("hasRole('CUSTOMER')")
	   @PostMapping("/list-transactions")
	    public List<TransactionDto> listTransactions(Long walletId) throws BadRequestException {
		   
		    checkEligibleOwner(walletId);
		    return transactionService.listTransactions(walletId);
	        
	    }
	   
	 
	   
	   
	   public boolean checkEligibleOwner(Long walletId) {
		   
          Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
		   
		   if (optionalWallet.isPresent()) {
			   Wallet wallet = optionalWallet.get();
			   List<ResponseWalletDto> ownerWallets= walletService.listMyWallets(getCurrentUser().getUsername());
			   
			   boolean isWalletOwnedByUser = ownerWallets.stream()
			            .anyMatch(w -> w.getWalletId().equals(wallet.getId()));
			   if(!isWalletOwnedByUser) {
				   throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bu cüzdan size ait değil.");

			   }
		   }
		   return true;
	   }
	   

}
