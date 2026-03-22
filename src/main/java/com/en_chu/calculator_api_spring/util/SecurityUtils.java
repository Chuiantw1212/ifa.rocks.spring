package com.en_chu.calculator_api_spring.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class SecurityUtils {

	/**
	 * 取得目前登入使用者的 Firebase UID 如果未登入或 Context 為空，直接拋出 401 Unauthorized 例外
	 */
	public static String getCurrentUserUid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 1. 檢查是否已驗證
		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getPrincipal())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
		}

		// 2. 嘗試取得 UID (根據你的 Filter 邏輯，Principal 就是 UID String)
		Object principal = authentication.getPrincipal();
		if (principal instanceof String) {
			return (String) principal;
		}

		// 3. 如果 Principal 格式不對 (防呆)
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid authentication principal type");
	}
}