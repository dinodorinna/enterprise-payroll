package me.dorin.payroll.web.scheduler;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import me.dorin.payroll.web.model.Employee;
import me.dorin.payroll.web.model.request.PayoutLogCreateRequest;
import me.dorin.payroll.web.repository.EmployeeRepository;
import me.dorin.payroll.web.service.PayoutService;

@Component
@Log
@AllArgsConstructor
public class CutOffScheduler {
	private final EmployeeRepository employeeRepository;
	private final PayoutService payoutService;

	@Scheduled(cron = "0 0 0 1 1/1 ?")
	@Transactional
	public void processCutOff() {
		log.info("Start processing salary payout");

		List<Employee> employees = employeeRepository.findAllByActive();

		employees.forEach(employee -> {
			PayoutLogCreateRequest request = new PayoutLogCreateRequest();
			request.setMonth(new Date());
			request.setRemark("Auto cut off");

			payoutService.createPayout(employee, request);
		});

		log.info("Finish processing salary payout");
	}
}
