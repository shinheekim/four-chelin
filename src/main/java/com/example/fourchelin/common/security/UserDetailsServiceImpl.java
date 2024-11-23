package com.example.fourchelin.common.security;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() ->
                new NullPointerException("Not Found User")
        );

        return new UserDetailsImpl(member);
    }


}
