package me.dorin.payroll.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.dorin.payroll.web.model.Employee;
import me.dorin.payroll.web.model.EmployeeSalary;

@Repository
public interface EmployeeSalaryRepository extends CrudRepository<EmployeeSalary, Long> {
}
