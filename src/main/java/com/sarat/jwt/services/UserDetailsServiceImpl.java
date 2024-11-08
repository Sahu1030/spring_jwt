package com.sarat.jwt.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sarat.jwt.model.Role;
import com.sarat.jwt.model.User;
import com.sarat.jwt.model.UserEntity;
import com.sarat.jwt.repo.UserDAO;
import com.sarat.jwt.repo.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user data from database or other source
    	Optional<User> userEntity = userDAO.getUserByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if(userEntity.isPresent()) {
        	User user= userEntity.get();
        	 return new User(user.getId(), user.getUsername(), user.getPassword());
        }
		return null;
       
    }

    private List<GrantedAuthority> getAuthorities(List<String> roles) {
        // Convert roles to authorities
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
