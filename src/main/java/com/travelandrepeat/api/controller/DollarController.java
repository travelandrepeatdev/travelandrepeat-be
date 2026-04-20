package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.service.BanxicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dollar")
public class DollarController {

    private final BanxicoService banxicoService;

    @GetMapping(path = "/rate")
    public String getDollarRate() {
        return banxicoService.getExchangeRate();
    }
}
