package me.dorin.payroll.web.model.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;
import me.dorin.payroll.web.model.PayoutStatus;

@Data
public class PayoutLogStatusRequest {
	@Valid
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	private PayoutStatus status;

	@Valid
	private String remark;
}
