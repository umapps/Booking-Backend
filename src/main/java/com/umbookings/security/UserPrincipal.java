package com.umbookings.security;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umbookings.dto.request.UserSignUpDTO;

/**
 * @author Shrikar Kalagi
 *
 */

public class UserPrincipal implements UserDetails {
	private static final Logger LOG = LoggerFactory.getLogger(UserPrincipal.class);
	private static final long serialVersionUID = 1L;

	private Long id;

	@JsonIgnore
	private String emailId;

	@JsonIgnore
	private String password;

	private String firstName;

	private String lastName;

	private String mobileNumber;

	private Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(Long id, String firstName, String lastName, String emailId, String password,
			String mobileNumber, Collection<? extends GrantedAuthority> authorities) {

		this.id = id;

		this.emailId = emailId;

		this.password = password;

		this.firstName = firstName;

		this.lastName = lastName;

		this.authorities = authorities;

		this.mobileNumber = mobileNumber;
	}

	public static UserPrincipal create(UserSignUpDTO user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
		return new UserPrincipal(user.getId(), user.getFirstName(), user.getLastName(), user.getEmailId(),
				user.getPassword(), user.getMobileNumber(), authorities);
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getUsername() {
		return firstName + lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserPrincipal that = (UserPrincipal) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}
}
