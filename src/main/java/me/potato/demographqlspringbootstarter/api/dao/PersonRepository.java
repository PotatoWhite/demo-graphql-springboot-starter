package me.potato.demographqlspringbootstarter.api.dao;

import me.potato.demographqlspringbootstarter.api.entity.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
	Person findByEmail(String email);
}
