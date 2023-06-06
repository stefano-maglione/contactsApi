package com.maglione.contactsapi.services.impl;

import com.maglione.contactsapi.controllers.PersonController;
import com.maglione.contactsapi.controllers.SkillController;
import com.maglione.contactsapi.domain.Dto.PersonDto;
import com.maglione.contactsapi.domain.Dto.PersonListDto;
import com.maglione.contactsapi.domain.Dto.SkillListDto;
import com.maglione.contactsapi.domain.Skill;
import com.maglione.contactsapi.domain.mappers.PersonMapper;
import com.maglione.contactsapi.domain.mappers.SkillMapper;
import com.maglione.contactsapi.repositories.PersonRepository;
import com.maglione.contactsapi.repositories.SkillRepository;
import com.maglione.contactsapi.services.PersonService;
import com.maglione.contactsapi.services.SkillService;
import com.maglione.contactsapi.services.exceptions.ResourceNotFoundException;
import com.maglione.contactsapi.services.exceptions.ResourceUniqueConstraintException;
import org.springframework.stereotype.Service;
import com.maglione.contactsapi.domain.Dto.SkillDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    private final PersonRepository personRepository;
    private final SkillMapper skillMapper;

    private final PersonMapper personMapper;
    private final PersonService personService;

    public SkillServiceImpl(SkillRepository skillRepository, PersonRepository personRepository, SkillMapper skillMapper, PersonMapper personMapper, PersonService personService) {
        this.skillRepository = skillRepository;
        this.personRepository = personRepository;
        this.skillMapper = skillMapper;
        this.personMapper = personMapper;

        this.personService = personService;
    }


    @Override
    public SkillListDto getAllSkills() {

        List<SkillDto> skillDtos = skillRepository
                .findAll()
                .stream()
                .map(skill -> {
                    SkillDto skillDto = skillMapper.convertToDto(skill);
                    skillDto.setSkillUrl(getSkillUrl(skill.getId()));
                    return skillDto;
                })
                .collect(Collectors.toList());

        return new SkillListDto(skillDtos);
    }

    @Override
    public SkillDto getSkillById(Long id) {

        return skillRepository.findById(id)
                .map(skillMapper::convertToDto)
                .map(skillDto -> {
                    skillDto.setSkillUrl(getSkillUrl(id));
                    return skillDto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Skill with id " + id + " not found"));
    }

    public SkillListDto getSkillByPerson(Long id) {

        personService.getPersonById(id);

        List<SkillDto> skillDtos = skillRepository
                .findSkillsByPersonsId(id)
                .stream()
                .map(skill -> {
                    SkillDto skillDto = skillMapper.convertToDto(skill);
                    skillDto.setSkillUrl(getSkillUrl(skill.getId()));
                    return skillDto;
                })
                .collect(Collectors.toList());


        return new SkillListDto(skillDtos);
    }

    @Override
    public SkillDto createNewSkill(SkillDto skillDTO) {
        return saveAndReturnDTO(skillMapper.convertToEntity(skillDTO));
    }

    @Override
    public SkillDto saveSkillByDTO(Long id, SkillDto skillDTO) {
        Skill skillToSave = skillMapper.convertToEntity(skillDTO);
        skillToSave.setId(id);

        return saveAndReturnDTO(skillToSave);
    }

    @Override
    public SkillDto patchSkill(Long id, SkillDto skillDTO) {
        return skillRepository.findById(id).map(skill -> {

            if (skillDTO.getName() != null) {
                skill.setName(skillDTO.getName());
            }

            if (skillDTO.getLevel() != null) {
                skill.setLevel(skillDTO.getLevel());
            }

            SkillDto returnDto = skillMapper.convertToDto(skillRepository.save(skill));
            returnDto.setSkillUrl(getSkillUrl(id));

            return returnDto;

        }).orElseThrow(() -> new ResourceNotFoundException("Skill with id " + id + " not found"));
    }

    public PersonListDto getPersonsBySkill(Long id) { /////

        getSkillById(id);

        List<PersonDto> personDtos = personRepository
                .findPersonsBySkillsId(id)
                .stream()
                .map(person -> {
                    PersonDto personDto = personMapper.convertToDto(person);
                    personDto.setPersonUrl(PersonController.BASE_URL + "/" + person.getId());
                    return personDto;
                })
                .collect(Collectors.toList());


        return new PersonListDto(personDtos);
    }

    @Override
    public void deleteSkillById(Long id) {
        getSkillById(id);
        skillRepository.deleteById(id);
    }

    @Override
    public SkillListDto getSkillByDtoLevelAndName(SkillDto skillDTO) {

        List<SkillDto> skillDtos = skillRepository
                .findByLevelAndName(skillDTO.getLevel(), skillDTO.getName()).stream()
                .map(skillMapper::convertToDto)
                .map(skillDto -> {
                    skillDto.setSkillUrl(getSkillUrl(skillDTO.getId()));
                    return skillDto;
                }).collect(Collectors.toList());

        return new SkillListDto(skillDtos);

    }


    private String getSkillUrl(Long id) {
        return SkillController.BASE_URL + "/" + id;
    }

    private SkillDto saveAndReturnDTO(Skill skill) {
        Skill savedSkill = skillRepository.save(skill);
        SkillDto returnDto = skillMapper.convertToDto(savedSkill);
        returnDto.setSkillUrl(getSkillUrl(savedSkill.getId()));
        return returnDto;
    }

    public List<SkillDto> verifyUniqueSkill(SkillDto skillDto) {
        List<SkillDto> skillListDto = getSkillByDtoLevelAndName(skillDto).getSkills();
        if (!skillListDto.isEmpty()) {
            throw new ResourceUniqueConstraintException("Skill with name " + skillDto.getName() + " and level " + skillDto.getLevel() + " already exists");
        }

        return skillListDto;

    }
}
