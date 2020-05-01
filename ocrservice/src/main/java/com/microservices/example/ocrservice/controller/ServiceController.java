package com.microservices.example.ocrservice.controller;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

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

	@PostMapping("/parse")
	@PreAuthorize("#oauth2.hasScope('upload')")
	public String ocr(@RequestParam("file") MultipartFile file) throws TesseractException, IOException {
		File tempFile = File.createTempFile("temp_", ".jpg");
		try {
			file.transferTo(tempFile);
			tempFile.deleteOnExit();
			Tesseract tesseract = new Tesseract();
			tesseract.setDatapath(LoadLibs.extractTessResources("tessdata").getAbsolutePath());
			// tesseract.setLanguage("eng");
			// tesseract.setPageSegMode(1);
			// tesseract.setOcrEngineMode(1);
			return tesseract.doOCR(tempFile);
		} finally {
			log.info(tempFile.getAbsolutePath() + " deleted: " + tempFile.delete());
		}
	}

}
