package me.dorin.payroll.web.model.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;
import me.dorin.payroll.web.model.EmployeeRole;

@Data
public class EmployeeSalaryUpdateRequest {
	@NotNull
	@Valid
	private Float salary;

	@NotNull
	@Valid
	private EmployeeRole role;
}
