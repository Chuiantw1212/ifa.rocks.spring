package com.en_chu.calculator_api_spring.controller;

import com.en_chu.calculator_api_spring.model.*;
import com.en_chu.calculator_api_spring.service.*;
import com.en_chu.calculator_api_spring.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User API", description = "使用者核心資料管理")
@RequiredArgsConstructor // ✅ 使用 Lombok 實現建構函式注入
public class UserController {

    // ✅ 將所有依賴都改為 private final
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final UserCareerService userCareerService;
    private final UserLaborPensionService userLaborPensionService;
    private final UserLaborInsuranceService userLaborInsuranceService;
    private final UserTaxService userTaxService;

    @Operation(summary = "獲取當前使用者所有資料")
    @GetMapping("/me")
    public ResponseEntity<UserFullDataRes> getMe() {
        String uid = SecurityUtils.getCurrentUserUid();
        return ResponseEntity.ok(userService.getFullUserData(uid));
    }

    @Operation(summary = "更新使用者個人資料")
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody @Valid UserProfileUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        userProfileService.updateProfile(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新使用者職涯資料")
    @PutMapping("/career")
    public ResponseEntity<String> updateCareer(@RequestBody @Valid UserCareerUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        userCareerService.updateCareer(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新使用者勞退資料")
    @PutMapping("/labor-pension")
    public ResponseEntity<String> updateLaborPension(@RequestBody @Valid UserLaborPensionUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        userLaborPensionService.updateLaborPension(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新使用者勞保資料")
    @PutMapping("/labor-insurance")
    public ResponseEntity<String> updateLaborInsurance(@RequestBody @Valid UserLaborInsuranceUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        userLaborInsuranceService.updateLaborInsurance(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "更新使用者稅務資料")
    @PutMapping("/tax")
    public ResponseEntity<String> updateTax(@RequestBody @Valid UserTaxUpdateReq req) {
        String uid = SecurityUtils.getCurrentUserUid();
        userTaxService.updateTax(uid, req);
        return ResponseEntity.ok("更新成功");
    }

    @Operation(summary = "刪除當前使用者帳號")
    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        String uid = SecurityUtils.getCurrentUserUid();
        userService.deleteUser(uid);
        return ResponseEntity.noContent().build();
    }
}
