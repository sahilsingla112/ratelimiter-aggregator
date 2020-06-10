package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 07-06-2020
 */
@RestController
@RequestMapping("/api/v1/books")
public class BookController {
	@RequestMapping(value = "/available")
	public String available() {
		return "Spring in Action";
	}

	@RequestMapping(value = "/checked-out")
	public String checkedOut() {
		return "Spring Boot in Action";
	}

	@PostMapping(value = "/buy")
	public ResponseEntity<String> buyBooks(@RequestBody final Book request){
		if (request != null)
			return new ResponseEntity<>("You have bought a new book " + request.getName() + "!", HttpStatus.OK);
		else
			return new ResponseEntity<>("Please mention some book name in the request", HttpStatus.BAD_REQUEST);
	}
}
