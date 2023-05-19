package com.vehicle.maintenance.service.impl;

import com.vehicle.maintenance.entity.Credential;
import com.vehicle.maintenance.exception.InvalidUserNameOrPasswordException;
import com.vehicle.maintenance.repository.CredentialRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private CredentialRepository credentialRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return credentialRepository.findUserCredentialByUsername(username).orElseThrow(
          () -> new UsernameNotFoundException("Username not found"));
    }

    public Credential getAuthenticatedUser(String username, String password)
      throws InvalidUserNameOrPasswordException {
        Optional<Credential> user = credentialRepository.findUserCredentialByUsername(username);

        if (user.isEmpty()) {
            throw new InvalidUserNameOrPasswordException();
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new InvalidUserNameOrPasswordException();
        }

        return user.get();
    }
}
