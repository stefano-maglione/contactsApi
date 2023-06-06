package com.maglione.contactsapi.services;

import com.maglione.contactsapi.domain.Dto.PersonDto;
import com.maglione.contactsapi.domain.Dto.PersonListDto;
import com.maglione.contactsapi.domain.Dto.SkillDto;

import java.util.List;


public interface PersonService {

    PersonListDto getAllPersons();

    PersonDto getPersonById(Long id);

    PersonDto createNewPerson(PersonDto personDTO);

    PersonDto savePersonByDTO(Long id, PersonDto personDTO);

    PersonDto patchPerson(Long id, PersonDto personDTO);

    void deletePersonById(Long id);

    PersonDto addSkillByDto(PersonDto personDto, SkillDto skillDto);

    PersonDto removeSkillByDto(PersonDto personDTO, SkillDto skillDTO);

    PersonListDto getPersonByDtoEmail(PersonDto personDTO);

    List<PersonDto> verifyUniquePerson(PersonDto personDto);

}
