package com.ing.hub.dto;

import com.ing.hub.enums.Status;

import lombok.Data;

@Data
public class TransactionUpdateDto {
   

	private Long transactionId;
	private Status status;

}



	