package com.zerti.utilities.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionInfo {

	private final String cause;
	private final List<String> fields;

	@JsonIgnore
	private final StackTraceElement[] stackTraceElement;
}
