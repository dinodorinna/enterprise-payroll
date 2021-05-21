package me.dorin.payroll.web.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "employee")
public class Employee {
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(AccessLevel.PRIVATE)
	private Long id;

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

	@ElementCollection
	@CollectionTable(name = "salary", joinColumns = @JoinColumn(name = "employee_id"))
	private List<EmployeeSalary> salaryHistory;

	private boolean hasPayrollAccess;

	@Valid
	private String username;

	@Valid
	@JsonIgnore
	private String password;

	@NotNull
	@Valid
	private Date joinDate;

	@NotNull
	@Valid
	private Date lastUpdate;

	@Column(name = "is_deactivated")
	private boolean deactivated;
	private Date deactivateDate;
}
