package me.dorin.payroll.web.repository;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.dorin.payroll.web.model.Employee;
import me.dorin.payroll.web.model.EmployeeIncentive;

@Repository
public interface EmployeeIncentiveRepository extends CrudRepository<EmployeeIncentive, Long> {
	List<EmployeeIncentive> findAllByEmployeeIsAndTimestampBetween(@NotNull Employee employee,
			@NotNull @Valid Date start, @NotNull @Valid Date end);
}
