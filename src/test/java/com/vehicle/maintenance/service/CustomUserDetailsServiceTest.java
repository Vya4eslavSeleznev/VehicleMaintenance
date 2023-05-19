package com.vehicle.maintenance.service;

import com.vehicle.maintenance.entity.Credential;
import com.vehicle.maintenance.exception.InvalidUserNameOrPasswordException;
import com.vehicle.maintenance.model.Role;
import com.vehicle.maintenance.repository.CredentialRepository;
import com.vehicle.maintenance.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private String username;
    private String password;
    private Credential expectedUC;
    private Optional<Credential> optionalUC;

    @BeforeEach
    public void init() {
        customUserDetailsService = new CustomUserDetailsService(credentialRepository, passwordEncoder);
        username = "username";
        password = "pwd";
        expectedUC = new Credential(Role.USER, password, username);
        optionalUC = Optional.of(expectedUC);
    }

    @Test
    public void should_load_user_by_username_returned_user_details() {
        when(credentialRepository.findUserCredentialByUsername(username)).thenReturn(optionalUC);

        UserDetails actualUD = customUserDetailsService.loadUserByUsername(username);

        verify(credentialRepository, times(1)).findUserCredentialByUsername(username);

        assertEquals(expectedUC, actualUD);
    }

    @Test
    public void should_not_load_user_by_username_exception() {
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(username));
    }

    @Test
    public void should_get_authenticated_user_returned_user_credential() throws InvalidUserNameOrPasswordException {
        when(credentialRepository.findUserCredentialByUsername(username)).thenReturn(optionalUC);
        when(passwordEncoder.matches(password, password)).thenReturn(true);

        Credential actualUC = customUserDetailsService.getAuthenticatedUser(username, password);

        verify(credentialRepository, times(1)).findUserCredentialByUsername(username);

        assertEquals(expectedUC, actualUC);
    }

    @Test
    public void should_get_empty_user_exception() {
        assertThrows(InvalidUserNameOrPasswordException.class, () ->
          customUserDetailsService.getAuthenticatedUser(username, password));
    }

    @Test
    public void should_get_incorrect_password_exception() {
        when(credentialRepository.findUserCredentialByUsername(username)).thenReturn(optionalUC);
        when(passwordEncoder.matches(password, password)).thenReturn(false);
        assertThrows(InvalidUserNameOrPasswordException.class, () ->
          customUserDetailsService.getAuthenticatedUser(username, password));
    }
}
