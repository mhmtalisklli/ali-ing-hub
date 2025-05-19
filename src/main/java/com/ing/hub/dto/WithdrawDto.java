package com.ing.hub.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ing.hub.enums.OppositePartyType;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawDto {
	
	
	private Long walletId;
	private Long oppositeWalletId;
	
	@DecimalMin(value = "0.01", inclusive = true, message = "Tutar 0'dan büyük olmalıdır")
	private BigDecimal amount;
	private OppositePartyType oppositePartyType;
	
	
}
