package com.travelandrepeat.api.service;

public interface CaptchaValidatorService {

    void verify(String token) throws Exception;

}
