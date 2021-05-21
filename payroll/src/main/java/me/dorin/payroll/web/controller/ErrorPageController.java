package me.dorin.payroll.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import me.dorin.payroll.web.model.ErrorJson;

@RestController
public class ErrorPageController implements ErrorController {
	private final ErrorAttributes errorAttributes;

	private static final String PATH = "/error";

	public ErrorPageController(ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}

	@RequestMapping(value = PATH)
	public ErrorJson error(HttpServletResponse response, WebRequest webRequest) {
		return new ErrorJson(response.getStatus(), getErrorAttributes(webRequest));
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

	private Map<String, Object> getErrorAttributes(WebRequest webRequest) {
		return errorAttributes.getErrorAttributes(webRequest, false);
	}
}
