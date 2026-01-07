package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.service.BanxicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/dollar")
public class DollarController {

    @Autowired
    private BanxicoService banxicoService;

    @GetMapping(path = "/rate")
    public ResponseEntity<String> getDollarRate() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(banxicoService.getExchangeRate());
    }
}
