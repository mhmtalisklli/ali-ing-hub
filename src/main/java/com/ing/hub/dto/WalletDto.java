package com.ing.hub.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ing.hub.entity.Wallet;
import com.ing.hub.enums.Currency;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletDto {
	
	
	
	private String customerTckn;
	
	@NotBlank(message = "Cüzdan adı boş olamaz")
	private String walletName;
	
	@NotNull
	private Currency currency;
	
	private Boolean activeForShopping;
	
	private Boolean activeForWithdraw;
	
	@DecimalMin(value = "0.00", inclusive = true, message = "Tutar 0'dan küçük olamaz")
    private BigDecimal balance;
	
	@DecimalMin(value = "0.00", inclusive = true, message = "Tutar 0'dan küçük olamaz")
	private BigDecimal usableBalance;
	
	
	
    public Wallet toEntity() {
    	
    	Wallet wallet = new Wallet();
    	
    	wallet.setWalletName(getWalletName());
    	wallet.setCurrency(getCurrency());
    	wallet.setActiveForShopping(getActiveForShopping());
    	wallet.setActiveForWithdraw(getActiveForWithdraw());
    	wallet.setBalance(getBalance());
    	wallet.setUsableBalance(getUsableBalance());
    	return wallet;
    	
    }

}
