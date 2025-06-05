package org.example.Controller;

import lombok.AllArgsConstructor;
import org.example.Entities.RefreshToken;
import org.example.Model.UserInfoDto;
import org.example.Service.JwtService;
import org.example.Service.RefreshTokenService;
import org.example.Service.UserDetailsServiceImp;
import org.example.response.JwtResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @PostMapping("auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody UserInfoDto userInfoDto){
        try{
            Boolean isSignUped = userDetailsServiceImp.signupUser(userInfoDto);
            if(Boolean.FALSE.equals(isSignUped)){
                return new ResponseEntity<>("Already Exist", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());
            return new ResponseEntity<>(JwtResponseDTO. builder().accessToken(jwtToken).
                    token(refreshToken.getToken()).build() , HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>("Exception in User Seervice" , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
