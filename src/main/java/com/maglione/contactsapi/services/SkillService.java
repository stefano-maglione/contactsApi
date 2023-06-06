package com.maglione.contactsapi.services;

import com.maglione.contactsapi.domain.Dto.PersonListDto;
import com.maglione.contactsapi.domain.Dto.SkillDto;
import com.maglione.contactsapi.domain.Dto.SkillListDto;

import java.util.List;

public interface SkillService {

    SkillListDto getAllSkills();

    SkillDto getSkillById(Long id);

    SkillDto createNewSkill(SkillDto skillDTO);

    SkillDto saveSkillByDTO(Long id, SkillDto skillDTO);

    SkillDto patchSkill(Long id, SkillDto skillDTO);

    SkillListDto getSkillByPerson(Long id);

    void deleteSkillById(Long id);

    SkillListDto getSkillByDtoLevelAndName(SkillDto skillDTO);

    List<SkillDto> verifyUniqueSkill(SkillDto skillDto);

    PersonListDto getPersonsBySkill(Long id);


}
