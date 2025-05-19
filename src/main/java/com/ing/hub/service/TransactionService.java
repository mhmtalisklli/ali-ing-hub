package com.ing.hub.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ing.hub.dto.TransactionDto;
import com.ing.hub.dto.TransactionUpdateDto;
import com.ing.hub.dto.WithdrawDto;
import com.ing.hub.entity.Transaction;
import com.ing.hub.entity.Wallet;
import com.ing.hub.enums.Status;
import com.ing.hub.enums.Type;
import com.ing.hub.exception.ApiResponse;
import com.ing.hub.repository.TransactionRepository;
import com.ing.hub.repository.WalletRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
	
	
	   @Autowired
	   private TransactionRepository transactionRepository;
	   
	   @Autowired
	   private WalletService walletService;
	   
	   @Autowired
	    private WalletRepository walletRepository;
	
	
	public List<TransactionDto> listTransactions(Long walletId) {

		List<Transaction> transactionEntityList = transactionRepository.findByWalletId(walletId);

		List<TransactionDto> dtoList = transactionEntityList.stream().map(TransactionDto::fromEntity)
				.collect(Collectors.toList());
		return dtoList;

	}
	
	
	
	@Transactional
	public ResponseEntity<ApiResponse<Void>> compliteTransaction(TransactionUpdateDto transactionUpdateDto) {
		ApiResponse<Void> response = null ;
		try {
			
		Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionUpdateDto.getTransactionId());
		
		 if (optionalTransaction.isPresent()) {
			 
			 Transaction transaction = optionalTransaction.get();
			 
				 if(transaction.getStatus().equals(Status.PENDING)  && transaction.isComplited() == false) {
					 
					 transaction.setComplited(true);
					 transactionRepository.save(transaction);
					 makeTransactionOperation(transaction , transactionUpdateDto.getStatus());
					 makeWalletOperation(transaction , transactionUpdateDto.getStatus());
					response = new ApiResponse<>(true, "İşlem Başarılı", null);
					
					 
				 }
			 
		 	}else {
		 		response = new ApiResponse<>(true, "Beklenmeyen bir hata oluştu, lütfen bilgileri kontrol edin", null);
		 	}
		 
		 }catch (Exception e) {
				ApiResponse<Void> errorResponse = new ApiResponse<>(false,
						"Beklenmeyen bir hata oluştu, lütfen bilgileri kontrol edin", null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
			}  
		 return ResponseEntity.ok(response);
		}
	
	
	
		public void makeTransactionOperation(Transaction transaction , Status status) {
			
			Transaction newTransaction = new Transaction();
			
			newTransaction.setAmount(transaction.getAmount());
			newTransaction.setComplited(true);
			newTransaction.setStatus(status);
			newTransaction.setOppositeParty(transaction.getOppositeParty());
			newTransaction.setOppositePartyType(transaction.getOppositePartyType());
			newTransaction.setType(transaction.getType());
			newTransaction.setWallet(transaction.getWallet());
			transactionRepository.save(newTransaction);
			
			
							
		}
		
		
	public void makeWalletOperation(Transaction transaction , Status status) {
			
			Wallet wallet = transaction.getWallet();
			
	      if(status.equals(Status.DENIED)) {

	    	  		if(transaction.getType().equals(Type.WITHDRAW)) {
	    	  			
	    	  			BigDecimal updatedBalance = wallet.getUsableBalance().add(transaction.getAmount());
	    	  			wallet.setUsableBalance(updatedBalance);
	    	  			walletRepository.save(wallet);
	    	  		}
	    	  		else {
	    	  			
	    	  			BigDecimal updatedBalance = wallet.getBalance().subtract(transaction.getAmount());
	    	  			wallet.setBalance(updatedBalance);
	    	  			walletRepository.save(wallet);
	    	  			
	    	  		}
	    	    }else {
	    	    	
	    	    	if(transaction.getType().equals(Type.WITHDRAW)) {
	    	    		WithdrawDto withdrawDto = new WithdrawDto();
	    	    		withdrawDto.setAmount(transaction.getAmount());
	    	    		withdrawDto.setOppositePartyType(transaction.getOppositePartyType());
	    	    		withdrawDto.setOppositeWalletId(transaction.getOppositeParty());
	    	    		withdrawDto.setWalletId(transaction.getWallet().getId());
	    	    		
	    	  			
	    	  			BigDecimal updatedBalance = wallet.getBalance().subtract(withdrawDto.getAmount());
	    	  			wallet.setBalance(updatedBalance);
	    	  			
	    	  			if((transaction.getOppositeParty()) != null && (transaction.getOppositeParty()!= 0)){
	    	  				walletService.makeOppositeTransaction(withdrawDto,transaction.getWallet().getCurrency());
	    	  			}
	    	  			
	    	  			walletRepository.save(wallet);
	    	  			
	    	  		}
	    	  		else {
	    	  			
	    	  			BigDecimal updatedBalance = wallet.getUsableBalance().add(transaction.getAmount());
	    	  			wallet.setUsableBalance(updatedBalance);
	    	  			walletRepository.save(wallet);
	    	  			
	    	  		}
	    	    	
	    	    	
	    	    	
	    	    }
			
			
							
		}

}
