package com.example.billing.reocurringPayments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class Controller {
	@Autowired
	private com.example.payments.Gateway paymentGateway;

	@Autowired
	private Environment env;

	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	@RequestMapping(value = "/reocurringPayment", method = POST)
	public ResponseEntity<String> createReocurringPayment(@RequestBody Map<String, Object> data) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("content-type", MediaType.APPLICATION_JSON.toString());

		logger.info("************ Creating Reocurring payment *****************");
		logger.info("From Billing server with active profiles: ");
		for (String profile : env.getActiveProfiles()) {
			logger.info(profile + "\n");
		}
		logger.info("**********************************************************");

		ResponseEntity<String> response;
		if (paymentGateway.createReocurringPayment((Integer) data.get("amount"))) {
			Integer amount = (Integer) data.get("amount");

			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			logger.info("Amount: " + Integer.toString(amount));
			logger.info("Amount: " + data.get("amount"));
			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

			if (amount < 1) {
				response = new ResponseEntity<>("{errors: []}", BAD_REQUEST);
			} else if (amount == 42) {
				response = new ResponseEntity<>("{errors: []}", INTERNAL_SERVER_ERROR);
			} else {
				response = new ResponseEntity<>("{errors: []}", responseHeaders, CREATED);
			}
		} else {
			response = new ResponseEntity<>("{errors: [\"error1\", \"error2\"]}", responseHeaders, BAD_REQUEST);
		}

		return response;
	}
}
