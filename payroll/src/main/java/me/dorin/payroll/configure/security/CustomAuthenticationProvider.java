package me.dorin.payroll.configure.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import me.dorin.payroll.web.model.Employee;
import me.dorin.payroll.web.model.SecurityUserDetail;
import me.dorin.payroll.web.repository.EmployeeRepository;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements UserDetailsService {
	private final EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Employee> user = employeeRepository.findByUsername(username);
		if (user.isEmpty()){
			throw new UsernameNotFoundException(username);
		}

		return new SecurityUserDetail(user.get());
	}
}
