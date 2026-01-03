package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.QuotationFormRequest;

public interface MailService {
    void sendMail(QuotationFormRequest request);
}
