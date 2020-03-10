package com.gymsoft.domain.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CustomerDTO {
	private String name;
	private String email;
	private String mobile;
	private Date doj;
}
