package com.ing.hub.entity;

import java.math.BigDecimal;

import com.ing.hub.enums.Currency;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@Entity
@Table(name = "wallet", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"user_id", "wallet_name"})
	})
public class Wallet {
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

	private String walletName;
	
	@Enumerated(EnumType.STRING)
	private Currency currency;
	
	private Boolean activeForShopping;
	
	private Boolean activeForWithdraw;
	
	private BigDecimal balance;
	
	private BigDecimal usableBalance;

}
