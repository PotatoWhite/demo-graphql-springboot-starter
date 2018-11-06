package me.potato.demographqlspringbootstarter.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;


@Getter
@Setter
@Entity
public class Person {
	@Id
	private Long id;
	private String name;
	private String email;
	private String mobile;
	private String[] address;
}
