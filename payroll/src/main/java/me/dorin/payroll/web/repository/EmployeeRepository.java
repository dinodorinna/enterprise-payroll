package me.dorin.payroll.web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.dorin.payroll.web.model.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
	List<Employee> findByDeactivated(boolean deactivated);
	Page<Employee> findByDeactivated(boolean deactivated, Pageable pageable);
	Optional<Employee> findByUsername(String username);

	@Query("SELECT e FROM Employee e WHERE e.deactivated = false")
	List<Employee> findAllByActive();
}
