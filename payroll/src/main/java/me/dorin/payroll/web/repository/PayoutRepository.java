package me.dorin.payroll.web.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import me.dorin.payroll.web.model.PayoutLog;

@Repository
public interface PayoutRepository extends PagingAndSortingRepository<PayoutLog, Long> {

}
