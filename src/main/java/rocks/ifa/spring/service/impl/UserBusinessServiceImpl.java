package rocks.ifa.spring.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import rocks.ifa.spring.entity.UserBusiness;
import rocks.ifa.spring.mapper.UserBusinessMapper;
import rocks.ifa.spring.model.UserBusinessDto;
import rocks.ifa.spring.util.PageResponse;
import rocks.ifa.spring.service.UserBusinessService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBusinessServiceImpl implements UserBusinessService {

	private final UserBusinessMapper mapper;

    @Override
    public PageResponse<UserBusinessDto> getList(String uid, int currentPage, int pageSize) {
        int offset = (currentPage - 1) * pageSize;
        List<UserBusiness> entities = mapper.selectPage(uid, pageSize, offset);
        long total = mapper.countByUid(uid);
	
        List<UserBusinessDto> dtos = entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageResponse<>(dtos, total, currentPage, pageSize);
    }

	@Override
	public UserBusinessDto getById(String uid, Long id) {
		UserBusiness entity = mapper.selectByIdAndUid(id, uid)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到該事業項目"));
		return convertToDto(entity);
	}

	@Override
	@Transactional
	public UserBusinessDto create(String uid, UserBusinessDto req) {
		UserBusiness entity = new UserBusiness();
		BeanUtils.copyProperties(req, entity);
		entity.setFirebaseUid(uid);
		mapper.insert(entity);
		log.info("Created business id={} for user={}", entity.getId(), uid);
		return convertToDto(entity);
	}

	@Override
	@Transactional
	public UserBusinessDto update(String uid, Long id, UserBusinessDto req) {
		UserBusiness updateEntity = new UserBusiness();
		BeanUtils.copyProperties(req, updateEntity);
		updateEntity.setId(id);
		updateEntity.setFirebaseUid(uid);
		int rows = mapper.update(updateEntity);

		if (rows == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "更新失敗：找不到該資料或無權限");
		}
		return getById(uid, id);
	}

	@Override
	@Transactional
	public void delete(String uid, Long id) {
		int rows = mapper.deleteByIdAndUid(id, uid);

		if (rows == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "刪除失敗：找不到該資料或無權限");
		}
	}

	private UserBusinessDto convertToDto(UserBusiness entity) {
		UserBusinessDto dto = new UserBusinessDto();
		BeanUtils.copyProperties(entity, dto);
		return dto;
	}
}
