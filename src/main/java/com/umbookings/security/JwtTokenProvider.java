package com.umbookings.security;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * @author Shrikar Kalagi
 *
 */
@Component
public class JwtTokenProvider {

	private static final Logger LOG = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationInMs}")
	private Long jwtExpirationInMs;

	public String generateToken(Authentication authentication) {
		UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
		return Jwts.builder().setSubject(Long.toString(userDetails.getId())).setIssuedAt(new Date())
				.setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

	public boolean validateToken(String authToken) {
		LOG.info("Enter validateToken() for authToken: {}", authToken);
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			LOG.info("Exit validateToken()");
			return true;
		} catch (io.jsonwebtoken.SignatureException ex) {
			LOG.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			LOG.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			LOG.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			LOG.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			LOG.error("JWT string is illegal");
		}
		return false;
	}
}