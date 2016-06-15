package com.example.subscriptions;

import com.example.billing.BillingRequest;
import com.example.billing.Client;
import com.example.email.SendEmail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CreateSubscription {

	private final Client billingClient;
	private final SendEmail emailSender;
	private final SubscriptionRepository subscriptions;

	public CreateSubscription(
			Client billingClient,
			SendEmail emailSender, SubscriptionRepository subscriptions) {
		this.billingClient = billingClient;
		this.emailSender = emailSender;
		this.subscriptions = subscriptions;
	}

	public HttpStatus run(String userId, String packageId, int amount) {
		subscriptions.create(new Subscription(userId, packageId));
		ResponseEntity<String> response = billingClient.billUser(new BillingRequest(userId, amount));

		if (response.getStatusCode() == HttpStatus.CREATED) {
			emailSender.run("me@example.com", "Subscription Created", "Some email body");
			return HttpStatus.CREATED;
		} else {
			return response.getStatusCode();
		}
	}
}
