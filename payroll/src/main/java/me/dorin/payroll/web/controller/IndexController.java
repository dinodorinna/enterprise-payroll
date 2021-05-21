package me.dorin.payroll.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class IndexController {
	@GetMapping(path = "/")
	public String index() {
		return "Hi";
	}
}
