package rocks.ifa.spring.domain.clientCreditCards.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateCreditCardReq(
    UUID clientId,
    String name,
    String deductionAccount,
    String usageType,
    String storageLocation,
    BigDecimal averageMonthlyExpense
) {}
