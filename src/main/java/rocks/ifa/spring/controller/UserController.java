package rocks.ifa.spring.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rocks.ifa.spring.model.dto.UserFullDataRes;
import rocks.ifa.spring.model.dto.UserProfileUpdateReq;
import rocks.ifa.spring.service.UserProfileService;
import rocks.ifa.spring.service.UserService;
import rocks.ifa.spring.util.SecurityUtils;

@RestController
public class UserController {

    private final UserService userService;
    private final UserProfileService userProfileService;

    public UserController(UserService userService, UserProfileService userProfileService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
    }

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
}