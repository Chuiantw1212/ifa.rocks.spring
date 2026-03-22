package com.en_chu.calculator_api_spring.controller;

import com.en_chu.calculator_api_spring.model.UserPortfolioCreateReq;
import com.en_chu.calculator_api_spring.model.UserPortfolioDto;
import com.en_chu.calculator_api_spring.model.UserPortfolioUpdateReq;
import com.en_chu.calculator_api_spring.service.UserPortfolioService;
import com.en_chu.calculator_api_spring.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/portfolios")
@Tag(name = "User Portfolio API", description = "投資組合管理 (股票/ETF/基金)")
@RequiredArgsConstructor
public class UserPortfolioController {

	private final UserPortfolioService userPortfolioService;

	@Operation(summary = "獲取所有投資組合")
	@GetMapping
	public ResponseEntity<List<UserPortfolioDto>> getList() {
		String uid = SecurityUtils.getCurrentUserUid();
		return ResponseEntity.ok(userPortfolioService.getList(uid));
	}

	@Operation(summary = "新增一筆投資組合")
	@PostMapping
	public ResponseEntity<UserPortfolioDto> create(@RequestBody @Valid UserPortfolioCreateReq req) {
		String uid = SecurityUtils.getCurrentUserUid();
		return ResponseEntity.ok(userPortfolioService.create(uid, req));
	}

	@Operation(summary = "更新一筆投資組合")
	@PutMapping("/{id}")
	public ResponseEntity<UserPortfolioDto> update(@PathVariable Long id, @RequestBody @Valid UserPortfolioUpdateReq req) {
		String uid = SecurityUtils.getCurrentUserUid();
		return ResponseEntity.ok(userPortfolioService.update(uid, id, req));
	}

	@Operation(summary = "刪除一筆投資組合")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		String uid = SecurityUtils.getCurrentUserUid();
		userPortfolioService.delete(uid, id);
		return ResponseEntity.noContent().build();
	}
}
