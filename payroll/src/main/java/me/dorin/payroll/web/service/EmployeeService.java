package me.dorin.payroll.web.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.dorin.payroll.exception.DuplicatedUsername;
import me.dorin.payroll.exception.EmployeeNotFound;
import me.dorin.payroll.web.model.Employee;
import me.dorin.payroll.web.model.EmployeeSalary;
import me.dorin.payroll.web.model.request.EmployeeCreateRequest;
import me.dorin.payroll.web.model.request.EmployeeSalaryUpdateRequest;
import me.dorin.payroll.web.model.request.EmployeeUpdateRequest;

public interface EmployeeService {
	Page<Employee> getAllEmployee(Pageable pageable);

	Employee getEmployeeById(Long id) throws EmployeeNotFound;

	Employee createEmployee(EmployeeCreateRequest request) throws DuplicatedUsername;

	Employee updateEmployee(Long id, EmployeeUpdateRequest request);

	Employee disableEmployee(Long id);

	Employee reactivateEmployee(Long id);

	EmployeeSalary updateEmployeeSalary(Long id, EmployeeSalaryUpdateRequest request);
}
