package com.ing.hub.entity;

import java.math.BigDecimal;

import com.ing.hub.enums.OppositePartyType;
import com.ing.hub.enums.Status;
import com.ing.hub.enums.Type;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "transaction")
public class Transaction {
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	private Type type;
	
	@Enumerated(EnumType.STRING)
	private OppositePartyType oppositePartyType;
	
	private Long oppositeParty;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	private boolean complited;
}
