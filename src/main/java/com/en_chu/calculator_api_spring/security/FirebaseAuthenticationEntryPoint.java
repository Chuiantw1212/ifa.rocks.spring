package com.en_chu.calculator_api_spring.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FirebaseAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		// 1. 嘗試從 Filter 拿出剛剛存的具體錯誤原因
		String firebaseError = (String) request.getAttribute("firebase_exception");

		// 2. 決定最終錯誤訊息 (有具體原因就用具體的，沒有就用預設的)
		String finalMessage = (firebaseError != null) ? firebaseError : "存取被拒：請提供有效的 Authorization Token";

		// 3. 組裝 JSON
		Map<String, Object> body = new HashMap<>();
		body.put("status", 401);
		body.put("error", "Unauthorized");
		body.put("message", finalMessage);
		body.put("path", request.getRequestURI());

		// 4. 寫入 Response
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
	}
}