package me.dorin.payroll.web.model.request;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PayoutLogCreateRequest {
	@Valid
	private Float amount;

	@Valid
	private String remark;

	@Valid
	@NotNull
	private Date month;
}
