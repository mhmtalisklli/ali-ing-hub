package com.ing.hub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ing.hub.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
   
	List<Wallet> findByUserId(Long userId);
	List<Wallet> findByUserName(String userName);
	
}
