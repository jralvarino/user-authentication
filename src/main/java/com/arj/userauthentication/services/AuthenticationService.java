package com.arj.userauthentication.services;

import com.arj.userauthentication.entities.UserEntity;
import com.arj.userauthentication.repositories.UserRepository;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

  private UserRepository userRepository;

  public AuthenticationService(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByEmail(email);

    if(user == null){
      throw new UsernameNotFoundException("User not found");
    }

    List<GrantedAuthority> authorityList;

    if(user.isAdmin()){
      authorityList = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
    }else{
      authorityList = AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    return new User(user.getEmail(), user.getPassword(), authorityList);
  }
}
