package org.sidibe.learning.demeter.domain;

public class Customer {

	private String name;
	private Wallet wallet;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public void changeColor(Wallet wallet) {
		Color color = wallet.getColor();
		color.setColor(0);// Violation
	}

	public float getPayment(float bill) {
		if (wallet == null) {
			return -1;
		}
		if (wallet.getTotalMoney() > bill) {
			wallet.subtractMoney(bill);
			return bill;
		}
		return -1;

	}
}
