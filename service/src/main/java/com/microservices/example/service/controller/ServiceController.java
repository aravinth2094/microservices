package com.microservices.example.service.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class ServiceController {

	private static Logger log = LoggerFactory.getLogger(ServiceController.class);

	@Value("${server.port}")
	private int port;

	@GetMapping("/serviceGreeting")
	@PreAuthorize("#oauth2.hasScope('greet') and hasAnyRole('ADMIN', 'USER')")
	public String serviceGreeting(@RequestHeader(value = "x-forwarded-for", required = false) String clientIp,
			Principal principal) {
		try {
			log.info("Processing request for {} from {}", principal.getName(), clientIp);
			return InetAddress.getLocalHost().getHostAddress() + ":" + port + " says \"Hello " + principal.getName()
					+ "\"";
		} catch (UnknownHostException e) {
			return "Service Error: " + e.getMessage();
		}
	}

	@GetMapping("/userinfo")
	@PreAuthorize("#oauth2.hasScope('profile') and hasAnyRole('ADMIN', 'USER')")
	public Principal userInfo(Principal principal) {
		return principal;
	}

}
