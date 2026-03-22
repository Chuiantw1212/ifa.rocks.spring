package com.en_chu.calculator_api_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserProfile extends UserBaseEntity {

    private LocalDate birthDate;
    private String gender;
    private Integer currentAge;
    private Integer lifeExpectancy;
    private Integer marriageYear; // ✅ Reverted to Integer
    private String careerInsuranceType;
    private String biography;
}
