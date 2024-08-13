package com.pms.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pms.model.JwtRequest;
import com.pms.model.JwtResponse;
import com.pms.model.User;
import com.pms.repository.UserRepository;
import com.pms.util.JwtUtil;

@Service
public class JwtService implements UserDetailsService {

	@Autowired
	@Lazy
	private UserRepository userRepository;

	@Autowired
	@Lazy
	private AuthenticationManager authenticationManager;

	@Autowired
	@Lazy
	private JwtUtil jwtUtil;

	public JwtResponse createJwtToken(JwtRequest jwtRequest) {
		String email = jwtRequest.getEmail();
		String password = jwtRequest.getPassword();

		if (email == null || email.isEmpty()) {
			throw new UsernameNotFoundException("User Name is not found...");
		}

		User user = findByEmail(email);

		if (user == null || !authentication(user.getEmail(), password)) {
			throw new UsernameNotFoundException("Bad Credencial userName and password");
		}

		final UserDetails userDetails = loadUserByUsername(email);
		String generateToken = jwtUtil.generateToken(userDetails);

		return new JwtResponse(generateToken);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmailIgnoreCase(email);
		if (user != null) {
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					getAuthorities(user));
		} else {
			throw new UsernameNotFoundException("User Name is Requied...");
		}
	}

	public Set<SimpleGrantedAuthority> getAuthorities(User user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
		});
		return authorities;
	}

	private boolean authentication(String email, String password) {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			return true;
		} catch (BadCredentialsException e) {
			// TODO: handle exception
			System.out.println("Bad Credential from the user name...");
			return false;
		}

	}

	private User findByEmail(String email) {

		User user = userRepository.findByEmailIgnoreCase(email);

		if (user == null) {
			user = userRepository.findByEmailIgnoreCase(email);
		}

		return user;
	}

}
