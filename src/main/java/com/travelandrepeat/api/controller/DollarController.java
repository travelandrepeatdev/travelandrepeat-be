package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.service.BanxicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dollar")
public class DollarController {

    @Autowired
    BanxicoService banxicoService;

    @GetMapping(path = "/rate")
    public String getDollarRate() {
        return banxicoService.getExchangeRate();
    }
}
