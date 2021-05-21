package me.dorin.payroll.web.service.implement;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import me.dorin.payroll.exception.DuplicatedUsername;
import me.dorin.payroll.exception.EmployeeNotFound;
import me.dorin.payroll.web.model.Employee;
import me.dorin.payroll.web.model.EmployeeSalary;
import me.dorin.payroll.web.model.SecurityUserDetail;
import me.dorin.payroll.web.model.request.EmployeeCreateRequest;
import me.dorin.payroll.web.model.request.EmployeeSalaryUpdateRequest;
import me.dorin.payroll.web.model.request.EmployeeUpdateRequest;
import me.dorin.payroll.web.repository.EmployeeRepository;
import me.dorin.payroll.web.service.EmployeeService;

@AllArgsConstructor
@Service
@Log
public class EmployeeServiceImpl implements EmployeeService {
	private final EmployeeRepository employeeRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Page<Employee> getAllEmployee(Pageable pageable) {
		return employeeRepository.findByDeactivated(false, pageable);
	}

	@Override
	public Employee getEmployeeById(Long id) throws EmployeeNotFound {
		Optional<Employee> employee = employeeRepository.findById(id);

		if (employee.isEmpty()) {
			throw new EmployeeNotFound(id);
		}

		return employee.get();
	}

	@Override
	public Employee createEmployee(EmployeeCreateRequest request) throws DuplicatedUsername {
		Employee employee = new Employee();

		employee.setEnglishName(request.getEnglishName());
		employee.setThaiName(request.getThaiName());
		employee.setSocialId(request.getSocialId());
		employee.setAddress(request.getAddress());
		employee.setHasPayrollAccess(request.getHasPayrollAccess() != null ? request.getHasPayrollAccess() : false);
		employee.setJoinDate(new Date(System.currentTimeMillis()));
		employee.setLastUpdate(new Date(System.currentTimeMillis()));
		employee.setDeactivated(false);
		employee.setDeactivateDate(null);

		if (request.getHasPayrollAccess() != null && request.getHasPayrollAccess()) {
			String encryptPassword = passwordEncoder.encode(request.getPassword());

			if (employeeRepository.findByUsername(request.getUsername()).isPresent()) {
				throw new DuplicatedUsername(request.getUsername());
			}

			employee.setUsername(request.getUsername());
			employee.setPassword(encryptPassword);
		}

		employee = employeeRepository.save(employee);

		log.log(Level.INFO, MessageFormat.format("Created user id={0}, name={1}, payrollAccess={2}",
				employee.getId(), employee.getEnglishName(), employee.isHasPayrollAccess()));

		return employee;
	}

	@Override
	public Employee updateEmployee(Long id, EmployeeUpdateRequest request) {
		Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
			throw new EmployeeNotFound(id);
		});

		if (request.getEnglishName() != null) {
			employee.setEnglishName(request.getEnglishName());
		}
		if (request.getThaiName() != null) {
			employee.setThaiName(request.getThaiName());
		}
		if (request.getSocialId() != null) {
			employee.setSocialId(request.getSocialId());
		}
		if (request.getAddress() != null) {
			employee.setAddress(request.getAddress());
		}
		if (request.getHasPayrollAccess() != null) {
			employee.setHasPayrollAccess(request.getHasPayrollAccess());
		}
		if (request.getPassword() != null
				&& (request.getHasPayrollAccess() == null ? employee.isHasPayrollAccess() : request.getHasPayrollAccess())) {
			String encryptPassword = passwordEncoder.encode(request.getPassword());
			employee.setPassword(encryptPassword);
		}
		employee.setLastUpdate(new Date(System.currentTimeMillis()));

		employeeRepository.save(employee);

		log.log(Level.INFO, MessageFormat.format("Updated user id={0}, name={1}, payrollAccess={2}",
				employee.getId(), employee.getEnglishName(), employee.isHasPayrollAccess()));

		return employee;
	}

	@Override
	public Employee disableEmployee(Long id) {
		Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
			throw new EmployeeNotFound(id);
		});

		employee.setLastUpdate(new Date(System.currentTimeMillis()));
		employee.setDeactivated(true);
		employee.setDeactivateDate(new Date(System.currentTimeMillis()));

		employeeRepository.save(employee);

		log.log(Level.INFO, MessageFormat.format("Deactivated user id={0}, name={1}",
				employee.getId(), employee.getEnglishName()));

		return employee;
	}

	@Override
	public Employee reactivateEmployee(Long id) {
		Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
			throw new EmployeeNotFound(id);
		});

		employee.setLastUpdate(new Date(System.currentTimeMillis()));
		employee.setDeactivated(false);
		employee.setDeactivateDate(null);

		employeeRepository.save(employee);

		log.log(Level.INFO, MessageFormat.format("Reactivated user id={0}, name={1}",
				employee.getId(), employee.getEnglishName()));

		return employee;
	}

	@Override
	public EmployeeSalary updateEmployeeSalary(Long id, EmployeeSalaryUpdateRequest request) {
		Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
			throw new EmployeeNotFound(id);
		});

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!(principal instanceof SecurityUserDetail)) {
			throw new RuntimeException();
		}

		EmployeeSalary employeeSalary = new EmployeeSalary();
		employeeSalary.setSalary(request.getSalary());
		employeeSalary.setRole(request.getRole());
		employeeSalary.setTimestamp(new Date(System.currentTimeMillis()));
		employeeSalary.setUpdaterId(((SecurityUserDetail)principal).getUser().getId());

		employee.getSalaryHistory().add(employeeSalary);
		employee.setLastUpdate(new Date(System.currentTimeMillis()));

		employeeRepository.save(employee);

		log.log(Level.INFO, MessageFormat.format("Created user id={0}, name={1}, newSalary={2}, newRole={3}",
				employee.getId(), employee.getEnglishName(), request.getSalary(), request.getRole()));

		return employeeSalary;
	}
}
