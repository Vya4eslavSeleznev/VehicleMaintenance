package com.vehicle.maintenance.service;

import com.vehicle.maintenance.entity.Credential;
import com.vehicle.maintenance.exception.InvalidUserNameOrPasswordException;
import com.vehicle.maintenance.model.AuthRequestModel;
import com.vehicle.maintenance.model.Role;
import com.vehicle.maintenance.model.TokenResponseModel;
import com.vehicle.maintenance.security.jwt.JwtTokenProvider;
import com.vehicle.maintenance.service.impl.CustomUserDetailsService;
import com.vehicle.maintenance.service.impl.SignInServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignInServiceTest {

    @InjectMocks
    private SignInServiceImpl signInService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void should_sign_in_users_token_returned() throws InvalidUserNameOrPasswordException {
        String username = "username";
        String password = "pwd";
        String token = "qwe123qwe";

        AuthRequestModel requestModel = new AuthRequestModel(username, password);
        Credential credential = new Credential(Role.USER, password, username);

        when(userDetailsService.getAuthenticatedUser(
          requestModel.getUsername(), requestModel.getPassword()))
          .thenReturn(credential);

        when(jwtTokenProvider.createToken(credential.getUsername(), credential.getRole())).thenReturn(token);

        TokenResponseModel tokenResponseModel = signInService.signIn(requestModel);

        verify(userDetailsService, times(1)).getAuthenticatedUser(
          requestModel.getUsername(), requestModel.getPassword()
        );

        verify(jwtTokenProvider, times(1)).createToken(
          credential.getUsername(), credential.getRole()
        );

        assertEquals(token, tokenResponseModel.getToken());
    }
}
