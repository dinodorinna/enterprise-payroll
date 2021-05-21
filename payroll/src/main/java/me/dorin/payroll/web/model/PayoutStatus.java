package me.dorin.payroll.web.model;

public enum PayoutStatus {
	CREATE,
	AUDIT,
	PAYMENT,
	DONE,
	INVALID_BANKID,
	SUSPENDED,
	INVALID,
	DUPLICATED
}
