package com.pms.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pms.service.JwtService;
import com.pms.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String header = request.getHeader("Authorization");
		String jwtToken = null;
		String userName = null;

		if (header != null && header.startsWith("Bearer ")) {
			jwtToken = header.substring(7);

			try {
				userName = jwtUtil.getUserNameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				// TODO: handle exception
				System.out.println("Pls Enter correct userName and password" + e);
			} catch (BadCredentialsException e) {
				// TODO: handle exception
				System.out.println("Pls bad credencial...");
			}
		}

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = jwtService.loadUserByUsername(userName);
			if (jwtUtil.validToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userName, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(userDetails);
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}

		filterChain.doFilter(request, response);

	}

}
