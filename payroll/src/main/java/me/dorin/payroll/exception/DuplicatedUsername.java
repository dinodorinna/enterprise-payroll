package me.dorin.payroll.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicatedUsername extends RuntimeException {
	public DuplicatedUsername(String username) {
		super(String.format("Username = %s duplicated", username));
	}
}
