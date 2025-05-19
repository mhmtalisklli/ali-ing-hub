package com.ing.hub.dto;

import java.math.BigDecimal;

import com.ing.hub.entity.Transaction;
import com.ing.hub.enums.OppositePartyType;
import com.ing.hub.enums.Status;
import com.ing.hub.enums.Type;

import lombok.Data;

@Data
public class TransactionDto {
   

	private Long transactionId;
	private BigDecimal amount;
	private Type type;
	private OppositePartyType oppositePartyType;
	private Long oppositeParty;
	private Status status;
	
	
	
	public static TransactionDto fromEntity(Transaction transaction) {
		
		TransactionDto transactionDto =new  TransactionDto();
		
		transactionDto.setTransactionId(transaction.getId());
		transactionDto.setAmount(transaction.getAmount());
		transactionDto.setType(transaction.getType());
		transactionDto.setOppositeParty(transaction.getOppositeParty());
		transactionDto.setOppositePartyType(transaction.getOppositePartyType());
		transactionDto.setStatus(transaction.getStatus());
		
		return transactionDto;
		
		} 
}



	