package com.vehicle.maintenance.controller;

import com.vehicle.maintenance.exception.InvalidUserNameOrPasswordException;
import com.vehicle.maintenance.model.AuthRequestModel;
import com.vehicle.maintenance.model.TokenResponseModel;
import com.vehicle.maintenance.service.SignInService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private SignInService signInService;

    @PostMapping("/signin")
    public ResponseEntity<TokenResponseModel> signIn(@RequestBody AuthRequestModel authRequestModel) {
        try {
            return new ResponseEntity<>(signInService.signIn(authRequestModel), HttpStatus.OK);
        }
        catch(InvalidUserNameOrPasswordException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
