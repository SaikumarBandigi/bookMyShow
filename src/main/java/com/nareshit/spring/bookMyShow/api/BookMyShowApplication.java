package com.nareshit.spring.bookMyShow.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@SpringBootApplication
@RestController
@EnableHystrix
@EnableHystrixDashboard
public class BookMyShowApplication {

	@Autowired
	private RestTemplate template;

	@HystrixCommand(fallbackMethod = "bookMyShowFallBack")
	@GetMapping("/bookNow")
	public String bookShow() {
		String emailServiceResponse = template.getForObject("http://localhost:8181/emailService/send", String.class);
		String paymentServiceResponse = template.getForObject("http://localhost:8282/paymentService/pay", String.class);

		return emailServiceResponse + "\n" + paymentServiceResponse;
	}

	@GetMapping("/bookNowWitoutHystrix")
	public String bookTicketWitoutHystrix() {
			String paymentServiceResponse = template.getForObject("http://localhost:8282/paymentService/pay", String.class);
			String emailServiceResponse = template.getForObject("http://localhost:8181/emailService/send", String.class);

			return emailServiceResponse + "\n" + paymentServiceResponse;
	}

	public static void main(String[] args) {
		SpringApplication.run(BookMyShowApplication.class, args);
	}

	public String bookMyShowFallBack() {
		return "Payment Service May be Down Please try again Later...";
	}

	@Bean
	public RestTemplate template() {
		return new RestTemplate();
	}


}

// 3 projects this and paytm,emailService for hystrix project
// in pom.xml currently no Spring Cloud version compatible with Spring Boot 2.4. Downgrade Spring Boot.

/*

In Spring Boot, the spring-boot-starter-actuator dependency provides built-in endpoints to monitor and interact with your Spring Boot application. These endpoints offer various functionalities such as viewing application health, metrics, environment details, logging configuration, thread dumps, etc. It's commonly used for monitoring and managing applications in production environments.

Here's a brief overview of some commonly used endpoints provided by Spring Boot Actuator:

/actuator/health: Provides information about the health of the application.
/actuator/info: Displays arbitrary application information.
/actuator/metrics: Exposes a wide range of metrics about the running application.
/actuator/env: Displays the current environment properties.
/actuator/loggers: Allows viewing and modifying logging levels.
/actuator/trace: Provides information about recent HTTP requests.
/actuator/threaddump: Outputs a thread dump of the application.

 */