package rocks.ifa.spring.service.impl;

import rocks.ifa.spring.entity.UserCreditCard;
import rocks.ifa.spring.mapper.UserCreditCardMapper;
import rocks.ifa.spring.dto.UserCreditCardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import rocks.ifa.spring.service.UserCreditCardService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCreditCardServiceImpl implements UserCreditCardService {

	private final UserCreditCardMapper userCreditCardMapper;

	@Override
	public List<UserCreditCardDto> getCards(String firebaseUid) {
		return userCreditCardMapper.selectByUid(firebaseUid).stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public UserCreditCardDto getCard(Long id, String firebaseUid) {
		UserCreditCard card = userCreditCardMapper.selectByIdAndUid(id, firebaseUid)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到該信用卡或無權限存取"));
		return convertToDto(card);
	}

	@Override
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

	@Override
	@Transactional
	public UserCreditCardDto updateCard(Long id, UserCreditCardDto dto, String firebaseUid) {
		UserCreditCard existing = userCreditCardMapper.selectByIdAndUid(id, firebaseUid)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "無法更新：找不到該信用卡或無權限"));

		existing.setName(dto.getName());
		existing.setDeductionAccount(dto.getDeductionAccount());
		existing.setUsageType(dto.getUsageType());
		existing.setStorageLocation(dto.getStorageLocation());
		existing.setAverageMonthlyExpense(dto.getAverageMonthlyExpense());

		userCreditCardMapper.update(existing);
		log.info("User [{}] updated credit card: ID={}", firebaseUid, id);

		return convertToDto(existing);
	}

	@Override
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
