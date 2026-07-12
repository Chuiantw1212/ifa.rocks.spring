package rocks.ifa.spring.domain.clientCreditCards.dtos;

import java.math.BigDecimal;

public record CreateCreditCardReq(
    String name,
    String deductionAccount,
    String usageType,
    String storageLocation,
    BigDecimal averageMonthlyExpense
) {}
