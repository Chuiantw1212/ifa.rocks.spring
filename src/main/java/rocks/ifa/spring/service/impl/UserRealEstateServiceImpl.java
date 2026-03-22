package rocks.ifa.spring.service.impl;

import rocks.ifa.spring.entity.UserRealEstate;
import rocks.ifa.spring.mapper.UserRealEstateMapper;
import rocks.ifa.spring.model.UserRealEstateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.service.UserRealEstateService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRealEstateServiceImpl implements UserRealEstateService {

	private final UserRealEstateMapper mapper;

	@Override
	@Transactional
	public UserRealEstateDto create(String uid, UserRealEstateDto req) {
		UserRealEstate entity = new UserRealEstate();
		BeanUtils.copyProperties(req, entity);
		entity.setFirebaseUid(uid);

		if (entity.getName() == null || entity.getName().isBlank()) {
			entity.setName("新房產 (未命名)");
		}
		if (entity.getSize() == null) {
			entity.setSize(BigDecimal.ZERO);
		}
		if (entity.getPricePerPing() == null) {
			entity.setPricePerPing(BigDecimal.ZERO);
		}
		if (entity.getUsageType() == null) {
			entity.setUsageType("self");
		}

		calculateTotalPrice(entity);
		mapper.insert(entity);
		log.info("✅ [RealEstate] Created new record with ID: {} for user: {}", entity.getId(), uid);
		return convertToDto(entity);
	}

	@Override
	public List<UserRealEstateDto> getList(String uid) {
		return mapper.selectByUid(uid).stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public UserRealEstateDto getById(String uid, Long id) {
		UserRealEstate entity = mapper.selectByIdAndUid(id, uid)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到該房地產資料"));
		return convertToDto(entity);
	}

	@Override
	@Transactional
	public UserRealEstateDto update(String uid, Long id, UserRealEstateDto req) {
		mapper.selectByIdAndUid(id, uid)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到該房地產資料，或無權限修改"));

		UserRealEstate updateEntity = new UserRealEstate();
		BeanUtils.copyProperties(req, updateEntity);
		updateEntity.setId(id);
		updateEntity.setFirebaseUid(uid);

		calculateTotalPrice(updateEntity);
		mapper.update(updateEntity);
		log.info("✅ [RealEstate] Updated record with ID: {} for user: {}", id, uid);

		return getById(uid, id);
	}

	@Override
	@Transactional
	public void delete(String uid, Long id) {
		int rows = mapper.deleteByIdAndUid(id, uid);
		if (rows == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "刪除失敗：找不到該資料或無權限");
		}
		log.info("✅ [RealEstate] Deleted record with ID: {} for user: {}", id, uid);
	}

	private void calculateTotalPrice(UserRealEstate entity) {
		if (entity.getPricePerPing() != null && entity.getSize() != null) {
			BigDecimal price = entity.getPricePerPing();
			BigDecimal size = entity.getSize();
			BigDecimal total = price.multiply(size);
			entity.setTotalPrice(total);
		} else {
			entity.setTotalPrice(BigDecimal.ZERO);
		}
	}

	private UserRealEstateDto convertToDto(UserRealEstate entity) {
		UserRealEstateDto dto = new UserRealEstateDto();
		BeanUtils.copyProperties(entity, dto);
		return dto;
	}
}
