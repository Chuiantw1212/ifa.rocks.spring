package rocks.ifa.spring.domain.clientCreditCards.dtos;

import java.math.BigDecimal;

public record UpdateCreditCardReq(
    String name,
    String deductionAccount,
    String usageType,
    String storageLocation,
    BigDecimal averageMonthlyExpense
) {}
