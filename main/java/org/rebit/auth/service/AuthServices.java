package org.rebit.auth.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.rebit.auth.model.ApiAndRolesDto;
import org.rebit.auth.model.ApiAndRolesResponseDto;
import org.rebit.auth.model.KeyAndCaptchaDto;
import org.rebit.auth.model.LogOutDto;
import org.rebit.auth.model.PermitToAllDto;
import org.rebit.auth.model.StatusAndMessage;
import org.rebit.auth.model.SynchDto;
import org.rebit.auth.model.TokenAndUri;
import org.rebit.auth.model.TokenResponse;
import org.rebit.auth.model.UsernameAndPasswordDto;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

public interface AuthServices {
	TokenResponse jwtTokenByRefreshToken(String refreshToken);
	TokenResponse login(UsernameAndPasswordDto usernameAndPasswordDto);
	StatusAndMessage verifyUri(TokenAndUri tokenAndUri);
	void apiAndRolesMaping(@Valid ApiAndRolesDto apiAndRolesDto);
	List<ApiAndRolesResponseDto> retrieveAndRolesMaping();
	void apiPermitAll(PermitToAllDto permitToAllDto);
	void logOutUser(LogOutDto logOutDto,String authorization);
	public ResponseEntity<Object> captcha() throws FileNotFoundException, IOException, Exception;
	public KeyAndCaptchaDto getPublicKey();
	void getSynch(SynchDto synchDto);
	void refreshApplicationProperties();
}