package com.example.fourchelin.common.filter;

import com.example.fourchelin.common.security.UserDetailsServiceImpl;
import com.example.fourchelin.domain.member.entity.Member;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class MemberAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false); // Session 정보를 가져오는데 없으면 세션을 새로 생성X
        if (session == null) {
            if(requestURI.startsWith("/api/searches")){ // /api/searches 로 시작하는 API는 익명 사용자도 접근이 가능
                chain.doFilter(request, response);

            } else {
                throw new MemberException("requestURI : " + requestURI + "/인증되지 않은 사용자 입니다.");
            }
        }
        Member member = (Member) session.getAttribute("LOGIN_MEMBER");
        setAuthentication(member.getId());
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
        String requestURI = request.getRequestURI();
        System.out.println("shouldNotFilter");
        return requestURI.startsWith("/api/members");
    }
}
