package com.en_chu.calculator_api_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Represents a user's credit card.
 * Inherits id, firebaseUid, createdAt, and updatedAt from UserBaseEntity.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserCreditCard extends UserBaseEntity {

    /**
     * The name of the card (e.g., "Chase Sapphire Preferred").
     */
    private String name;

    /**
     * The bank account from which the card is paid.
     */
    private String deductionAccount;

    /**
     * A code for categorizing card usage (e.g., "online", "daily", "travel").
     */
    private String usageType;

    /**
     * The physical or digital location of the card (e.g., "wallet", "digital", "drawer").
     */
    private String storageLocation;

    /**
     * The average monthly expense charged to this card.
     */
    private BigDecimal averageMonthlyExpense;
}
