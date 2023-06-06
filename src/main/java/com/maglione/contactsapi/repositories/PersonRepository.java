package com.maglione.contactsapi.repositories;

import com.maglione.contactsapi.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByEmail(String string);

    List<Person> findPersonsBySkillsId(Long skillId);

}
