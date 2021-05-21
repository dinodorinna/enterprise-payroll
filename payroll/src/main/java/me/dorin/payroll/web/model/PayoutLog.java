package me.dorin.payroll.web.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name = "payout")
public class PayoutLog {
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(AccessLevel.PRIVATE)
	private Long id;

	@Valid
	private Long employeeId;

	@NotNull
	@Valid
	private Date timestamp;

	@ElementCollection
	@CollectionTable(name = "payout_status", joinColumns = @JoinColumn(name = "log_id"))
	private List<PayoutLogStatus> status;

	@NotNull
	@Valid
	private Float amount;

	@Valid
	private String remark;
}
