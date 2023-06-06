package com.maglione.contactsapi.domain.mappers;

import com.maglione.contactsapi.domain.Dto.SkillDto;
import com.maglione.contactsapi.domain.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SkillMapper {

    SkillMapper INSTANCE = Mappers.getMapper(SkillMapper.class);

    SkillDto convertToDto(Skill skill);

    Skill convertToEntity(SkillDto skillDto);

}
