package com.project.project_rest_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class ProjectRestApiApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ProjectRestApiApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));

		app.run(args);
	}

}
