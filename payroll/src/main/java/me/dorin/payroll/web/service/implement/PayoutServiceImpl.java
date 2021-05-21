package me.dorin.payroll.web.service.implement;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import me.dorin.payroll.exception.EmployeeNotFound;
import me.dorin.payroll.exception.PayoutNotFound;
import me.dorin.payroll.web.model.Employee;
import me.dorin.payroll.web.model.EmployeeIncentive;
import me.dorin.payroll.web.model.EmployeeSalary;
import me.dorin.payroll.web.model.IncentiveType;
import me.dorin.payroll.web.model.PayoutLog;
import me.dorin.payroll.web.model.PayoutLogStatus;
import me.dorin.payroll.web.model.PayoutStatus;
import me.dorin.payroll.web.model.SecurityUserDetail;
import me.dorin.payroll.web.model.request.PayoutLogCreateRequest;
import me.dorin.payroll.web.model.request.PayoutLogStatusRequest;
import me.dorin.payroll.web.repository.EmployeeIncentiveRepository;
import me.dorin.payroll.web.repository.EmployeeRepository;
import me.dorin.payroll.web.repository.PayoutRepository;
import me.dorin.payroll.web.service.PayoutService;

@Service
@AllArgsConstructor
@Log
public class PayoutServiceImpl implements PayoutService {
	private final EmployeeRepository employeeRepository;
	private final PayoutRepository payoutRepository;
	private final EmployeeIncentiveRepository employeeIncentiveRepository;

	@Override
	public PayoutLog createPayout(Long employeeId, PayoutLogCreateRequest request) {
		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> {
			throw new EmployeeNotFound(employeeId);
		});

		return createPayout(employee, request);
	}

	@Override
	public PayoutLog createPayout(Employee employee, PayoutLogCreateRequest request) {
		SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		PayoutLogStatus payoutLogStatus = new PayoutLogStatus();
		payoutLogStatus.setStatus(PayoutStatus.CREATE);
		payoutLogStatus.setTimestamp(new Date(System.currentTimeMillis()));
		payoutLogStatus.setUpdaterId(user.getUser().getId());

		PayoutLog payout = new PayoutLog();
		payout.setEmployeeId(employee.getId());
		payout.setTimestamp(new Date(System.currentTimeMillis()));
		payout.setStatus(List.of(payoutLogStatus));
		payout.setRemark(request.getRemark());

		if (request.getAmount() == null) {
			if (employee.getSalaryHistory().isEmpty()) {
				throw new RuntimeException("No value provided to payout");
			}

			EmployeeSalary employeeSalary = employee.getSalaryHistory()
					.stream()
					.sorted(Comparator.comparing(EmployeeSalary::getTimestamp).reversed())
					.collect(Collectors.toList())
					.get(0);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(request.getMonth());

			Calendar cutDate = Calendar.getInstance();
			cutDate.setTime(cutDate.getTime());

			int endMonth = calendar.get(Calendar.MONTH);
			int endYear = calendar.get(Calendar.YEAR);
			int startMonth = endMonth == Calendar.JANUARY ? Calendar.DECEMBER : endMonth - 1;
			int startYear = endMonth == Calendar.JANUARY ? endYear - 1 : endYear;

			Calendar startDate = Calendar.getInstance();
			startDate.set(startYear, startMonth, cutDate.get(Calendar.DATE) + 1);

			Calendar endDate = Calendar.getInstance();
			endDate.set(endYear, endMonth, cutDate.get(Calendar.DATE));

			List<EmployeeIncentive> incentives =
					employeeIncentiveRepository.findAllByEmployeeIsAndTimestampBetween(
					employee, startDate.getTime(), endDate.getTime());

			float payAmount = employeeSalary.getSalary();
			for (EmployeeIncentive incentive : incentives) {
				if (incentive.getAmount() >= 0) {
					continue;
				}

				if (incentive.getType() == IncentiveType.CASH) {
					payAmount -= incentive.getAmount();
				} else {
					payAmount *= 1 - (incentive.getAmount() / 100);
				}
			}

			for (EmployeeIncentive incentive : incentives) {
				if (incentive.getAmount() < 0) {
					continue;
				}

				if (incentive.getType() == IncentiveType.CASH) {
					payAmount += incentive.getAmount();
				} else {
					payAmount *= 1 + (incentive.getAmount() / 100);
				}
			}

			payout.setAmount(payAmount);
		} else {
			payout.setAmount(request.getAmount());
		}
		if (request.getRemark() != null) {
			payout.setRemark(request.getRemark());
		}

		payoutRepository.save(payout);

		return payout;
	}

	@Override
	public PayoutLog updatePayStatus(Long payoutId, PayoutLogStatusRequest request) {
		SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		PayoutLog payoutLog = payoutRepository.findById(payoutId).orElseThrow(() -> {
			throw new PayoutNotFound(payoutId);
		});

		PayoutLogStatus payoutLogStatus = new PayoutLogStatus();
		payoutLogStatus.setStatus(request.getStatus());
		payoutLogStatus.setTimestamp(new Date(System.currentTimeMillis()));
		payoutLogStatus.setUpdaterId(user.getUser().getId());

		if (request.getRemark() != null) {
			payoutLogStatus.setRemark(request.getRemark());
		}

		payoutLog.getStatus().add(payoutLogStatus);

		payoutRepository.save(payoutLog);

		return payoutLog;
	}

	@Override
	public Page<PayoutLog> getPayLogs(Pageable pageable) {
		return payoutRepository.findAll(pageable);
	}

	@Override
	public PayoutLog getPayLog(Long payoutId) {
		Optional<PayoutLog> payoutLog = payoutRepository.findById(payoutId);

		if (payoutLog.isEmpty()) {
			throw new PayoutNotFound(payoutId);
		}

		return payoutLog.get();
	}
}
