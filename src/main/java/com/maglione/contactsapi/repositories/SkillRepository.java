package com.maglione.contactsapi.repositories;

import com.maglione.contactsapi.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findSkillsByPersonsId(Long personId);

    List<Skill> findByLevelAndName(String level, String name);
}
