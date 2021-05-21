package me.dorin.payroll.web.model.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;
import me.dorin.payroll.web.model.IncentiveType;

@Data
public class EmployeeIncentiveCreateRequest {
	@Valid
	@NotNull
	private IncentiveType type;

	@Valid
	@NotNull
	private Float amount;
}
