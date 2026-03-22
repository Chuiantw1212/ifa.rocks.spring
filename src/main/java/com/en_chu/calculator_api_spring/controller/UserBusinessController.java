package com.en_chu.calculator_api_spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.en_chu.calculator_api_spring.model.UserBusinessDto;
import com.en_chu.calculator_api_spring.service.UserBusinessService;
import com.en_chu.calculator_api_spring.util.PageResponse;
import com.en_chu.calculator_api_spring.util.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user/businesses")
@Tag(name = "User Business API", description = "副業與創業項目管理")
@RequiredArgsConstructor
public class UserBusinessController {

	private final UserBusinessService userBusinessService;

	@Operation(summary = "獲取所有事業列表 (分頁)")
	@GetMapping
    public ResponseEntity<PageResponse<UserBusinessDto>> getList(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        String uid = SecurityUtils.getCurrentUserUid();
        
        if (currentPage < 1) currentPage = 1;
        if (pageSize < 1) pageSize = 10;
        if (pageSize > 100) {
            pageSize = 100;
        }

        return ResponseEntity.ok(userBusinessService.getList(uid, currentPage, pageSize));
    }

	@Operation(summary = "新增事業項目")
	@PostMapping
	public ResponseEntity<UserBusinessDto> create(@RequestBody @Valid UserBusinessDto req) {
		String uid = SecurityUtils.getCurrentUserUid();
		return ResponseEntity.ok(userBusinessService.create(uid, req));
	}

	@Operation(summary = "更新事業項目")
	@PutMapping("/{id}")
	public ResponseEntity<UserBusinessDto> update(@PathVariable Long id, @RequestBody @Valid UserBusinessDto req) {
		String uid = SecurityUtils.getCurrentUserUid();
		return ResponseEntity.ok(userBusinessService.update(uid, id, req));
	}

	@Operation(summary = "刪除事業項目")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		String uid = SecurityUtils.getCurrentUserUid();
		userBusinessService.delete(uid, id);
		return ResponseEntity.noContent().build();
	}
}