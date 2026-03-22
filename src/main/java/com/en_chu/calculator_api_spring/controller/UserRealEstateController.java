package com.en_chu.calculator_api_spring.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.en_chu.calculator_api_spring.model.UserRealEstateDto;
import com.en_chu.calculator_api_spring.service.UserRealEstateService;
import com.en_chu.calculator_api_spring.util.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user/real-estates")
@Tag(name = "User Real Estate API", description = "房地產資產管理")
@RequiredArgsConstructor
public class UserRealEstateController {

	private final UserRealEstateService userRealEstateService;

	@Operation(summary = "獲取所有房地產")
	@GetMapping
	public ResponseEntity<List<UserRealEstateDto>> getList() {
		String uid = SecurityUtils.getCurrentUserUid();
		return ResponseEntity.ok(userRealEstateService.getList(uid));
	}

	@Operation(summary = "新增房地產")
	@PostMapping
	public ResponseEntity<UserRealEstateDto> create(@RequestBody(required = false) UserRealEstateDto req) {
		String uid = SecurityUtils.getCurrentUserUid();
		if (req == null) {
			req = new UserRealEstateDto();
		}
		return ResponseEntity.ok(userRealEstateService.create(uid, req));
	}

	@Operation(summary = "更新房地產")
	@PutMapping("/{id}")
	public ResponseEntity<UserRealEstateDto> update(@PathVariable Long id, @RequestBody @Valid UserRealEstateDto req) {
		String uid = SecurityUtils.getCurrentUserUid();
		return ResponseEntity.ok(userRealEstateService.update(uid, id, req));
	}

	@Operation(summary = "刪除房地產")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		String uid = SecurityUtils.getCurrentUserUid();
		userRealEstateService.delete(uid, id);
		return ResponseEntity.noContent().build();
	}
}