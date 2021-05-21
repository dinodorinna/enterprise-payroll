package me.dorin.payroll.web.controller;

import java.sql.Date;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import me.dorin.payroll.web.model.EmployeeIncentive;
import me.dorin.payroll.web.model.request.EmployeeIncentiveCreateRequest;

@RestController
@RequestMapping(path = "/incentive")
@AllArgsConstructor
public class EmployeeIncentiveController {

	@GetMapping(path = "/{employeeId}")
	public Page<EmployeeIncentive> list(@PathVariable("employeeId") Long employeeId, @RequestBody Date month) {
		return null;
	}

	@PostMapping(path = "/{employeeId}")
	@Transactional
	public EmployeeIncentive addEmployeeIncentive(@PathVariable("employeeId") Long employeeId,
			@RequestBody EmployeeIncentiveCreateRequest request) {
		return null;
	}

	@DeleteMapping(path = "/{employeeId}")
	@Transactional
	public EmployeeIncentive removeEmployeeIncentive(@PathVariable("employeeId") Long employeeId, @RequestBody Long incentiveId) {
		return null;
	}
}
