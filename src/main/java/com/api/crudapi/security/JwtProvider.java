package com.api.crudapi.security;

import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.api.crudapi.user.UserModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtProvider {

	private String SECRET_UNSAFE_KEY = "c2VjcmV0";

	public String sign(UserModel user) {
		var jwt = Jwts.builder().setSubject(user.getEmail()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_UNSAFE_KEY).compact();
		return jwt;
	}

	public Claims verify(String token) {
		var jwt = Jwts.parser().setSigningKey(SECRET_UNSAFE_KEY).parseClaimsJws(token).getBody();
		return jwt;
	}

	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = verify(token);

		return claimResolver.apply(claims);
	}
}
