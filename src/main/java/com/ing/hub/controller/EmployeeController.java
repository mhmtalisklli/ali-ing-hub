package com.ing.hub.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ing.hub.dto.DepositDto;
import com.ing.hub.dto.ResponseWalletDto;
import com.ing.hub.dto.TransactionDto;
import com.ing.hub.dto.TransactionUpdateDto;
import com.ing.hub.dto.UserDto;
import com.ing.hub.dto.WalletDto;
import com.ing.hub.dto.WithdrawDto;
import com.ing.hub.exception.ApiResponse;
import com.ing.hub.service.CustomUserDetailsService;
import com.ing.hub.service.TransactionService;
import com.ing.hub.service.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/employee", "/admin/employee"})
public class EmployeeController {
	
	@Autowired
	CustomUserDetailsService userService;
	
	@Autowired
	WalletService walletService;
	
	@Autowired
	TransactionService transactionService;
	
	
	
	   @PreAuthorize("hasRole('EMPLOYEE')")
	   @PostMapping("/add-WALLET")
	    public ResponseEntity<ApiResponse<Void>> addUserWallet(@Valid @RequestBody WalletDto wallet) {
		  
		   try {
		   return walletService.addWallet(wallet);
	   }catch (Exception e) {
				ApiResponse<Void> errorResponse = new ApiResponse<>(false,
						"Beklenmeyen bir hata oluştu, lütfen bilgileri kontrol edin", null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
			}  
	        
	    }
	   
	   
	   @PreAuthorize("hasRole('EMPLOYEE')")
	   @GetMapping("/list-wallets")
	    public List<ResponseWalletDto> getUserWallets(@RequestParam String tckn) {
		   
		  
		   return walletService.listMyWallets(tckn);
	        
	    }

	   
	   @PreAuthorize("hasRole('EMPLOYEE')")
	   @PostMapping("/add-user")
	    public  ResponseEntity<ApiResponse<Void>>  addUser(@Valid @RequestBody UserDto user) {
		  
		   try { 
		   return userService.addUser(user);
	   }catch (Exception e) {
			ApiResponse<Void> errorResponse = new ApiResponse<>(false,
					"Beklenmeyen bir hata oluştu, lütfen bilgileri kontrol edin", null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}  
	        
	    }
	   
	   
	   @PreAuthorize("hasRole('EMPLOYEE')")
	   @PostMapping("/make-deposit")
	    public ResponseEntity<ApiResponse<Void>> makeDeposit(@Valid @RequestBody DepositDto depositDto) {
		  
		   try { 
		  
		   return  walletService.makeDeposit(depositDto);
	   }catch (Exception e) {
				ApiResponse<Void> errorResponse = new ApiResponse<>(false,
						"Beklenmeyen bir hata oluştu, lütfen bilgileri kontrol edin", null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
			}  
	        
	    }
	   
	   @PreAuthorize("hasRole('EMPLOYEE')")
	   @PostMapping("/make-withdraw")
	    public ResponseEntity<ApiResponse<Void>> makeWithdraw(@Valid @RequestBody WithdrawDto withdrawDto) throws BadRequestException {
		   
		   try { 
		    return walletService.makeWithdraw(withdrawDto);
	   }catch (Exception e) {
				ApiResponse<Void> errorResponse = new ApiResponse<>(false,
						"Beklenmeyen bir hata oluştu, lütfen bilgileri kontrol edin", null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
			}  
	        
	    }
	   
	   @PreAuthorize("hasRole('EMPLOYEE')")
	   @PostMapping("/list-transactions")
	    public List<TransactionDto> listTransactions(@RequestParam Long walletId) throws BadRequestException {
		   
		  
		    return transactionService.listTransactions(walletId);
	        
	    }
	   
	   
	   @PreAuthorize("hasRole('EMPLOYEE')")
	   @PostMapping("/complite-transaction")
	    public ResponseEntity<ApiResponse<Void>> compliteransaction(TransactionUpdateDto transactionUpdateDto) throws BadRequestException {
		 
		   try { 
		     return transactionService.compliteTransaction(transactionUpdateDto);
	   }catch (Exception e) {
			ApiResponse<Void> errorResponse = new ApiResponse<>(false,
					"Beklenmeyen bir hata oluştu, lütfen bilgileri kontrol edin", null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}  
    }
	   
}
