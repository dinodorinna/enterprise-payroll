package me.dorin.payroll.exception;

public class EmployeeNotFound extends RuntimeException {
	public EmployeeNotFound(Long id) {
		super(String.format("Employee id = %d Not found", id));
	}
}
