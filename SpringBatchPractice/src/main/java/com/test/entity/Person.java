package com.test.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PERSON")
@Getter
@Setter
public class Person {

	@Id
	private long id;
	
	@Version
	private long version;
	
	private String userId;
	private String firstName;
	private String lastName;
	private String gender;
	private String email;
	private String phone;
	private String dob;
	private String jobTitle;
	

}
