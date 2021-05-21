package me.dorin.payroll.web.model;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Embeddable
public class EmployeeSalary {
	private Long updaterId;

	@NotNull
	@Valid
	private Date timestamp;

	@NotNull
	@Valid
	private Float salary;

	@NotNull
	@Valid
	@Enumerated(EnumType.ORDINAL)
	private EmployeeRole role;
}
