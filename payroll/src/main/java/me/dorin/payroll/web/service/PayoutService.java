package me.dorin.payroll.web.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.dorin.payroll.web.model.Employee;
import me.dorin.payroll.web.model.PayoutLog;
import me.dorin.payroll.web.model.request.PayoutLogCreateRequest;
import me.dorin.payroll.web.model.request.PayoutLogStatusRequest;

public interface PayoutService {
	PayoutLog createPayout(Long employeeId, PayoutLogCreateRequest request);

	PayoutLog createPayout(Employee employee, PayoutLogCreateRequest request);

	PayoutLog updatePayStatus(Long payoutId, PayoutLogStatusRequest request);

	Page<PayoutLog> getPayLogs(Pageable pageable);

	PayoutLog getPayLog(Long payoutId);
}
