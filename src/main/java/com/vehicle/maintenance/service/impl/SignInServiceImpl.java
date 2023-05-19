package com.vehicle.maintenance.service.impl;

import com.vehicle.maintenance.entity.Credential;
import com.vehicle.maintenance.exception.InvalidUserNameOrPasswordException;
import com.vehicle.maintenance.model.AuthRequestModel;
import com.vehicle.maintenance.model.TokenResponseModel;
import com.vehicle.maintenance.security.jwt.JwtTokenProvider;
import com.vehicle.maintenance.service.SignInService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class SignInServiceImpl implements SignInService {

    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenResponseModel signIn(AuthRequestModel request)
      throws InvalidUserNameOrPasswordException {
        Credential user = userDetailsService.getAuthenticatedUser(request.getUsername(), request.getPassword());
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole());

        Map<Object, Object> model = new HashMap<>();
        model.put("token", token);

        return new TokenResponseModel(model.get("token").toString());
    }
}
