package me.dorin.payroll.web.model.request;

import javax.validation.Valid;

import lombok.Data;
import me.dorin.payroll.web.model.EmployeeRole;

@Data
public class EmployeeUpdateRequest {
	@Valid
	private String thaiName;
	@Valid
	private String englishName;
	@Valid
	private String socialId;
	@Valid
	private String address;

	@Valid
	private Boolean hasPayrollAccess;
	@Valid
	private String password;
}
