package com.en_chu.calculator_api_spring.controller;

import com.en_chu.calculator_api_spring.model.UserCreditCardDto;
import com.en_chu.calculator_api_spring.service.UserCreditCardService;
import com.en_chu.calculator_api_spring.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/credit-cards")
@Tag(name = "User Credit Card API", description = "使用者信用卡管理")
@RequiredArgsConstructor
public class UserCreditCardController {

	private final UserCreditCardService userCreditCardService;

	@Operation(summary = "獲取所有信用卡")
	@GetMapping
	public ResponseEntity<List<UserCreditCardDto>> getList() {
		String uid = SecurityUtils.getCurrentUserUid();
		return ResponseEntity.ok(userCreditCardService.getCards(uid));
	}

	@Operation(summary = "獲取單張信用卡")
	@GetMapping("/{id}")
	public ResponseEntity<UserCreditCardDto> getOne(@PathVariable Long id) {
		String uid = SecurityUtils.getCurrentUserUid();
		return ResponseEntity.ok(userCreditCardService.getCard(id, uid));
	}

	@Operation(summary = "新增信用卡")
	@PostMapping
	public ResponseEntity<UserCreditCardDto> create() {
		String uid = SecurityUtils.getCurrentUserUid();
		return ResponseEntity.ok(userCreditCardService.createCard(uid));
	}

	@Operation(summary = "更新信用卡")
	@PutMapping("/{id}")
	public ResponseEntity<UserCreditCardDto> update(@PathVariable Long id, @RequestBody @Valid UserCreditCardDto req) {
		String uid = SecurityUtils.getCurrentUserUid();
		return ResponseEntity.ok(userCreditCardService.updateCard(id, req, uid));
	}

	@Operation(summary = "刪除信用卡")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		String uid = SecurityUtils.getCurrentUserUid();
		userCreditCardService.deleteCard(id, uid);
		return ResponseEntity.noContent().build();
	}
}
