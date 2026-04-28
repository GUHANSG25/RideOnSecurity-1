package com.rideon.security;

import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

	private final Key key;

	public JwtUtil(@Value("${jwt.secret}") String secret) {
		this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
	}

	public String generateToken(String mobile, String role) {
		return Jwts.builder()
				.setSubject(mobile)
				.claim("role", role) 
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) 																							// expiry
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String generateRefreshToken(String mobile) {
		return Jwts.builder()
				.setSubject(mobile)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) 
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String getMobileFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	public boolean isTokenExpired(String token) {
		return getClaimsFromToken(token)
				.getExpiration().before(new Date());
	}

	private Claims getClaimsFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public String getRoleFromToken(String token) {
		return (String) getClaimsFromToken(token)
				.get("role");
	}
	

}

