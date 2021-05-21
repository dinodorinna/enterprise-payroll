package me.dorin.payroll.web.model.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EmployeeCreateRequest {
	@NotNull
	@Valid
	private String thaiName;
	@NotNull
	@Valid
	private String englishName;
	@NotNull
	@Valid
	private String socialId;
	@NotNull
	@Valid
	private String address;

	@Valid
	private Boolean hasPayrollAccess;
	@Valid
	private String username;
	@Valid
	private String password;
}
