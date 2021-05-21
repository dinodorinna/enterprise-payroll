package me.dorin.payroll.web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.dorin.payroll.web.model.PayoutLogStatus;

@Repository
public interface PayoutStatusRepository extends CrudRepository<PayoutLogStatus, Long> {

}
