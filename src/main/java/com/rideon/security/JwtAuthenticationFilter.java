package com.rideon.security;

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rideon.entity.User;
import com.rideon.repository.RevokedTokenRepository;
import com.rideon.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RevokedTokenRepository revokedTokenRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String token = request.getHeader("Authorization");

		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7); 

			if (jwtUtil.isTokenExpired(token)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Invalid or expired token.");
				return;
			}

			if (revokedTokenRepository.findById(token).isPresent()) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Token has been revoked.");
				return;
			}

			String mobile = jwtUtil.getMobileFromToken(token);
			String role = jwtUtil.getRoleFromToken(token);

			Optional<User> user = userRepository.findByMobile(mobile);

			if (user.isPresent()) {
				List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

				SecurityContextHolder.getContext()
						.setAuthentication(new UsernamePasswordAuthenticationToken(mobile, null, authorities));

				System.out.println("Authorities: " + authorities);
			}
		}

		chain.doFilter(request, response);
	}
	
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//
//	}

}
