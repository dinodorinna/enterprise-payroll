package me.dorin.payroll.web.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name = "employee_incentive")
public class EmployeeIncentive {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@NotNull
	@Setter(AccessLevel.PRIVATE)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="employee_id", nullable=false)
	private Employee employee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="updater_id", nullable=false)
	private Employee updater;

	@NotNull
	@Valid
	private Date timestamp;

	@Valid
	@NotNull
	@Column(name = "incentive_type")
	@Enumerated(EnumType.ORDINAL)
	private IncentiveType type;

	@Valid
	@NotNull
	private Float amount;
}
