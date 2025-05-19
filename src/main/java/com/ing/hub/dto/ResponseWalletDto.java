package com.ing.hub.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ing.hub.entity.Wallet;
import com.ing.hub.enums.Currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseWalletDto {
	
		
	private Long walletId;
	private String walletName;
	
	private Currency currency;
	
	private Boolean activeForShopping;
	
	private Boolean activeForWithdraw;
	
	private BigDecimal balance;
		
    private BigDecimal usableBalance;
	
	
	
    public static ResponseWalletDto fromEntity(Wallet wallet) {
    	
    	ResponseWalletDto responseWalletDto = new ResponseWalletDto();
    	
    	
    	responseWalletDto.setWalletId(wallet.getId());
    	responseWalletDto.setWalletName(wallet.getWalletName());
    	responseWalletDto.setCurrency(wallet.getCurrency());
    	responseWalletDto.setActiveForShopping(wallet.getActiveForShopping());
    	responseWalletDto.setActiveForWithdraw(wallet.getActiveForWithdraw());
    	responseWalletDto.setBalance(wallet.getBalance());
    	responseWalletDto.setUsableBalance(wallet.getUsableBalance());
    	return responseWalletDto;
    	
    }
    




}
