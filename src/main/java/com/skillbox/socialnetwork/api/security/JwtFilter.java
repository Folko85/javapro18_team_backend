package com.skillbox.socialnetwork.api.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.StringUtils.hasText;

public class JwtFilter extends AbstractAuthenticationProcessingFilter {

    public static final String AUTHORIZATION_KEY = "Authorization";

    private final JwtProvider jwtProvider;

    private final UserDetailServiceImpl userDetailService;

    public JwtFilter(JwtProvider jwtProvider, UserDetailServiceImpl userDetailService) {
        super("/**");
        this.jwtProvider = jwtProvider;
        this.userDetailService = userDetailService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = getTokenFromRequest((HttpServletRequest) servletRequest);
            if (token != null && jwtProvider.validateToken(token)) {
                String userLogin = jwtProvider.getLoginFromToken(token);
                UserDetails userDetails = userDetailService.loadUserByUsername(userLogin);
                if (userDetails != null)
                    SecurityContextHolder.getContext()
                            .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (AuthenticationException ex) {
            unsuccessfulAuthentication((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, ex);
        }

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String token = httpServletRequest.getHeader(AUTHORIZATION);
        token = StringUtils.removeStart(token, "Bearer").trim();
        Authentication requestAuthentication = new UsernamePasswordAuthenticationToken(token, token);
        return getAuthenticationManager().authenticate(requestAuthentication);

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_KEY);
        if (hasText(token)) {
            return token;
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
