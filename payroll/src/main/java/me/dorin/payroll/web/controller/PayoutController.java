package me.dorin.payroll.web.controller;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import me.dorin.payroll.web.model.PayoutLog;
import me.dorin.payroll.web.model.request.PayoutLogCreateRequest;
import me.dorin.payroll.web.model.request.PayoutLogStatusRequest;
import me.dorin.payroll.web.scheduler.CutOffScheduler;
import me.dorin.payroll.web.service.PayoutService;

@RestController
@RequestMapping(path = "/payout")
@AllArgsConstructor
public class PayoutController {
	private final PayoutService payoutService;
	private final CutOffScheduler cutOffScheduler;

	@PostMapping(path = "/{employeeId}", params = "a=payout")
	@Transactional
	public PayoutLog createPayout(@PathVariable("employeeId") Long employeeId, @RequestBody PayoutLogCreateRequest request) {
		return payoutService.createPayout(employeeId, request);
	}

	@PostMapping(path = "/{payoutId}", params = "a=updatePayStatus")
	@Transactional
	public PayoutLog updatePayStatus(@PathVariable("payoutId") Long payoutId, @RequestBody PayoutLogStatusRequest request) {
		return payoutService.updatePayStatus(payoutId, request);
	}

	@GetMapping()
	public Page<PayoutLog> getPayLogs(Pageable pageable) {
		return payoutService.getPayLogs(pageable);
	}

	@GetMapping(path = "/{payoutId}")
	public PayoutLog getPayLog(@PathVariable Long payoutId) {
		return payoutService.getPayLog(payoutId);
	}

	@PostMapping(params = "a=forceCutOff")
	@Transactional
	public void forceCutOff() {
		cutOffScheduler.processCutOff();
	}
}
