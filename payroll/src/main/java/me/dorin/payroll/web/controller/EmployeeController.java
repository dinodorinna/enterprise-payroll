package me.dorin.payroll.web.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import me.dorin.payroll.exception.DuplicatedUsername;
import me.dorin.payroll.web.model.Employee;
import me.dorin.payroll.web.model.EmployeeSalary;
import me.dorin.payroll.web.model.request.EmployeeCreateRequest;
import me.dorin.payroll.web.model.request.EmployeeSalaryUpdateRequest;
import me.dorin.payroll.web.model.request.EmployeeUpdateRequest;
import me.dorin.payroll.web.service.EmployeeService;

@RestController
@RequestMapping(path = "/employee")
@AllArgsConstructor
public class EmployeeController {
	private final EmployeeService employeeService;

	@GetMapping
	public Page<Employee> list(Pageable pageable) {
		return employeeService.getAllEmployee(pageable);
	}

	@GetMapping(path = "/{id}")
	public Employee getEmployeeById(@PathVariable Long id) {
		return employeeService.getEmployeeById(id);
	}

	@PostMapping
	@Transactional
	public Employee createEmployee(@RequestBody EmployeeCreateRequest request) throws DuplicatedUsername {
		return employeeService.createEmployee(request);
	}

	@PatchMapping(path = "/{id}")
	@Transactional
	public Employee updateEmployee(@PathVariable("id") Long id, @RequestBody EmployeeUpdateRequest request) {
		return employeeService.updateEmployee(id, request);
	}

	@DeleteMapping(path = "/{id}")
	@Transactional
	public Employee deactivateEmployee(@PathVariable("id") Long id) {
		return employeeService.disableEmployee(id);
	}

	@PatchMapping(path = "/{id}", params = "a=reactivate")
	@Transactional
	public Employee reactivateEmployee(@PathVariable("id") Long id) {
		return employeeService.reactivateEmployee(id);
	}

	@PostMapping(path = "/{id}", params = "a=updateSalary")
	@Transactional
	public EmployeeSalary updateEmployeeSalaryAndRole(@PathVariable("id") Long id,
			@RequestBody EmployeeSalaryUpdateRequest request) {
		return employeeService.updateEmployeeSalary(id, request);
	}
}
