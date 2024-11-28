package com.example.fourchelin.common.filter;

import com.example.fourchelin.common.security.UserDetailsServiceImpl;
import com.example.fourchelin.common.service.CacheService;
import com.example.fourchelin.domain.member.exception.MemberException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class MemberAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final CacheService cacheService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false); // Session 정보를 가져오는데 없으면 세션을 새로 생성X

        if (session == null) {
            if(requestURI.startsWith("/api/searches")){ // /api/searches 로 시작하는 API는 익명 사용자도 접근이 가능
                chain.doFilter(request, response);
                return;
            } else {
                throw new MemberException("requestURI : " + requestURI + "/인증되지 않은 사용자 입니다.");
            }
        }

        // 클라이언트의 세션 정보가 캐시 저장소에 저장되어있는지 확인
        Long memberId = cacheService.getCacheData("member", session.getId())
                        .orElseThrow(() -> new MemberException("로그아웃 되었습니다. 재로그인 해주세요."));

        setAuthentication(memberId);

        chain.doFilter(request, response);
    }

    private void setAuthentication(Long memberId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(memberId);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(Long memberId) {
        UserDetails userDetails = userDetailsService.loadUserByMemberId(memberId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        List<String> excludedPaths = List.of(
                "/api/members/signup",
                "/api/members/login"
        );

        return excludedPaths.stream()
                .anyMatch(path -> StringUtils.startsWithIgnoreCase(request.getRequestURI(), path));

    }
}
