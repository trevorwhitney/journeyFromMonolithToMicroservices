package com.example.billing;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.POST;

public class Client {
	private RestTemplate restTemplate;

	public Client(RestTemplate restTemplate) {
		restTemplate.setErrorHandler(new RestTemplateErrorHandler());
		this.restTemplate = restTemplate;
	}

	@HystrixCommand(fallbackMethod = "billUserFallback")
	public ResponseEntity<String> billUser(BillingRequest billingRequest) {
		HttpEntity<BillingRequest> request = new HttpEntity<>(billingRequest);
		ResponseEntity<String> exchange =
				this.restTemplate.exchange(
						"http://billing/reocurringPayment",
						POST,
						request,
						String.class
				);

		if (exchange.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
			return billUserFallback(billingRequest);
		}

		return exchange;
	}

	public ResponseEntity<String> billUserFallback(BillingRequest billingRequest) {
		return new ResponseEntity<>("Hello World", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
