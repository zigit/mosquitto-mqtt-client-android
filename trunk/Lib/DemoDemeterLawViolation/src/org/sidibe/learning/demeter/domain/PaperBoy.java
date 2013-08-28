package org.sidibe.learning.demeter.domain;

public class PaperBoy {

	private Customer customer;

	public void doPayment() {
		float payment = 2.00f;
		Wallet theWallet = customer.getWallet();
		if (theWallet.getTotalMoney() > payment) {
			theWallet.subtractMoney(payment);
		} else {
		}
	}

	public void doPaymentNews() {
		float payment = 2.00f; // ÒI want my two dollars!Ó
		float paidAmount = customer.getPayment(payment);
		if (paidAmount == payment) {
			// say thank you and give customer a receipt
		} else {
			// come back later and get my money
		}
	}

}
