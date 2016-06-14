package com.example.billing.reocurringPayments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class Controller {
    @Autowired
    private com.example.payments.Gateway paymentGateway;

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @RequestMapping(value = "/reocurringPayment", method = RequestMethod.POST)
    public ResponseEntity<String> createReocurringPayment(@RequestBody Map<String, Object> data){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-type", MediaType.APPLICATION_JSON.toString());

        ResponseEntity<String> response;
        if (paymentGateway.createReocurringPayment((Integer)data.get("amount"))) {
            response = new ResponseEntity<>("{errors: []}", responseHeaders, HttpStatus.CREATED);
        } else {
            response = new ResponseEntity<>("{errors: [\"error1\", \"error2\"]}", responseHeaders, HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}
