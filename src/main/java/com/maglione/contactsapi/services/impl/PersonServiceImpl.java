package com.maglione.contactsapi.services.impl;

import com.maglione.contactsapi.controllers.PersonController;
import com.maglione.contactsapi.domain.Dto.PersonDto;
import com.maglione.contactsapi.domain.Dto.PersonListDto;
import com.maglione.contactsapi.domain.Dto.SkillDto;
import com.maglione.contactsapi.domain.Person;
import com.maglione.contactsapi.domain.Skill;
import com.maglione.contactsapi.domain.mappers.PersonMapper;
import com.maglione.contactsapi.domain.mappers.SkillMapper;
import com.maglione.contactsapi.repositories.PersonRepository;
import com.maglione.contactsapi.services.PersonService;
import com.maglione.contactsapi.services.exceptions.ResourceNotFoundException;
import com.maglione.contactsapi.services.exceptions.ResourceUniqueConstraintException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final SkillMapper skillMapper;

    public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper, SkillMapper skillMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.skillMapper = skillMapper;
    }

    @Override
    public PersonListDto getAllPersons() {
        List<PersonDto> personDtos = personRepository
                .findAll()
                .stream()
                .map(person -> {
                    PersonDto personDto = personMapper.convertToDto(person);
                    personDto.setPersonUrl(getPersonUrl(person.getId()));
                    return personDto;
                })
                .collect(Collectors.toList());

        return new PersonListDto(personDtos);
    }

    @Override
    public PersonDto getPersonById(Long id) {

        return personRepository.findById(id)
                .map(personMapper::convertToDto)
                .map(personDto -> {
                    personDto.setPersonUrl(getPersonUrl(id));
                    return personDto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));
    }

    @Override
    public PersonListDto getPersonByDtoEmail(PersonDto personDTO) {

        List<PersonDto> personDtos = personRepository
                .findByEmail(personDTO.getEmail()).stream()
                .map(personMapper::convertToDto)
                .map(personDto -> {
                    personDto.setPersonUrl(getPersonUrl(personDTO.getId()));
                    return personDto;
                }).collect(Collectors.toList());

        return new PersonListDto(personDtos);

    }

    @Override
    public PersonDto createNewPerson(PersonDto personDTO) {
        return saveAndReturnDTO(personMapper.convertToEntity(personDTO));
    }

    @Override
    public PersonDto savePersonByDTO(Long id, PersonDto personDTO) {
        Person personToSave = personMapper.convertToEntity(personDTO);
        getPersonById(id);

        personToSave.setId(id);

        return saveAndReturnDTO(personToSave);
    }

    @Override
    public PersonDto addSkillByDto(PersonDto personDTO, SkillDto skillDTO) {
        Person personToSave = personMapper.convertToEntity(personDTO);
        Skill skillToAdd = skillMapper.convertToEntity(skillDTO);
        personToSave.addSkill(skillToAdd);

        return saveAndReturnDTO(personToSave);
    }

    @Override
    public PersonDto removeSkillByDto(PersonDto personDTO, SkillDto skillDTO) {
        Person personToSave = personMapper.convertToEntity(personDTO);
        Skill skillToRemove = skillMapper.convertToEntity(skillDTO);
        personToSave.removeSkill(skillToRemove.getId());

        return saveAndReturnDTO(personToSave);
    }

    @Override
    public PersonDto patchPerson(Long id, PersonDto personDTO) {
        return personRepository.findById(id).map(person -> {

            if (personDTO.getFirstname() != null) {
                person.setFirstname(personDTO.getFirstname());
            }

            if (personDTO.getLastname() != null) {
                person.setLastname(personDTO.getLastname());
            }

            if (personDTO.getEmail() != null) {
                person.setEmail(personDTO.getEmail());
            }

            if (personDTO.getMobilePhone() != null) {
                person.setMobilePhone(personDTO.getMobilePhone());
            }

            if (personDTO.getCity() != null) {
                person.setCity(personDTO.getCity());
            }

            if (personDTO.getAddress() != null) {
                person.setAddress(personDTO.getAddress());
            }

            PersonDto returnDto = personMapper.convertToDto(personRepository.save(person));
            returnDto.setPersonUrl(getPersonUrl(id));

            return returnDto;

        }).orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));
    }

    @Override
    public void deletePersonById(Long id) {
        getPersonById(id);
        personRepository.deleteById(id);
    }

    private String getPersonUrl(Long id) {
        return PersonController.BASE_URL + "/" + id;
    }

    private PersonDto saveAndReturnDTO(Person person) {
        Person savedPerson = personRepository.save(person);
        PersonDto returnDto = personMapper.convertToDto(savedPerson);
        returnDto.setPersonUrl(getPersonUrl(savedPerson.getId()));
        return returnDto;
    }

    public List<PersonDto> verifyUniquePerson(PersonDto personDto) {
        List<PersonDto> personListDto = getPersonByDtoEmail(personDto).getPersons();
        if (!personListDto.isEmpty()) {
            throw new ResourceUniqueConstraintException("Person with email " + personDto.getEmail() + " already exists");
        }

        return personListDto;

    }
}
