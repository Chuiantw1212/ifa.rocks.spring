package com.en_chu.calculator_api_spring.security;

import com.en_chu.calculator_api_spring.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
@RequiredArgsConstructor
public class FirebaseTokenFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String EXCEPTION_ATTR_NAME = "firebase_exception";

    private final UserService userService; // ✅ 注入 UserService

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(AUTH_HEADER);

        if (!StringUtils.hasText(header) || !header.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(TOKEN_PREFIX.length());

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getUid();

            // ✅ 核心修正：在認證成功後，立刻同步使用者資料
            // 這會確保在執行任何後續操作之前，user_profiles 表中一定存在該使用者的紀錄。
            userService.syncUser(uid);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    uid, decodedToken, new ArrayList<>());

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (FirebaseAuthException e) {
            log.warn("⚠️ [Auth Fail] Firebase Token is invalid: {} (Code: {})", e.getMessage(), e.getAuthErrorCode());
            request.setAttribute(EXCEPTION_ATTR_NAME, "Token validation failed: " + e.getMessage());
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            log.error("💥 [System Error] An unexpected error occurred in the Auth Filter", e);
            request.setAttribute(EXCEPTION_ATTR_NAME, "Internal system error during authentication.");
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
