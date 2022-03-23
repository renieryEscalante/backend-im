package com.marvel.common;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties({"localizedMessage","suppressed","stackTrace","cause"})
public class CustomException extends Exception {

	private String message;
	private HttpStatus httpStatus;
	private static final long serialVersionUID = 7367928397950106446L;

}
