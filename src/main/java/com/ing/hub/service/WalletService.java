package com.ing.hub.service;



import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ing.hub.dto.DepositDto;
import com.ing.hub.dto.ResponseWalletDto;
import com.ing.hub.dto.WalletDto;
import com.ing.hub.dto.WithdrawDto;
import com.ing.hub.entity.Transaction;
import com.ing.hub.entity.User;
import com.ing.hub.entity.Wallet;
import com.ing.hub.enums.Currency;
import com.ing.hub.enums.Status;
import com.ing.hub.enums.Type;
import com.ing.hub.exception.ApiResponse;
import com.ing.hub.repository.TransactionRepository;
import com.ing.hub.repository.UserRepository;
import com.ing.hub.repository.WalletRepository;

@Service
public class WalletService {
	
	   @Autowired
	    private UserRepository userRepository;
	   
	   
	   @Autowired
	    private WalletRepository walletRepository;
	   
	   
	   @Autowired
	    private CurrencyService currencyService;
	   
	   @Autowired
	   private TransactionRepository transactionRepository;
	
	
		public ResponseEntity<ApiResponse<Void>> addWallet(WalletDto walletDto) {

			try {
				Wallet wallet = walletDto.toEntity();

				User user = userRepository.findByTckn(walletDto.getCustomerTckn())
						.orElseThrow(() -> new UsernameNotFoundException(
								"Tckn ile kullanıcı bulunamadı: " + walletDto.getCustomerTckn()));

				wallet.setUser(user);
				walletRepository.save(wallet);
				ApiResponse<Void> response = new ApiResponse<>(true, "Cüzdan başarıyla eklendi", null);
				return ResponseEntity.ok(response);
			} catch (Exception e) {
				ApiResponse<Void> errorResponse = new ApiResponse<>(false,
						"Beklenmeyen bir hata oluştu, lütfen bilgileri kontrol edin", null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
			}

		}

		public List<ResponseWalletDto> listMyWallets(String userName) {

			User user = userRepository.findByTckn(userName)
					.orElseThrow(() -> new UsernameNotFoundException("Tckn ile kullanıcı bulunamadı: " + userName));

			List<Wallet> walletEntities = walletRepository.findByUserId(user.getId());

			List<ResponseWalletDto> wallets = walletEntities.stream().map(ResponseWalletDto::fromEntity)
					.collect(Collectors.toList());
			return wallets;

		}

		@Transactional
		public ResponseEntity<ApiResponse<Void>> makeDeposit(DepositDto depositDto) {

			Transaction transaction = new Transaction();
			Wallet sourceWallet = walletRepository.findById(depositDto.getWalletId()).orElseThrow(
					() -> new UsernameNotFoundException("Id ile cüzdan bulunamadı: " + depositDto.getWalletId()));

			transaction.setWallet(sourceWallet);
			transaction.setAmount(depositDto.getAmount());
			transaction.setType(Type.DEPOSIT);
			transaction.setOppositePartyType(depositDto.getOppositePartyType());

			if ((depositDto.getAmount().compareTo(new BigDecimal("1000")) <= 0)) {

				transaction.setStatus(Status.APPROVED);
				BigDecimal updatedBalance = sourceWallet.getBalance().add(depositDto.getAmount());
				BigDecimal updatedUsableBalance = sourceWallet.getUsableBalance().add(depositDto.getAmount());
				sourceWallet.setBalance(updatedBalance);
				sourceWallet.setUsableBalance(updatedUsableBalance);
				walletRepository.save(sourceWallet);
				transaction.setComplited(true);
				transactionRepository.save(transaction);
				ApiResponse<Void> response = new ApiResponse<>(true, "İşlem Başarılı", null);
				return ResponseEntity.ok(response);
			}
			else if ((depositDto.getAmount().compareTo(new BigDecimal("1000")) > 0)) {

				transaction.setStatus(Status.PENDING);
				BigDecimal updateBalance = sourceWallet.getBalance().add(depositDto.getAmount());
				sourceWallet.setBalance(updateBalance);
				walletRepository.save(sourceWallet);
				transaction.setComplited(false);
				transactionRepository.save(transaction);

				ApiResponse<Void> response = new ApiResponse<>(true, "İşlem Başarılı, onay bekliyor", null);
				return ResponseEntity.ok(response);

			}
			else {

				transaction.setStatus(Status.DENIED);
				transaction.setComplited(true);
				transactionRepository.save(transaction);
				ApiResponse<Void> response = new ApiResponse<>(true,
						"Yapmak istediğiniz işlem için cüzdan durumunuz uygun değil", null);
				return ResponseEntity.ok(response);
			}

		}

		@Transactional
		public ResponseEntity<ApiResponse<Void>> makeWithdraw(WithdrawDto withdrawDto)
				throws BadRequestException {

			Transaction transaction = new Transaction();
			Wallet sourceWallet = walletRepository.findById(withdrawDto.getWalletId()).orElseThrow(
					() -> new UsernameNotFoundException("Id ile cüzdan bulunamadı: " + withdrawDto.getWalletId()));

			Boolean eligibleOperation = checkOperation(withdrawDto, sourceWallet);

			transaction.setWallet(sourceWallet);
			transaction.setAmount(withdrawDto.getAmount());
			transaction.setType(Type.WITHDRAW);
			transaction.setOppositePartyType(withdrawDto.getOppositePartyType());

			if ((sourceWallet.getUsableBalance().compareTo(withdrawDto.getAmount()) > 0)
					&& (withdrawDto.getAmount().compareTo(new BigDecimal("1000")) < 0) && eligibleOperation) {

				transaction.setStatus(Status.APPROVED);
				if (withdrawDto.getOppositeWalletId() != null && withdrawDto.getOppositeWalletId() != 0) {
					makeOppositeTransaction(withdrawDto, sourceWallet.getCurrency());
					transaction.setOppositeParty(withdrawDto.getOppositeWalletId());
				}

				BigDecimal updatedBalance = sourceWallet.getBalance().subtract(withdrawDto.getAmount());
				BigDecimal updatedUsableBalance = sourceWallet.getUsableBalance().subtract(withdrawDto.getAmount());

				sourceWallet.setBalance(updatedBalance);
				sourceWallet.setUsableBalance(updatedUsableBalance);
				transaction.setComplited(true);
				walletRepository.save(sourceWallet);
				transactionRepository.save(transaction);
				ApiResponse<Void> response = new ApiResponse<>(true, "İşlem Başarılı", null);
				return ResponseEntity.ok(response);
			} 
			else if ((sourceWallet.getUsableBalance().compareTo(withdrawDto.getAmount()) > 0)
					&& (withdrawDto.getAmount().compareTo(new BigDecimal("1000")) > 0) && eligibleOperation) {

				transaction.setStatus(Status.PENDING);
				if (withdrawDto.getOppositeWalletId() != null && withdrawDto.getOppositeWalletId() != 0) {
					transaction.setOppositeParty(withdrawDto.getOppositeWalletId());
				}
				BigDecimal updatedUsableBalance = sourceWallet.getUsableBalance().subtract(withdrawDto.getAmount());
				sourceWallet.setUsableBalance(updatedUsableBalance);
				walletRepository.save(sourceWallet);
				transaction.setComplited(false);
				transactionRepository.save(transaction);
				ApiResponse<Void> response = new ApiResponse<>(true, "İşlem Başarılı, onay bekliyor", null);
				return ResponseEntity.ok(response);

			} 
			else {

				transaction.setStatus(Status.DENIED);
				transaction.setComplited(true);
				transactionRepository.save(transaction);
				ApiResponse<Void> response = new ApiResponse<>(true,
						"Yapmak istediğiniz işlem için cüzdan durumunuz uygun değil", null);
				return ResponseEntity.ok(response);

			}

		}

		public boolean checkOperation(WithdrawDto withdrawDto, Wallet wallet) {
			boolean reuturnValue = true;
			switch (withdrawDto.getOppositePartyType()) {

			case IBAN:
				if (!wallet.getActiveForWithdraw()) {
					reuturnValue = false;
				}
				break;

			case PAYMENT:
				if (!wallet.getActiveForShopping()) {
					reuturnValue = false;
				}
				break;
			}
			return reuturnValue;

		}

		public void makeOppositeTransaction(WithdrawDto withdrawDto, Currency currency) {

			Wallet oppositeWallet = walletRepository.findById(withdrawDto.getOppositeWalletId())
					.orElseThrow(() -> new UsernameNotFoundException(
							"Id ile cüzdan bulunamadı: " + withdrawDto.getOppositeWalletId()));

			BigDecimal currentBalance = oppositeWallet.getBalance();
			BigDecimal currentUsableBalance = oppositeWallet.getUsableBalance();

			if (currency.equals(oppositeWallet.getCurrency())) {
				currentBalance = currentBalance.add(withdrawDto.getAmount());
				currentUsableBalance = currentUsableBalance.add(withdrawDto.getAmount());
			} else {

				BigDecimal convertedAmount = currencyService.getExchangeRates(currency.name(),
						oppositeWallet.getCurrency().name(), withdrawDto.getAmount());
				currentBalance = currentBalance.add(convertedAmount);
				currentUsableBalance = currentUsableBalance.add(convertedAmount);

			}

			oppositeWallet.setBalance(currentBalance);
			oppositeWallet.setUsableBalance(currentUsableBalance);
			walletRepository.save(oppositeWallet);
		}

		

	}
