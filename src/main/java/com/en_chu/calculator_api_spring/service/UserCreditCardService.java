package com.en_chu.calculator_api_spring.service;

import com.en_chu.calculator_api_spring.entity.UserCreditCard;
import com.en_chu.calculator_api_spring.mapper.UserCreditCardMapper;
import com.en_chu.calculator_api_spring.model.UserCreditCardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCreditCardService {

	private final UserCreditCardMapper userCreditCardMapper;

	/**
	 * 取得使用者的所有信用卡列表
	 */
	public List<UserCreditCardDto> getCards(String firebaseUid) {
		return userCreditCardMapper.selectByUid(firebaseUid).stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}

	/**
	 * 取得單張信用卡詳情
	 */
	public UserCreditCardDto getCard(Long id, String firebaseUid) {
		UserCreditCard card = userCreditCardMapper.selectByIdAndUid(id, firebaseUid)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到該信用卡或無權限存取"));
		return convertToDto(card);
	}

	/**
	 * 新增信用卡 (後端生成預設值)
	 */
	@Transactional
	public UserCreditCardDto createCard(String firebaseUid) {
		UserCreditCard entity = new UserCreditCard();

		entity.setFirebaseUid(firebaseUid);
		entity.setName("新卡片");
		entity.setDeductionAccount("");
		entity.setUsageType("daily");
		entity.setStorageLocation("wallet");
		entity.setAverageMonthlyExpense(BigDecimal.ZERO);

		userCreditCardMapper.insert(entity);
		log.info("User [{}] initialized new credit card: ID={}", firebaseUid, entity.getId());

		return convertToDto(entity);
	}

	/**
	 * 更新信用卡
	 */
	@Transactional
	public UserCreditCardDto updateCard(Long id, UserCreditCardDto dto, String firebaseUid) {
		UserCreditCard existing = userCreditCardMapper.selectByIdAndUid(id, firebaseUid)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "無法更新：找不到該信用卡或無權限"));

		// 將 DTO 的屬性複製到從資料庫取出的 Entity 上
		existing.setName(dto.getName());
		existing.setDeductionAccount(dto.getDeductionAccount());
		existing.setUsageType(dto.getUsageType());
		existing.setStorageLocation(dto.getStorageLocation());
		existing.setAverageMonthlyExpense(dto.getAverageMonthlyExpense());

		userCreditCardMapper.update(existing);
		log.info("User [{}] updated credit card: ID={}", firebaseUid, id);

		return convertToDto(existing);
	}



	/**
	 * 刪除信用卡
	 */
	@Transactional
	public void deleteCard(Long id, String firebaseUid) {
		int rows = userCreditCardMapper.deleteByIdAndUid(id, firebaseUid);
		if (rows == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "刪除失敗：找不到該信用卡或無權限");
		}
		log.info("User [{}] deleted credit card: ID={}", firebaseUid, id);
	}

	private UserCreditCardDto convertToDto(UserCreditCard entity) {
		UserCreditCardDto dto = new UserCreditCardDto();
		BeanUtils.copyProperties(entity, dto);
		return dto;
	}
}
