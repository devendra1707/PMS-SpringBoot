package com.pms.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;





@Component
public class JwtUtil {

	private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
	
	private static final String SECREATE_KEY = "yfuyfcuy@#$%87654@#$kjjkh vjh ";
	private static final int VALID_TOKEN = 60;

	public String getUserNameFromToken(String token) {
	
		log.info("This is The getUserNameFromToken method");
		return getClaimsFromToken(token, Claims::getSubject);
	}

	private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
	
		log.info("This is The getClaimsFromToken method");
		Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	public Claims getAllClaimsFromToken(String token) {
		
		log.info("This is The getAllClaimsFromToken method");
		return ((JwtParser) Jwts.parser().setSigningKey(SECREATE_KEY)).parseClaimsJws(token).getBody();
	}

	public boolean validToken(String token, UserDetails userDetails) {
		log.info("This is The validToken method");
		String userName = getUserNameFromToken(token);
		return (userName.equals(userDetails.getUsername()) && !isExpiredDate(token));
	}

	private boolean isExpiredDate(String token) {
	
		log.info("This is The isExpiredDate method");
		final Date tokenExpiredDate = expiredDateFromToken(token);
		return tokenExpiredDate.before(new Date());
	}

	private Date expiredDateFromToken(String token) {
		
		log.info("This is The expiredDateFromToken method");
		return getClaimsFromToken(token, Claims::getExpiration);
	}
	
	
	public String generateToken(UserDetails userDetails) {
		log.info("This is The generateToken method");
		Map<String, Object> claims = new HashMap<String, Object>();
		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + VALID_TOKEN * 10000000))
				.signWith(SignatureAlgorithm.HS512, SECREATE_KEY).compact();
	}

}
