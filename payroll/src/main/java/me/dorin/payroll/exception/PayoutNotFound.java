package me.dorin.payroll.exception;

public class PayoutNotFound extends RuntimeException {
	public PayoutNotFound(Long id) {
		super(String.format("Payout id = %d Not found", id));
	}
}
