package rocks.ifa.spring.domain.clientCreditCards;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import rocks.ifa.spring.domain.entityBase.ClientBaseEntity;

import java.math.BigDecimal;

/**
 * 信用卡 (Credit Card)
 * 用於支出管理與現金流追蹤
 */
@Getter
@Setter
@Entity
@Table(name = "client_credit_cards")
@AttributeOverride(name = "id", column = @Column(name = "client_id"))
public class ClientCreditCardEntity extends ClientBaseEntity {

    /**
     * 卡片名稱 (e.g. 玉山 U Bear)
     */
    @Column(nullable = false)
    private String name;

    /**
     * 扣款帳戶 (e.g. 台新 Richart)
     */
    private String deductionAccount;

    /**
     * 用途分類 (e.g. online, daily)
     */
    private String usageType;

    /**
     * 存放位置: 錢包(wallet) / 數位(digital) / 抽屜(drawer)
     */
    private String storageLocation;

    /**
     * 平均月刷卡金額
     */
    @Column(precision = 19, scale = 4)
    private BigDecimal averageMonthlyExpense;
}
