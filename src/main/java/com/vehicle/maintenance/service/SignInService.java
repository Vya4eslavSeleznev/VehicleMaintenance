package com.vehicle.maintenance.service;

import com.vehicle.maintenance.exception.InvalidUserNameOrPasswordException;
import com.vehicle.maintenance.model.AuthRequestModel;
import com.vehicle.maintenance.model.TokenResponseModel;

public interface SignInService {

    TokenResponseModel signIn(AuthRequestModel authRequestModel)
      throws InvalidUserNameOrPasswordException;
}
