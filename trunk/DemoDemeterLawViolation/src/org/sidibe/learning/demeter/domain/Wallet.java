package org.sidibe.learning.demeter.domain;

public class Wallet {

	private float value;
	private Color color;

	public float getTotalMoney() {
		return value;
	}

	public void setTotalMoney(float newValue) {
		value = newValue;
	}

	public void addMoney(float deposit) {
		value += deposit;
	}

	public void subtractMoney(float debit) {
		value -= debit;
	}

	public Color getColor() {
		return color;
	}
}
