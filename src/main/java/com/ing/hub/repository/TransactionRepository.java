package com.ing.hub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ing.hub.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	List<Transaction> findByWalletId(Long walletId);
   
}
