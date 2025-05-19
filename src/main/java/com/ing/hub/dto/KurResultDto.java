package com.ing.hub.dto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

@Data
public class KurResultDto {
    private boolean success;
    private Map<String, BigDecimal> quotes;



    // Getter ve setter'lar, istersen Lombok da kullanabilirsin
}
