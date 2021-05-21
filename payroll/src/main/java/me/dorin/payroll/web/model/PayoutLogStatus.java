package me.dorin.payroll.web.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Embeddable
public class PayoutLogStatus implements Serializable {
	@Enumerated(EnumType.ORDINAL)
	private PayoutStatus status;

	@NotNull
	@Valid
	private Date timestamp;

	@Valid
	private Long updaterId;

	@Valid
	private String remark;
}
