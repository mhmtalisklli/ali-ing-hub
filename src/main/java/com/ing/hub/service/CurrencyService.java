package com.ing.hub.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ing.hub.dto.KurResultDto;

@Service
public class CurrencyService {
	
	
	  private final RestTemplate restTemplate = new RestTemplate();

	    public BigDecimal getExchangeRates(String fromRate,String toRate ,BigDecimal amount ) {
	        
	    	String url = UriComponentsBuilder.fromHttpUrl("https://api.exchangerate.host/live")
	    	        .queryParam("access_key", "9f9521e6bf006e21f6a5958587e98934")
	    	        .queryParam("currencies", fromRate + "," + toRate)
	    	        .toUriString();
	        
	        RestTemplate restTemplate = new RestTemplate();
	        
	        KurResultDto response = restTemplate.getForObject(url, KurResultDto.class);
	        
	        	  
	        	  if(!fromRate.equals("USD")) {
	        	  
	        	  if(fromRate.equals("EUR") && toRate.equals("USD")) {
	        		  BigDecimal resultRate = response.getQuotes().get(toRate+fromRate);
	        		  return amount.multiply(resultRate);
	        	  }
	        	  else  if(fromRate.equals("TRY") && toRate.equals("USD")) {
	        		  BigDecimal resultRate = response.getQuotes().get(toRate+fromRate);
	        		  return amount.divide(resultRate, 8, RoundingMode.HALF_UP);
	        		  }
	        		 
	        	  else  if(fromRate.equals("TRY") && toRate.equals("EUR")) {
		        	  BigDecimal resultRate1 = response.getQuotes().get("USD"+fromRate);
		        	  BigDecimal resultRate2 = response.getQuotes().get("USD"+toRate);
		        	  
		        	  BigDecimal usdAmount = amount.divide(resultRate1, 8, RoundingMode.HALF_UP);
		        	  BigDecimal eurAmount = usdAmount.multiply(resultRate2);
		        	  
		        	  return eurAmount;  
		        	  }
	        	  
	        	  else  if(fromRate.equals("EUR") && toRate.equals("TRY")) {
	        		  BigDecimal resultRate1 = response.getQuotes().get("USD"+fromRate);
		        	  BigDecimal resultRate2 = response.getQuotes().get("USD"+toRate);
		        	  
		        
		        	  BigDecimal usdAmount = amount.divide(resultRate1, 8, RoundingMode.HALF_UP);
		        	  BigDecimal tryAmount = usdAmount.multiply(resultRate2);
		        	  
		        	  return tryAmount;  
		        	  }
	        	  
	                  
	        	  }		
	        
				        BigDecimal resultRate = response.getQuotes().get(fromRate+toRate);
				        return amount.multiply(resultRate);
				        
	    }
}
