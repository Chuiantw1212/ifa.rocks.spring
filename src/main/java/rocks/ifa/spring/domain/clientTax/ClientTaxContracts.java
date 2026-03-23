package rocks.ifa.spring.domain.clientTax;

import java.math.BigDecimal;

public interface ClientTaxContracts {

    record TaxRes(
        BigDecimal estimatedOtherIncome
    ) {}

    record UpdateTaxReq(
        BigDecimal estimatedOtherIncome
    ) {}
}
